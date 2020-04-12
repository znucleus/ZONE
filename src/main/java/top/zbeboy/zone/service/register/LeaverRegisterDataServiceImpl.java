package top.zbeboy.zone.service.register;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.LeaverRegisterDataDao;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("leaverRegisterDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterDataServiceImpl implements LeaverRegisterDataService, PaginationPlugin<SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private LeaverRegisterDataDao leaverRegisterDataDao;

    @Autowired
    LeaverRegisterDataServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByLeaverRegisterReleaseIdAndStudentId(String leaverRegisterReleaseId, int studentId) {
        return create.select()
                .from(LEAVER_REGISTER_DATA)
                .where(LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId)
                        .and(LEAVER_REGISTER_DATA.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(LEAVER_REGISTER_DATA)
                .leftJoin(STUDENT)
                .on(LEAVER_REGISTER_DATA.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return queryAllByPage(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public Result<Record> export(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(LEAVER_REGISTER_DATA)
                .leftJoin(STUDENT)
                .on(LEAVER_REGISTER_DATA.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return queryAll(selectOnConditionStep, paginationUtil, false);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(LEAVER_REGISTER_DATA)
                .leftJoin(STUDENT)
                .on(LEAVER_REGISTER_DATA.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, paginationUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(LeaverRegisterData leaverRegisterData) {
        leaverRegisterDataDao.insert(leaverRegisterData);
    }

    @Override
    public void deleteByLeaverRegisterReleaseIdAndStudentId(String leaverRegisterReleaseId, int studentId) {
        create.deleteFrom(LEAVER_REGISTER_DATA)
                .where(LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId)
                        .and(LEAVER_REGISTER_DATA.STUDENT_ID.eq(studentId)))
                .execute();
    }

    @Override
    public void deleteById(String id) {
        leaverRegisterDataDao.deleteById(id);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
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
        }
        return a;
    }

    @Override
    public Condition extraCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String leaverRegisterReleaseId = StringUtils.trim(search.getString("leaverRegisterReleaseId"));
            a = LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId);
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
