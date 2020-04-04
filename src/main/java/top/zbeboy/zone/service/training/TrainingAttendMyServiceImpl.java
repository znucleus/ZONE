package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.util.pagination.TableSawUtil;

import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.*;

@Service("trainingAttendMyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingAttendMyServiceImpl implements TrainingAttendMyService, PaginationPlugin<TableSawUtil> {

    private final DSLContext create;

    @Autowired
    TrainingAttendMyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAll(TableSawUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(TRAINING_ATTEND)
                .join(TRAINING_ATTEND_USERS)
                .on(TRAINING_ATTEND.TRAINING_ATTEND_ID.eq(TRAINING_ATTEND_USERS.TRAINING_ATTEND_ID))
                .join(TRAINING_USERS)
                .on(TRAINING_USERS.TRAINING_USERS_ID.eq(TRAINING_ATTEND_USERS.TRAINING_USERS_ID))
                .leftJoin(STUDENT)
                .on(TRAINING_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
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

                a = TRAINING_ATTEND.ATTEND_DATE.ge(startDate).and(TRAINING_ATTEND.ATTEND_DATE.le(endDate));
            }

            if (StringUtils.isNotBlank(operate)) {
                Byte operateByte = NumberUtils.toByte(operate);
                if (Objects.isNull(a)) {
                    a = TRAINING_ATTEND_USERS.OPERATE.eq(operateByte);
                } else {
                    a = a.and(TRAINING_ATTEND_USERS.OPERATE.eq(operateByte));
                }
            }

            if (StringUtils.isNotBlank(studentId)) {
                int studentIdInt = NumberUtils.toInt(studentId);
                if (Objects.isNull(a)) {
                    a = TRAINING_USERS.STUDENT_ID.eq(studentIdInt);
                } else {
                    a = a.and(TRAINING_USERS.STUDENT_ID.eq(studentIdInt));
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
            String trainingReleaseId = StringUtils.trim(search.getString("trainingReleaseId"));
            if (StringUtils.isNotBlank(trainingReleaseId)) {
                a = TRAINING_ATTEND.TRAINING_RELEASE_ID.eq(trainingReleaseId);
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
                    sortField[0] = TRAINING_ATTEND.ATTEND_DATE.asc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = TRAINING_ATTEND.ATTEND_DATE.desc();
                    sortField[1] = STUDENT.STUDENT_NUMBER.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
