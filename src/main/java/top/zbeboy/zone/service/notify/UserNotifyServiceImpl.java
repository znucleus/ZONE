package top.zbeboy.zone.service.notify;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.UserNotifyDao;
import top.zbeboy.zone.domain.tables.pojos.UserNotify;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.bean.notify.UserNotifyBean;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.USERS;
import static top.zbeboy.zone.domain.Tables.USER_NOTIFY;

@Service("userNotifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserNotifyServiceImpl implements UserNotifyService, PaginationPlugin<UserNotifyBean, SimplePaginationUtil> {

    private final DSLContext create;

    @Resource
    private UserNotifyDao userNotifyDao;

    @Autowired
    UserNotifyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByIdAndAcceptUserRelation(String id, String acceptUser) {
        return create.select()
                .from(USER_NOTIFY)
                .leftJoin(USERS)
                .on(USER_NOTIFY.SEND_USER.eq(USERS.USERNAME))
                .where(USER_NOTIFY.USER_NOTIFY_ID.eq(id).and(USER_NOTIFY.ACCEPT_USER.eq(acceptUser))).fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(SimplePaginationUtil paginationUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep = create.select()
                .from(USER_NOTIFY)
                .leftJoin(USERS)
                .on(USER_NOTIFY.SEND_USER.eq(USERS.USERNAME));
        return queryAllByPage(selectOnConditionStep, paginationUtil);
    }

    @Override
    public int countAll(SimplePaginationUtil paginationUtil) {
        return countAll(create, USER_NOTIFY, paginationUtil);
    }

    @Override
    public int countByAcceptUserAndIsSee(String acceptUser, Byte isSee) {
        Record1<Integer> count = create.selectCount()
                .from(USER_NOTIFY)
                .where(USER_NOTIFY.ACCEPT_USER.eq(acceptUser).and(USER_NOTIFY.IS_SEE.eq(isSee)))
                .fetchOne();
        return count.value1();
    }

    @Override
    public void update(UserNotify userNotify) {
        userNotifyDao.update(userNotify);
    }

    @Override
    public Condition searchCondition(SimplePaginationUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String notifyTitle = StringUtils.trim(search.getString("notifyTitle"));
            String acceptUser = StringUtils.trim(search.getString("acceptUser"));
            String isSee = StringUtils.trim(search.getString("isSee"));
            if (StringUtils.isNotBlank(notifyTitle)) {
                a = USER_NOTIFY.NOTIFY_TITLE.like(SQLQueryUtil.likeAllParam(notifyTitle));
            }

            if (StringUtils.isNotBlank(acceptUser)) {
                if (Objects.isNull(a)) {
                    a = USER_NOTIFY.ACCEPT_USER.eq(acceptUser);
                } else {
                    a = a.and(USER_NOTIFY.ACCEPT_USER.eq(acceptUser));
                }
            }

            if (StringUtils.isNotBlank(isSee)) {
                Byte isSeeParam = NumberUtils.toByte(isSee);
                if (Objects.isNull(a)) {
                    a = USER_NOTIFY.IS_SEE.eq(isSeeParam);
                } else {
                    a = a.and(USER_NOTIFY.IS_SEE.eq(isSeeParam));
                }
            }
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
            if (StringUtils.equals("createDate", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = USER_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = USER_NOTIFY.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
