package top.zbeboy.zone.service.training;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingUsersDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingUsers;
import top.zbeboy.zone.domain.tables.records.TrainingUsersRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("trainingUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingUsersServiceImpl implements TrainingUsersService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private TrainingUsersDao trainingUsersDao;

    @Autowired
    TrainingUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TrainingUsers findById(String id) {
        return trainingUsersDao.findById(id);
    }

    @Override
    public Optional<TrainingUsersRecord> findByTrainingReleaseIdAndStudentId(String trainingReleaseId, int studentId) {
        return create.selectFrom(TRAINING_USERS)
                .where(TRAINING_USERS.TRAINING_RELEASE_ID.eq(trainingReleaseId)
                .and(TRAINING_USERS.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(TRAINING_USERS)
                        .join(STUDENT)
                        .on(TRAINING_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .join(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(TRAINING_USERS)
                .join(STUDENT)
                .on(TRAINING_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .join(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(TRAINING_USERS)
                .join(STUDENT)
                .on(TRAINING_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .join(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<TrainingUsers> trainingUsers) {
        trainingUsersDao.insert(trainingUsers);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingUsers trainingUsers) {
        trainingUsersDao.insert(trainingUsers);
    }

    @Override
    public void update(TrainingUsers trainingUsers) {
        trainingUsersDao.update(trainingUsers);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String username = StringUtils.trim(search.getString("username"));
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

            if (StringUtils.isNotBlank(username)) {
                if (Objects.isNull(a)) {
                    a = STUDENT.USERNAME.like(SQLQueryUtil.likeAllParam(username));
                } else {
                    a = a.and(STUDENT.USERNAME.like(SQLQueryUtil.likeAllParam(username)));
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
            String trainingReleaseId = StringUtils.trim(search.getString("trainingReleaseId"));
            if (StringUtils.isNotBlank(trainingReleaseId)) {
                a = TRAINING_USERS.TRAINING_RELEASE_ID.eq(trainingReleaseId);
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
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.STUDENT_NUMBER.asc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.STUDENT_NUMBER.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.USERNAME.asc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.USERNAME.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("mobile", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.MOBILE.asc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.MOBILE.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("email", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.EMAIL.asc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.EMAIL.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("sex", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.SEX.asc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.SEX.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("remark", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = TRAINING_USERS.REMARK.asc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.asc();
                } else {
                    sortField[0] = TRAINING_USERS.REMARK.desc();
                    sortField[1] = TRAINING_USERS.TRAINING_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("createDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = TRAINING_USERS.CREATE_DATE.asc();
                } else {
                    sortField[0] = TRAINING_USERS.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
