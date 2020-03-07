package top.zbeboy.zone.service.notify;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.records.SystemNotifyRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.Objects;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.SYSTEM_NOTIFY;
import static top.zbeboy.zone.domain.Tables.USERS;

@Service("systemNotifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemNotifyServiceImpl implements SystemNotifyService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Autowired
    SystemNotifyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<SystemNotifyRecord> findByEffective() {
        return create.selectFrom(SYSTEM_NOTIFY)
                .where(SYSTEM_NOTIFY.EXPIRE_DATE.ge(now())
                        .and(SYSTEM_NOTIFY.VALID_DATE.le(now()))
                        .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(SYSTEM_NOTIFY.VALID_DATE))).fetch();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(SYSTEM_NOTIFY)
                        .leftJoin(USERS)
                        .on(SYSTEM_NOTIFY.SEND_USER.eq(USERS.USERNAME));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll() {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(SYSTEM_NOTIFY)
                .leftJoin(USERS)
                .on(SYSTEM_NOTIFY.SEND_USER.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(SYSTEM_NOTIFY)
                .leftJoin(USERS)
                .on(SYSTEM_NOTIFY.SEND_USER.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String notifyTitle = StringUtils.trim(search.getString("notifyTitle"));
            String notifyContent = StringUtils.trim(search.getString("notifyContent"));
            String realName = StringUtils.trim(search.getString("realName"));
            String time = StringUtils.trim(search.getString("time"));
            if (StringUtils.isNotBlank(notifyTitle)) {
                a = SYSTEM_NOTIFY.NOTIFY_TITLE.like(SQLQueryUtil.likeAllParam(notifyTitle));
            }

            if (StringUtils.isNotBlank(notifyContent)) {
                if (Objects.isNull(a)) {
                    a = SYSTEM_NOTIFY.NOTIFY_CONTENT.like(SQLQueryUtil.likeAllParam(notifyContent));
                } else {
                    a = a.and(SYSTEM_NOTIFY.NOTIFY_CONTENT.like(SQLQueryUtil.likeAllParam(notifyContent)));
                }
            }

            if (StringUtils.isNotBlank(realName)) {
                if (Objects.isNull(a)) {
                    a = USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName));
                } else {
                    a = a.and(USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName)));
                }
            }

            if (StringUtils.isNotBlank(time)) {
                int timeInt = NumberUtils.toInt(time);
                if (timeInt == 1) {
                    if (Objects.isNull(a)) {
                        a = SYSTEM_NOTIFY.VALID_DATE.le(now())
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(SYSTEM_NOTIFY.VALID_DATE))
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(now()));
                    } else {
                        a = a.and(SYSTEM_NOTIFY.VALID_DATE.le(now())
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(SYSTEM_NOTIFY.VALID_DATE))
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(now())));
                    }
                } else if (timeInt == 2) {
                    if (Objects.isNull(a)) {
                        a = SYSTEM_NOTIFY.VALID_DATE.gt(now())
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(SYSTEM_NOTIFY.VALID_DATE))
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(now()));
                    } else {
                        a = a.and(SYSTEM_NOTIFY.VALID_DATE.gt(now())
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(SYSTEM_NOTIFY.VALID_DATE))
                                .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(now())));
                    }
                } else if (timeInt == 3) {
                    if (Objects.isNull(a)) {
                        a = SYSTEM_NOTIFY.EXPIRE_DATE.le(now());
                    } else {
                        a = a.and(SYSTEM_NOTIFY.EXPIRE_DATE.le(now()));
                    }
                }

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
            if (StringUtils.equals("notifyTitle", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_NOTIFY.NOTIFY_TITLE.asc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = SYSTEM_NOTIFY.NOTIFY_TITLE.desc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("notifyContent", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_NOTIFY.NOTIFY_CONTENT.asc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = SYSTEM_NOTIFY.NOTIFY_CONTENT.desc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("notifyType", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_NOTIFY.NOTIFY_TYPE.asc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = SYSTEM_NOTIFY.NOTIFY_TYPE.desc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("validDateStr", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_NOTIFY.VALID_DATE.asc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = SYSTEM_NOTIFY.VALID_DATE.desc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("expireDateStr", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = SYSTEM_NOTIFY.EXPIRE_DATE.asc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = SYSTEM_NOTIFY.EXPIRE_DATE.desc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("realName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }

            if (StringUtils.equals("createDateStr", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = SYSTEM_NOTIFY.CREATE_DATE.asc();
                } else {
                    sortField[0] = SYSTEM_NOTIFY.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
