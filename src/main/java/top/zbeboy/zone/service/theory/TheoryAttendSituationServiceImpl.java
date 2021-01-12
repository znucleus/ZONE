package top.zbeboy.zone.service.theory;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import java.util.Objects;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryAttendSituationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryAttendSituationServiceImpl implements TheoryAttendSituationService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Autowired
    TheoryAttendSituationServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(THEORY_USERS)
                        .leftJoin(STUDENT)
                        .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .leftJoin(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(THEORY_ATTEND)
                        .on(THEORY_ATTEND.THEORY_RELEASE_ID.eq(THEORY_USERS.THEORY_RELEASE_ID))
                        .leftJoin(ORGANIZE)
                        .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                        .leftJoin(create.selectFrom(THEORY_ATTEND_USERS).asTable("OUTER"))
                        .on(DSL.field("OUTER.theory_users_id").eq(THEORY_USERS.THEORY_USERS_ID)
                                .and(DSL.field("OUTER.theory_attend_id").eq(THEORY_ATTEND.THEORY_ATTEND_ID)));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public Result<Record> export(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(THEORY_USERS)
                        .leftJoin(STUDENT)
                        .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .leftJoin(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(THEORY_ATTEND)
                        .on(THEORY_ATTEND.THEORY_RELEASE_ID.eq(THEORY_USERS.THEORY_RELEASE_ID))
                        .leftJoin(ORGANIZE)
                        .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                        .leftJoin(create.selectFrom(THEORY_ATTEND_USERS).asTable("OUTER"))
                        .on(DSL.field("OUTER.theory_users_id").eq(THEORY_USERS.THEORY_USERS_ID)
                                .and(DSL.field("OUTER.theory_attend_id").eq(THEORY_ATTEND.THEORY_ATTEND_ID)));
        return queryAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_USERS)
                .leftJoin(STUDENT)
                .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(THEORY_ATTEND)
                .on(THEORY_ATTEND.THEORY_RELEASE_ID.eq(THEORY_USERS.THEORY_RELEASE_ID))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .leftJoin(create.selectFrom(THEORY_ATTEND_USERS).asTable("OUTER"))
                .on(DSL.field("OUTER.theory_users_id").eq(THEORY_USERS.THEORY_USERS_ID)
                        .and(DSL.field("OUTER.theory_attend_id").eq(THEORY_ATTEND.THEORY_ATTEND_ID)));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_USERS)
                .leftJoin(STUDENT)
                .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(THEORY_ATTEND)
                .on(THEORY_ATTEND.THEORY_RELEASE_ID.eq(THEORY_USERS.THEORY_RELEASE_ID))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID))
                .leftJoin(create.selectFrom(THEORY_ATTEND_USERS).asTable("OUTER"))
                .on(DSL.field("OUTER.theory_users_id").eq(THEORY_USERS.THEORY_USERS_ID)
                        .and(DSL.field("OUTER.theory_attend_id").eq(THEORY_ATTEND.THEORY_ATTEND_ID)));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String attendDate = StringUtils.trim(search.getString("attendDate"));
            String operate = StringUtils.trim(search.getString("operate"));
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            if (StringUtils.isNotBlank(attendDate)) {
                java.sql.Date startDate;
                java.sql.Date endDate;
                if (attendDate.contains("至")) {
                    String[] arr = attendDate.split(" 至 ");
                    startDate = DateTimeUtil.defaultParseSqlDate(arr[0]);
                    endDate = DateTimeUtil.defaultParseSqlDate(arr[1]);
                } else {
                    startDate = DateTimeUtil.defaultParseSqlDate(attendDate);
                    endDate = DateTimeUtil.defaultParseSqlDate(attendDate);
                }

                a = THEORY_ATTEND.ATTEND_DATE.ge(startDate).and(THEORY_ATTEND.ATTEND_DATE.le(endDate));
            }

            if (StringUtils.isNotBlank(operate)) {
                Byte operateByte = NumberUtils.toByte(operate);
                if (Objects.isNull(a)) {
                    a = DSL.field("OUTER.operate").eq(operateByte);
                } else {
                    a = a.and(DSL.field("OUTER.operate").eq(operateByte));
                }
            }

            if (StringUtils.isNotBlank(realName)) {
                if (Objects.isNull(a)) {
                    a = USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName));
                } else {
                    a = a.and(USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName)));
                }
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = STUDENT.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(STUDENT.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
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
            String theoryReleaseId = StringUtils.trim(search.getString("theoryReleaseId"));
            if (StringUtils.isNotBlank(theoryReleaseId)) {
                a = THEORY_USERS.THEORY_RELEASE_ID.eq(theoryReleaseId);
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
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.STUDENT_NUMBER.asc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = STUDENT.STUDENT_NUMBER.desc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }

            if (StringUtils.equals("attendDate", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = THEORY_ATTEND.ATTEND_DATE.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = THEORY_ATTEND.ATTEND_DATE.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("organizeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.asc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.desc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }

            if (StringUtils.equals("sex", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.SEX.asc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = STUDENT.SEX.desc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }

            if (StringUtils.equals("operate", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = DSL.field("OUTER.operate").asc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = DSL.field("OUTER.operate").desc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }

            if (StringUtils.equals("remark", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = THEORY_USERS.REMARK.asc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.asc();
                } else {
                    sortField[0] = THEORY_USERS.REMARK.desc();
                    sortField[1] = THEORY_ATTEND.ATTEND_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
