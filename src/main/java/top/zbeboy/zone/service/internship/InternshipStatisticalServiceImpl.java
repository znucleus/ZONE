package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.records.InternshipApplyRecord;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import java.util.Objects;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("internshipStatisticalService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipStatisticalServiceImpl implements InternshipStatisticalService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Autowired
    InternshipStatisticalServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                        .join(STUDENT)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .join(ORGANIZE)
                        .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                        .join(GRADE)
                        .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                        .join(SCIENCE)
                        .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                        .join(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(INTERNSHIP_APPLY)
                        .on(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)
                                .and(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(INTERNSHIP_APPLY.STUDENT_ID)));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .join(STUDENT)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .join(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(INTERNSHIP_APPLY)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)
                        .and(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(INTERNSHIP_APPLY.STUDENT_ID)));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .join(STUDENT)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .join(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(INTERNSHIP_APPLY)
                .on(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)
                        .and(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID.eq(INTERNSHIP_APPLY.STUDENT_ID)));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countSubmitted(String internshipReleaseId) {
        Record1<Integer> record1 = create.selectCount()
                .from(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)).fetchOne();
        return record1.value1();
    }

    @Override
    public int countUnSubmitted(String internshipReleaseId) {
        Select<InternshipApplyRecord> select = create.selectFrom(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.STUDENT_ID.eq(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID))
                .and(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId));
        Record1<Integer> record1 = create.selectCount()
                .from(INTERNSHIP_TEACHER_DISTRIBUTION)
                .where(INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                        .andNotExists(select)).fetchOne();
        return record1.value1();
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String organizeId = StringUtils.trim(search.getString("organizeId"));
            String internshipApplyState = StringUtils.trim(search.getString("internshipApplyState"));
            if (StringUtils.isNotBlank(realName)) {
                a = USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName));
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = STUDENT.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(STUDENT.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
                }
            }

            if (StringUtils.isNotBlank(organizeId)) {
                if (Objects.isNull(a)) {
                    a = ORGANIZE.ORGANIZE_ID.eq(NumberUtils.toInt(organizeId));
                } else {
                    a = a.and(ORGANIZE.ORGANIZE_ID.eq(NumberUtils.toInt(organizeId)));
                }
            }

            if (StringUtils.isNotBlank(internshipApplyState)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.eq(NumberUtils.toInt(internshipApplyState));
                } else {
                    a = a.and(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.eq(NumberUtils.toInt(internshipApplyState)));
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
            String dataRange = StringUtils.trim(search.getString("dataRange"));
            if (StringUtils.isBlank(dataRange)) {
                dataRange = "0";// 默认未申请的
            }

            a = INTERNSHIP_TEACHER_DISTRIBUTION.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);

            int dataRangeInt = NumberUtils.toInt(dataRange);

            Select<InternshipApplyRecord> select = create.selectFrom(INTERNSHIP_APPLY)
                    .where(INTERNSHIP_APPLY.STUDENT_ID.eq(INTERNSHIP_TEACHER_DISTRIBUTION.STUDENT_ID))
                    .and(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId));

            if (dataRangeInt == 1) {
                a = a.andExists(select);
            } else {
                a = a.andNotExists(select);
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
            if (StringUtils.equals("realName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
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

            if (StringUtils.equals("scienceName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SCIENCE.SCIENCE_NAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = SCIENCE.SCIENCE_NAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("organizeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("internshipApplyState", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
