package top.zbeboy.zone.service.theory;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.TheoryAttendUsersDao;
import top.zbeboy.zbase.domain.tables.pojos.TheoryAttendUsers;
import top.zbeboy.zbase.domain.tables.records.TheoryAttendUsersRecord;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryAttendUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryAttendUsersServiceImpl implements TheoryAttendUsersService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private TheoryAttendUsersDao theoryAttendUsersDao;

    @Autowired
    TheoryAttendUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TheoryAttendUsers findById(String id) {
        return theoryAttendUsersDao.findById(id);
    }

    @Override
    public Result<Record> findStudentNotExistsUsers(String theoryReleaseId, String theoryAttendId) {
        Select<TheoryAttendUsersRecord> select = create.selectFrom(THEORY_ATTEND_USERS)
                .where(THEORY_USERS.THEORY_USERS_ID.eq(THEORY_ATTEND_USERS.THEORY_USERS_ID).and(THEORY_ATTEND_USERS.THEORY_ATTEND_ID.eq(theoryAttendId)));
        return create.select()
                .from(THEORY_USERS)
                .where(THEORY_USERS.THEORY_RELEASE_ID.eq(theoryReleaseId).andNotExists(select))
                .fetch();
    }

    @Override
    public Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> selectOnConditionStep =
                create.select(THEORY_ATTEND_USERS.ATTEND_USERS_ID, THEORY_ATTEND_USERS.OPERATE_USER,
                        THEORY_ATTEND_USERS.OPERATE, THEORY_ATTEND_USERS.REMARK,
                        USERS.REAL_NAME, STUDENT.STUDENT_NUMBER, USERS.MOBILE, USERS.EMAIL, STUDENT.SEX,
                        ORGANIZE.ORGANIZE_NAME, THEORY_USERS.THEORY_RELEASE_ID)
                        .from(THEORY_ATTEND_USERS)
                        .leftJoin(THEORY_USERS)
                        .on(THEORY_ATTEND_USERS.THEORY_USERS_ID.eq(THEORY_USERS.THEORY_USERS_ID))
                        .leftJoin(STUDENT)
                        .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .leftJoin(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(ORGANIZE)
                        .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> export(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> selectOnConditionStep =
                create.select(THEORY_ATTEND_USERS.ATTEND_USERS_ID, THEORY_ATTEND_USERS.OPERATE_USER,
                        THEORY_ATTEND_USERS.OPERATE, THEORY_ATTEND_USERS.REMARK,
                        USERS.REAL_NAME, STUDENT.STUDENT_NUMBER, USERS.MOBILE, USERS.EMAIL, STUDENT.SEX,
                        ORGANIZE.ORGANIZE_NAME, THEORY_USERS.THEORY_RELEASE_ID)
                        .from(THEORY_ATTEND_USERS)
                        .leftJoin(THEORY_USERS)
                        .on(THEORY_ATTEND_USERS.THEORY_USERS_ID.eq(THEORY_USERS.THEORY_USERS_ID))
                        .leftJoin(STUDENT)
                        .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .leftJoin(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                        .leftJoin(ORGANIZE)
                        .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID));
        return queryAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_ATTEND_USERS)
                .leftJoin(THEORY_USERS)
                .on(THEORY_ATTEND_USERS.THEORY_USERS_ID.eq(THEORY_USERS.THEORY_USERS_ID))
                .leftJoin(STUDENT)
                .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_ATTEND_USERS)
                .leftJoin(THEORY_USERS)
                .on(THEORY_ATTEND_USERS.THEORY_USERS_ID.eq(THEORY_USERS.THEORY_USERS_ID))
                .leftJoin(STUDENT)
                .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ORGANIZE)
                .on(STUDENT.ORGANIZE_ID.eq(ORGANIZE.ORGANIZE_ID));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<TheoryAttendUsers> theoryAttendUsers) {
        theoryAttendUsersDao.insert(theoryAttendUsers);
    }

    @Override
    public void update(TheoryAttendUsers theoryAttendUsers) {
        theoryAttendUsersDao.update(theoryAttendUsers);
    }

    @Override
    public void updateOperateByTheoryAttendId(String theoryAttendId, Byte operate) {
        create.update(THEORY_ATTEND_USERS)
                .set(THEORY_ATTEND_USERS.OPERATE, operate)
                .where(THEORY_ATTEND_USERS.THEORY_ATTEND_ID.eq(theoryAttendId))
                .execute();
    }

    @Override
    public void deleteById(List<String> ids) {
        theoryAttendUsersDao.deleteById(ids);
    }

    private Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> queryAllByPage(SelectOnConditionStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> selectOnConditionStep, DataTablesUtil paginationUtil, boolean useExtraCondition) {
        Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            sortCondition(selectOnConditionStep, paginationUtil);
            pagination(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> selectConditionStep = selectOnConditionStep.where(a);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    private Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> queryAll(SelectOnConditionStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> selectOnConditionStep, DataTablesUtil paginationUtil, boolean useExtraCondition) {
        Result<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            sortCondition(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> selectConditionStep = selectOnConditionStep.where(a);
            sortCondition(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String operate = StringUtils.trim(search.getString("operate"));
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

            if (StringUtils.isNotBlank(operate)) {
                Byte b = NumberUtils.toByte(operate);
                if (Objects.isNull(a)) {
                    a = THEORY_ATTEND_USERS.OPERATE.eq(b);
                } else {
                    a = a.and(THEORY_ATTEND_USERS.OPERATE.eq(b));
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
            String theoryAttendId = StringUtils.trim(search.getString("theoryAttendId"));
            if (StringUtils.isNotBlank(theoryAttendId)) {
                a = THEORY_ATTEND_USERS.THEORY_ATTEND_ID.eq(theoryAttendId);
            }
        }
        return a;
    }

    private void sortCondition(SelectConnectByStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("realName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.desc();
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

            if (StringUtils.equals("organizeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.asc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.asc();
                } else {
                    sortField[0] = ORGANIZE.ORGANIZE_NAME.desc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("mobile", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.MOBILE.asc();
                } else {
                    sortField[0] = USERS.MOBILE.desc();
                }
            }

            if (StringUtils.equals("email", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USERS.EMAIL.asc();
                } else {
                    sortField[0] = USERS.EMAIL.desc();
                }
            }

            if (StringUtils.equals("sex", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.SEX.asc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.SEX.desc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("operate", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = THEORY_ATTEND_USERS.OPERATE.asc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.asc();
                } else {
                    sortField[0] = THEORY_ATTEND_USERS.OPERATE.desc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("remark", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = THEORY_ATTEND_USERS.REMARK.asc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.asc();
                } else {
                    sortField[0] = THEORY_ATTEND_USERS.REMARK.desc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("operateUser", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = THEORY_ATTEND_USERS.OPERATE_USER.asc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.asc();
                } else {
                    sortField[0] = THEORY_ATTEND_USERS.OPERATE_USER.desc();
                    sortField[1] = THEORY_ATTEND_USERS.ATTEND_USERS_ID.desc();
                }
            }
        }
        sortFinish(step, sortField);
    }

    private void sortFinish(SelectConnectByStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> step, SortField... sortField) {
        if (Objects.nonNull(sortField)) {
            step.orderBy(sortField);
        }
    }

    private void pagination(SelectConnectByStep<Record11<String, String, Byte, String, String, String, String, String, String, String, String>> step, DataTablesUtil paginationUtil) {
        int start = paginationUtil.getStart();
        int length = paginationUtil.getLength();

        step.limit(start, length);
    }
}
