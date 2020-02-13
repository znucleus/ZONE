package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.*;

@Service("internshipReviewService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipReviewServiceImpl implements InternshipReviewService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Autowired
    InternshipReviewServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(INTERNSHIP_APPLY)
                .leftJoin(STUDENT)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .leftJoin(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .leftJoin(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(INTERNSHIP_INFO)
                .on(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)
                        .and(INTERNSHIP_INFO.STUDENT_ID.eq(INTERNSHIP_APPLY.STUDENT_ID)))
                .leftJoin(FILES)
                .on(INTERNSHIP_APPLY.FILE_ID.eq(FILES.FILE_ID));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(INTERNSHIP_APPLY)
                .leftJoin(STUDENT)
                .on(INTERNSHIP_APPLY.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .leftJoin(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .leftJoin(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .leftJoin(INTERNSHIP_INFO)
                .on(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID.eq(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID)
                        .and(INTERNSHIP_INFO.STUDENT_ID.eq(INTERNSHIP_APPLY.STUDENT_ID)))
                .leftJoin(FILES)
                .on(INTERNSHIP_APPLY.FILE_ID.eq(FILES.FILE_ID));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countByInternshipReleaseIdAndInternshipApplyState(String internshipReleaseId, int internshipApplyState) {
        return create.selectCount()
                .from(INTERNSHIP_APPLY)
                .where(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.eq(internshipApplyState)))
                .fetchOne().value1();
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String organizeId = StringUtils.trim(search.getString("organizeId"));
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
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();

        if (Objects.nonNull(search)) {
            String internshipReleaseId = StringUtils.trim(search.getString("internshipReleaseId"));
            String internshipApplyState = StringUtils.trim(search.getString("internshipApplyState"));

            a = INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
                    .and(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE.eq(NumberUtils.toInt(internshipApplyState)));
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
            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = STUDENT.STUDENT_NUMBER.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
