package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.InternshipTeacherDistributionDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipTeacherDistribution;
import top.zbeboy.zone.domain.tables.records.InternshipApplyRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.bean.internship.distribution.InternshipTeacherDistributionBean;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("internshipTeacherDistributionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipTeacherDistributionServiceImpl implements InternshipTeacherDistributionService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private InternshipTeacherDistributionDao internshipTeacherDistributionDao;

    @Autowired
    InternshipTeacherDistributionServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.INTERNSHIP_TEACHER_DISTRIBUTION, key = "#internshipReleaseId + '_' + #studentId")
    @Override
    public Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        return create.select()
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public Result<Record> findByInternshipReleaseIdAndStaffId(String internshipReleaseId, int staffId) {
        return create.select()
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(staffId)))
                .fetch();
    }

    @Override
    public Result<Record2<Integer, String>> findByInternshipReleaseIdAndDistinctStaffId(String internshipReleaseId) {
        return create.selectDistinct(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID,
                USERS.REAL_NAME)
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .join(STAFF)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(STAFF.STAFF_ID))
                .join(USERS)
                .on(STAFF.USERNAME.eq(USERS.USERNAME))
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId))
                .fetch();
    }

    @Override
    public List<InternshipTeacherDistributionBean> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                        .join(STAFF)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(STAFF.STAFF_ID))
                        .join(USERS)
                        .on(STAFF.USERNAME.eq(USERS.USERNAME))
                        .join(STUDENT)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID));
        return (List<InternshipTeacherDistributionBean>) queryAllByPageAndBuildData(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public List<InternshipTeacherDistributionBean> export(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                        .join(STAFF)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(STAFF.STAFF_ID))
                        .join(USERS)
                        .on(STAFF.USERNAME.eq(USERS.USERNAME))
                        .join(STUDENT)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID));
        return (List<InternshipTeacherDistributionBean>) queryAllAndBuildData(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep =
                create.selectCount()
                        .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                        .join(STAFF)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(STAFF.STAFF_ID))
                        .join(USERS)
                        .on(STAFF.USERNAME.eq(USERS.USERNAME))
                        .join(STUDENT)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .join(STAFF)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID.eq(STAFF.STAFF_ID))
                .join(USERS)
                .on(STAFF.USERNAME.eq(USERS.USERNAME))
                .join(STUDENT)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(InternshipTeacherDistribution internshipTeacherDistribution) {
        internshipTeacherDistributionDao.insert(internshipTeacherDistribution);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<InternshipTeacherDistribution> internshipTeacherDistribution) {
        internshipTeacherDistributionDao.insert(internshipTeacherDistribution);
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_TEACHER_DISTRIBUTION, key = "#internshipTeacherDistribution.internshipReleaseId + '_' + #internshipTeacherDistribution.studentId")
    @Override
    public void updateStaff(InternshipTeacherDistribution internshipTeacherDistribution) {
        create.update(INTERNSHIP_TEACHER_DISTRIBUTION)
                .set(INTERNSHIP_TEACHER_DISTRIBUTION.STAFF_ID, internshipTeacherDistribution.getStaffId())
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipTeacherDistribution.getInternshipReleaseId())
                        .and(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(internshipTeacherDistribution.getStudentId())))
                .execute();
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_TEACHER_DISTRIBUTION, allEntries = true)
    @Override
    public void deleteByInternshipReleaseId(String internshipReleaseId) {
        create.deleteFrom(INTERNSHIP_TEACHER_DISTRIBUTION)
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId))
                .execute();
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_TEACHER_DISTRIBUTION, key = "#internshipReleaseId + '_' + #studentId")
    @Override
    public void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        create.deleteFrom(INTERNSHIP_TEACHER_DISTRIBUTION)
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .and(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(studentId)))
                .execute();
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_TEACHER_DISTRIBUTION, allEntries = true)
    @Override
    public void deleteNotApply(String internshipReleaseId) {
        SelectConditionStep<InternshipApplyRecord> internshipApplyRecord = create.selectFrom(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.STUDENT_ID.eq(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID).and(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)));
        create.deleteFrom(INTERNSHIP_TEACHER_DISTRIBUTION)
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).andNotExists(internshipApplyRecord))
                .execute();
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String studentUsername = StringUtils.trim(search.getString("studentUsername"));
            String staffUsername = StringUtils.trim(search.getString("staffUsername"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String staffNumber = StringUtils.trim(search.getString("staffNumber"));
            String username = StringUtils.trim(search.getString("username"));
            String assigner = StringUtils.trim(search.getString("assigner"));
            if (StringUtils.isNotBlank(studentUsername)) {
                a = STUDENT.USERNAME.like(SQLQueryUtil.likeAllParam(studentUsername));
            }

            if (StringUtils.isNotBlank(staffUsername)) {
                if (Objects.isNull(a)) {
                    a = STAFF.USERNAME.like(SQLQueryUtil.likeAllParam(staffUsername));
                } else {
                    a = a.and(STAFF.USERNAME.like(SQLQueryUtil.likeAllParam(staffUsername)));
                }
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = STUDENT.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(STUDENT.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
                }
            }

            if (StringUtils.isNotBlank(staffNumber)) {
                if (Objects.isNull(a)) {
                    a = STAFF.STAFF_NUMBER.like(SQLQueryUtil.likeAllParam(staffNumber));
                } else {
                    a = a.and(STAFF.STAFF_NUMBER.like(SQLQueryUtil.likeAllParam(staffNumber)));
                }
            }

            if (StringUtils.isNotBlank(username)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_TEACHER_DISTRIBUTION.USERNAME.like(SQLQueryUtil.likeAllParam(username));
                } else {
                    a = a.and(INTERNSHIP_TEACHER_DISTRIBUTION.USERNAME.like(SQLQueryUtil.likeAllParam(username)));
                }
            }

            if (StringUtils.isNotBlank(assigner)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_TEACHER_DISTRIBUTION.ASSIGNER.like(SQLQueryUtil.likeAllParam(assigner));
                } else {
                    a = a.and(INTERNSHIP_TEACHER_DISTRIBUTION.ASSIGNER.like(SQLQueryUtil.likeAllParam(assigner)));
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
            if (StringUtils.isNotBlank(internshipReleaseId)) {
                a = INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);
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
            if (StringUtils.equals("studentRealName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_REAL_NAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_REAL_NAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("studentUsername", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = STUDENT.USERNAME.asc();
                } else {
                    sortField[0] = STUDENT.USERNAME.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("staffRealName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("staffUsername", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STAFF.USERNAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = STAFF.USERNAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("staffNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STAFF.STAFF_NUMBER.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = STAFF.STAFF_NUMBER.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("assigner", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_TEACHER_DISTRIBUTION.ASSIGNER.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_TEACHER_DISTRIBUTION.ASSIGNER.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_TEACHER_DISTRIBUTION.USERNAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_TEACHER_DISTRIBUTION.USERNAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }

    @Override
    public List<InternshipTeacherDistributionBean> buildData(Result<Record> records) {
        List<InternshipTeacherDistributionBean> beans = new ArrayList<>();
        if (records.isNotEmpty()) {
            for (Record r : records) {
                InternshipTeacherDistributionBean internshipTeacherDistributionBeen = new InternshipTeacherDistributionBean();
                internshipTeacherDistributionBeen.setStudentRealName(r.getValue(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_REAL_NAME));
                internshipTeacherDistributionBeen.setStudentUsername(r.getValue(STUDENT.USERNAME));
                internshipTeacherDistributionBeen.setStudentNumber(r.getValue(STUDENT.STUDENT_NUMBER));
                internshipTeacherDistributionBeen.setStudentId(r.getValue(STUDENT.STUDENT_ID));
                internshipTeacherDistributionBeen.setStaffRealName(r.getValue(USERS.REAL_NAME));
                internshipTeacherDistributionBeen.setStaffUsername(r.getValue(STAFF.USERNAME));
                internshipTeacherDistributionBeen.setStaffNumber(r.getValue(STAFF.STAFF_NUMBER));
                internshipTeacherDistributionBeen.setAssigner(r.getValue(INTERNSHIP_TEACHER_DISTRIBUTION.ASSIGNER));
                internshipTeacherDistributionBeen.setUsername(r.getValue(INTERNSHIP_TEACHER_DISTRIBUTION.USERNAME));

                beans.add(internshipTeacherDistributionBeen);
            }
        }
        return beans;
    }
}
