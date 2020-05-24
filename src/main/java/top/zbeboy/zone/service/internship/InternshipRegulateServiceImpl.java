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
import top.zbeboy.zone.domain.tables.daos.InternshipRegulateDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipRegulate;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_REGULATE;
import static top.zbeboy.zone.domain.Tables.STAFF;

@Service("internshipRegulateService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipRegulateServiceImpl implements InternshipRegulateService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private InternshipRegulateDao internshipRegulateDao;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Autowired
    InternshipRegulateServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public InternshipRegulate findById(String id) {
        return internshipRegulateDao.findById(id);
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, INTERNSHIP_REGULATE, dataTablesUtil, false);
    }

    @Override
    public Result<Record> export(DataTablesUtil dataTablesUtil) {
        return queryAll(create, INTERNSHIP_REGULATE, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_REGULATE, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_REGULATE, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipRegulate internshipRegulate) {
        internshipRegulateDao.insert(internshipRegulate);
    }

    @Override
    public void update(InternshipRegulate internshipRegulate) {
        internshipRegulateDao.update(internshipRegulate);
    }

    @Override
    public void deleteById(String id) {
        internshipRegulateDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String studentName = StringUtils.trim(search.getString("studentName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String schoolGuidanceTeacher = StringUtils.trim(search.getString("schoolGuidanceTeacher"));
            String createDate = StringUtils.trim(search.getString("createDate"));
            if (StringUtils.isNotBlank(studentName)) {
                a = INTERNSHIP_REGULATE.STUDENT_NAME.like(SQLQueryUtil.likeAllParam(studentName));
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_REGULATE.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(INTERNSHIP_REGULATE.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
                }
            }

            if (StringUtils.isNotBlank(schoolGuidanceTeacher)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_REGULATE.SCHOOL_GUIDANCE_TEACHER.like(SQLQueryUtil.likeAllParam(schoolGuidanceTeacher));
                } else {
                    a = a.and(INTERNSHIP_REGULATE.SCHOOL_GUIDANCE_TEACHER.like(SQLQueryUtil.likeAllParam(schoolGuidanceTeacher)));
                }
            }

            if (StringUtils.isNotBlank(createDate)) {
                Timestamp startTime;
                Timestamp endTime;
                if (createDate.contains("至")) {
                    String[] arr = createDate.split(" 至 ");
                    startTime = DateTimeUtil.defaultParseSqlTimestamp(arr[0] + " 00:00:00");
                    endTime = DateTimeUtil.defaultParseSqlTimestamp(arr[1] + " 23:59:59");
                } else {
                    startTime = DateTimeUtil.defaultParseSqlTimestamp(createDate + " 00:00:00");
                    endTime = DateTimeUtil.defaultParseSqlTimestamp(createDate + " 23:59:59");
                }
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_REGULATE.CREATE_DATE.gt(startTime).and(INTERNSHIP_REGULATE.CREATE_DATE.le(endTime));
                } else {
                    a = a.and(INTERNSHIP_REGULATE.CREATE_DATE.gt(startTime).and(INTERNSHIP_REGULATE.CREATE_DATE.le(endTime)));
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
            a = INTERNSHIP_REGULATE.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);

            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认全部
            }

            int dataRangeInt = NumberUtils.toInt(dataRange);
            // 个人
            if (dataRangeInt == 1) {
                int staffId = 0;
                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            staffId = record.get().get(STAFF.STAFF_ID);
                        }
                    }
                }
                a = a.and(INTERNSHIP_REGULATE.STAFF_ID.eq(staffId));
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
                    sortField[0] = INTERNSHIP_REGULATE.STUDENT_NAME.asc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_REGULATE.STUDENT_NAME.desc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_REGULATE.STUDENT_NUMBER.asc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_REGULATE.STUDENT_NUMBER.desc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("studentTel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_REGULATE.STUDENT_TEL.asc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_REGULATE.STUDENT_TEL.desc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("schoolGuidanceTeacher", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_REGULATE.SCHOOL_GUIDANCE_TEACHER.asc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_REGULATE.SCHOOL_GUIDANCE_TEACHER.desc();
                    sortField[1] = INTERNSHIP_REGULATE.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("createDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_REGULATE.CREATE_DATE.asc();
                } else {
                    sortField[0] = INTERNSHIP_REGULATE.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
