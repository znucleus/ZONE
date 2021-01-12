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
import top.zbeboy.zbase.tools.web.util.pagination.TableSawUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import java.util.Objects;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryAttendMyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryAttendMyServiceImpl implements TheoryAttendMyService, PaginationPlugin<TableSawUtil> {

    private final DSLContext create;

    @Autowired
    TheoryAttendMyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAll(TableSawUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(THEORY_USERS)
                        .leftJoin(STUDENT)
                        .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .leftJoin(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(THEORY_ATTEND)
                        .on(THEORY_ATTEND.THEORY_RELEASE_ID.eq(THEORY_USERS.THEORY_RELEASE_ID))
                        .leftJoin(create.selectFrom(THEORY_ATTEND_USERS).asTable("OUTER"))
                        .on(DSL.field("OUTER.theory_users_id").eq(THEORY_USERS.THEORY_USERS_ID)
                                .and(DSL.field("OUTER.theory_attend_id").eq(THEORY_ATTEND.THEORY_ATTEND_ID)));
        return queryAll(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public Condition searchCondition(TableSawUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String attendDate = StringUtils.trim(search.getString("attendDate"));
            String operate = StringUtils.trim(search.getString("operate"));
            String studentId = StringUtils.trim(search.getString("studentId"));
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

            if (StringUtils.isNotBlank(studentId)) {
                int studentIdInt = NumberUtils.toInt(studentId);
                if (Objects.isNull(a)) {
                    a = THEORY_USERS.STUDENT_ID.eq(studentIdInt);
                } else {
                    a = a.and(THEORY_USERS.STUDENT_ID.eq(studentIdInt));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(TableSawUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String theoryReleaseId = StringUtils.trim(search.getString("theoryReleaseId"));
            if (StringUtils.isNotBlank(theoryReleaseId)) {
                a = THEORY_ATTEND.THEORY_RELEASE_ID.eq(theoryReleaseId);
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, TableSawUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
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
        }
        sortToFinish(step, sortField);
    }
}
