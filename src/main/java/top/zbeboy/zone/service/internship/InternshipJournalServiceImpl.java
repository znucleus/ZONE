package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("internshipJournalService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipJournalServiceImpl implements InternshipJournalService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Autowired
    InternshipJournalServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, INTERNSHIP_JOURNAL, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_JOURNAL, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_JOURNAL, dataTablesUtil, false);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String studentName = StringUtils.trim(search.getString("studentName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String organize = StringUtils.trim(search.getString("organize"));
            String guidanceTeacher = StringUtils.trim(search.getString("guidanceTeacher"));
            if (StringUtils.isNotBlank(studentName)) {
                a = INTERNSHIP_JOURNAL.STUDENT_NAME.like(SQLQueryUtil.likeAllParam(studentName));
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_JOURNAL.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(INTERNSHIP_JOURNAL.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
                }
            }

            if (StringUtils.isNotBlank(organize)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_JOURNAL.ORGANIZE.like(SQLQueryUtil.likeAllParam(organize));
                } else {
                    a = a.and(INTERNSHIP_JOURNAL.ORGANIZE.eq(SQLQueryUtil.likeAllParam(organize)));
                }
            }

            if (StringUtils.isNotBlank(guidanceTeacher)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER.like(SQLQueryUtil.likeAllParam(guidanceTeacher));
                } else {
                    a = a.and(INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER.eq(SQLQueryUtil.likeAllParam(guidanceTeacher)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String internshipReleaseId = StringUtils.trim(search.getString("internshipReleaseId"));
            a = INTERNSHIP_JOURNAL.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);

            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认全部
            }

            int dataRangeInt = NumberUtils.toInt(dataRange);
            // 个人
            if (dataRangeInt == 1) {
                int studentId = 0;
                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            studentId = record.get().get(STUDENT.STUDENT_ID);
                        }
                    }
                }
                a = a.and(INTERNSHIP_JOURNAL.STUDENT_ID.eq(studentId));
            } else if (dataRangeInt == 2) {
                // 小组
                String staffId = StringUtils.trim(search.getString("staffId"));
                List<Integer> staffs = SmallPropsUtil.StringIdsToNumberList(staffId);
                a = a.and(INTERNSHIP_JOURNAL.STAFF_ID.in(staffs));
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("studentName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NAME.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NAME.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NUMBER.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.STUDENT_NUMBER.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("organize", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.ORGANIZE.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.ORGANIZE.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("schoolGuidanceTeacher", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER.asc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER.desc();
                    sortField[1] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("createDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_JOURNAL.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_JOURNAL.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
