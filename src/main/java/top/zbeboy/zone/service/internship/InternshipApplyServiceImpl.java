package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.InternshipApplyDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipApply;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.InternshipReleaseRecord;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.*;

@Service("internshipApplyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipApplyServiceImpl implements InternshipApplyService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private InternshipApplyDao internshipApplyDao;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Autowired
    InternshipApplyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        return create.select()
                .from(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_APPLY.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public Result<Record2<Integer, String>> findDistinctOrganize(String internshipReleaseId) {
        return create.selectDistinct(ORGANIZE.ORGANIZE_ID, ORGANIZE.ORGANIZE_NAME)
                .from(INTERNSHIP_APPLY)
                .leftJoin(STUDENT)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(INTERNSHIP_APPLY)
                .leftJoin(INTERNSHIP_RELEASE)
                .on(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_ID))
                .leftJoin(STUDENT)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(INTERNSHIP_TYPE)
                .on(INTERNSHIP_TYPE.INTERNSHIP_TYPE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_TYPE_ID))
                .leftJoin(SCIENCE)
                .on(INTERNSHIP_RELEASE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.COLLEGE_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(FILES)
                .on(INTERNSHIP_APPLY.FILE_ID.eq(FILES.FILE_ID))
                .leftJoin(INTERNSHIP_INFO)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(INTERNSHIP_INFO.STUDENT_ID).and(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID)));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_APPLY)
                .leftJoin(INTERNSHIP_RELEASE)
                .on(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_ID))
                .leftJoin(STUDENT)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(INTERNSHIP_TYPE)
                .on(INTERNSHIP_TYPE.INTERNSHIP_TYPE_ID.eq(INTERNSHIP_RELEASE.INTERNSHIP_TYPE_ID))
                .leftJoin(SCIENCE)
                .on(INTERNSHIP_RELEASE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .leftJoin(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .leftJoin(SCHOOL)
                .on(COLLEGE.COLLEGE_ID.eq(SCHOOL.SCHOOL_ID))
                .leftJoin(FILES)
                .on(INTERNSHIP_APPLY.FILE_ID.eq(FILES.FILE_ID))
                .leftJoin(INTERNSHIP_INFO)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(INTERNSHIP_INFO.STUDENT_ID).and(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID)));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public void update(InternshipApply internshipApply) {
        internshipApplyDao.update(internshipApply);
    }

    @Override
    public void updateState(int curState, int updateState) {
        Select<InternshipReleaseRecord> select = create.selectFrom(INTERNSHIP_RELEASE)
                .where(INTERNSHIP_RELEASE.END_TIME.le(now()).and(INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)));
        create.update(INTERNSHIP_APPLY)
                .set(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE, updateState)
                .where(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.eq(curState).andExists(select))
                .execute();
    }

    @Override
    public void updateChangeState(List<Integer> curState, int updateState) {
        create.update(INTERNSHIP_APPLY)
                .set(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE, updateState)
                .where(INTERNSHIP_APPLY.CHANGE_FILL_END_TIME.le(now()).and(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.in(curState)))
                .execute();
    }

    @Override
    public void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        create.deleteFrom(INTERNSHIP_APPLY).where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
        .and(INTERNSHIP_APPLY.STUDENT_ID.eq(studentId))).execute();
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String internshipTitle = StringUtils.trim(search.getString("internshipTitle"));
            if (StringUtils.isNotBlank(internshipTitle)) {
                a = INTERNSHIP_RELEASE.INTERNSHIP_TITLE.like(SQLQueryUtil.likeAllParam(internshipTitle));
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    int studentId = record.get().get(STUDENT.STUDENT_ID);
                    a = STUDENT.STUDENT_ID.eq(studentId);
                }
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, SimplePaginationUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("applyTime", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_APPLY.APPLY_TIME.asc();
                } else {
                    sortField[0] = INTERNSHIP_APPLY.APPLY_TIME.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
