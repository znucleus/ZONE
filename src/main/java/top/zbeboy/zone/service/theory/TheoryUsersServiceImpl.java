package top.zbeboy.zone.service.theory;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.daos.TheoryUsersDao;
import top.zbeboy.zbase.domain.tables.pojos.TheoryUsers;
import top.zbeboy.zbase.domain.tables.records.AuthoritiesRecord;
import top.zbeboy.zbase.domain.tables.records.TheoryUsersRecord;
import top.zbeboy.zbase.tools.service.util.SQLQueryUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.service.plugin.PaginationPlugin;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryUsersServiceImpl implements TheoryUsersService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private TheoryUsersDao theoryUsersDao;

    @Autowired
    TheoryUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TheoryUsers findById(String id) {
        return theoryUsersDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.THEORY_USERS, key = "#theoryReleaseId + '_' + #studentId")
    @Override
    public Optional<TheoryUsersRecord> findByTheoryReleaseIdAndStudentId(String theoryReleaseId, int studentId) {
        return create.selectFrom(THEORY_USERS)
                .where(THEORY_USERS.THEORY_RELEASE_ID.eq(theoryReleaseId)
                        .and(THEORY_USERS.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public List<TheoryUsers> findByTheoryReleaseId(String theoryReleaseId) {
        return theoryUsersDao.fetchByTheoryReleaseId(theoryReleaseId);
    }

    @Override
    public Result<Record> findStudentNotExistsUsers(String theoryReleaseId, int organizeId) {
        Select<TheoryUsersRecord> select = create.selectFrom(THEORY_USERS)
                .where(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID).and(THEORY_USERS.THEORY_RELEASE_ID.eq(theoryReleaseId)));
        Select<AuthoritiesRecord> existsAuthoritiesSelect = create.selectFrom(AUTHORITIES)
                .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME));
        return create.select()
                .from(STUDENT)
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(STUDENT.ORGANIZE_ID.eq(organizeId).andNotExists(select)
                        .and(USERS.VERIFY_MAILBOX.eq(BooleanUtil.toByte(true))).andExists(existsAuthoritiesSelect))
                .fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(THEORY_USERS)
                        .leftJoin(STUDENT)
                        .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                        .leftJoin(USERS)
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME));
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
                        .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return queryAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_USERS)
                .leftJoin(STUDENT)
                .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(THEORY_USERS)
                .leftJoin(STUDENT)
                .on(THEORY_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countByTheoryReleaseId(String theoryReleaseId) {
        Record1<Integer> count = create.selectCount()
                .from(THEORY_USERS)
                .where(THEORY_USERS.THEORY_RELEASE_ID.eq(theoryReleaseId))
                .fetchOne();
        return count.value1();
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_USERS, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<TheoryUsers> theoryUsers) {
        theoryUsersDao.insert(theoryUsers);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_USERS, key = "#theoryUsers.theoryReleaseId + '_' + #theoryUsers.studentId")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TheoryUsers theoryUsers) {
        theoryUsersDao.insert(theoryUsers);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_USERS, key = "#theoryUsers.theoryReleaseId + '_' + #theoryUsers.studentId")
    @Override
    public void update(TheoryUsers theoryUsers) {
        theoryUsersDao.update(theoryUsers);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_USERS, allEntries = true)
    @Override
    public void deleteById(List<String> ids) {
        theoryUsersDao.deleteById(ids);
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
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.STUDENT_NUMBER.asc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.STUDENT_NUMBER.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.USERNAME.asc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.USERNAME.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("mobile", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.MOBILE.asc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.MOBILE.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("email", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.EMAIL.asc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = USERS.EMAIL.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("sex", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = STUDENT.SEX.asc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = STUDENT.SEX.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("remark", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = THEORY_USERS.REMARK.asc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.asc();
                } else {
                    sortField[0] = THEORY_USERS.REMARK.desc();
                    sortField[1] = THEORY_USERS.THEORY_USERS_ID.desc();
                }
            }

            if (StringUtils.equals("createDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = THEORY_USERS.CREATE_DATE.asc();
                } else {
                    sortField[0] = THEORY_USERS.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
