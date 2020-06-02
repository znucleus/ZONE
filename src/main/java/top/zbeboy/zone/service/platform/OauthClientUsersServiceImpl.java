package top.zbeboy.zone.service.platform;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.OauthClientUsersDao;
import top.zbeboy.zone.domain.tables.pojos.OauthClientUsers;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.OauthClientUsersRecord;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("oauthClientUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OauthClientUsersServiceImpl implements OauthClientUsersService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private OauthClientUsersDao oauthClientUsersDao;

    @Autowired
    OauthClientUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<OauthClientUsersRecord> findById(String id) {
        return create.selectFrom(OAUTH_CLIENT_USERS)
                .where(OAUTH_CLIENT_USERS.CLIENT_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(OAUTH_CLIENT_USERS)
                .join(OAUTH_CLIENT_DETAILS)
                .on(OAUTH_CLIENT_USERS.CLIENT_ID.eq(OAUTH_CLIENT_DETAILS.CLIENT_ID))
                .where(OAUTH_CLIENT_USERS.CLIENT_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Optional<Record> findByIdAndUsernameRelation(String id, String username) {
        return create.select()
                .from(OAUTH_CLIENT_USERS)
                .join(OAUTH_CLIENT_DETAILS)
                .on(OAUTH_CLIENT_USERS.CLIENT_ID.eq(OAUTH_CLIENT_DETAILS.CLIENT_ID))
                .where(OAUTH_CLIENT_USERS.USERNAME.eq(username).and(OAUTH_CLIENT_USERS.CLIENT_ID.eq(id)))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record> selectOnConditionStep =
                create.select()
                        .from(OAUTH_CLIENT_USERS)
                        .join(OAUTH_CLIENT_DETAILS)
                        .on(OAUTH_CLIENT_USERS.CLIENT_ID.eq(OAUTH_CLIENT_DETAILS.CLIENT_ID))
                        .leftJoin(USERS)
                        .on(OAUTH_CLIENT_USERS.USERNAME.eq(USERS.USERNAME));
        return queryAllByPage(selectOnConditionStep, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(OAUTH_CLIENT_USERS)
                .join(OAUTH_CLIENT_DETAILS)
                .on(OAUTH_CLIENT_USERS.CLIENT_ID.eq(OAUTH_CLIENT_DETAILS.CLIENT_ID))
                .leftJoin(USERS)
                .on(OAUTH_CLIENT_USERS.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        SelectOnConditionStep<Record1<Integer>> selectOnConditionStep = create.selectCount()
                .from(OAUTH_CLIENT_USERS)
                .join(OAUTH_CLIENT_DETAILS)
                .on(OAUTH_CLIENT_USERS.CLIENT_ID.eq(OAUTH_CLIENT_DETAILS.CLIENT_ID))
                .leftJoin(USERS)
                .on(OAUTH_CLIENT_USERS.USERNAME.eq(USERS.USERNAME));
        return countAll(selectOnConditionStep, dataTablesUtil, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(OauthClientUsers oauthClientUsers) {
        oauthClientUsersDao.insert(oauthClientUsers);
    }

    @Override
    public void update(OauthClientUsers oauthClientUsers) {
        oauthClientUsersDao.update(oauthClientUsers);
    }

    @Override
    public void delete(OauthClientUsers oauthClientUsers) {
        oauthClientUsersDao.delete(oauthClientUsers);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String username = StringUtils.trim(search.getString("username"));
            String realName = StringUtils.trim(search.getString("realName"));
            String appName = StringUtils.trim(search.getString("appName"));
            if (StringUtils.isNotBlank(username)) {
                a = OAUTH_CLIENT_USERS.USERNAME.like(SQLQueryUtil.likeAllParam(username));
            }

            if (StringUtils.isNotBlank(realName)) {
                if (Objects.isNull(a)) {
                    a = USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName));
                } else {
                    a = a.and(USERS.REAL_NAME.like(SQLQueryUtil.likeAllParam(realName)));
                }
            }

            if (StringUtils.isNotBlank(appName)) {
                if (Objects.isNull(a)) {
                    a = OAUTH_CLIENT_USERS.APP_NAME.like(SQLQueryUtil.likeAllParam(appName));
                } else {
                    a = a.and(OAUTH_CLIENT_USERS.APP_NAME.like(SQLQueryUtil.likeAllParam(appName)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;

        if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = SessionUtil.getUserFromSession();
            a = OAUTH_CLIENT_USERS.USERNAME.eq(users.getUsername());
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
            if (StringUtils.equals("appName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_USERS.APP_NAME.asc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_USERS.APP_NAME.desc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("username", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_USERS.USERNAME.asc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_USERS.USERNAME.desc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("realName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = USERS.REAL_NAME.asc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = USERS.REAL_NAME.desc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("clientId", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("secret", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_USERS.SECRET.asc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_USERS.SECRET.desc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("webServerRedirectUri", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_DETAILS.WEB_SERVER_REDIRECT_URI.asc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_DETAILS.WEB_SERVER_REDIRECT_URI.desc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("remark", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_USERS.REMARK.asc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_USERS.REMARK.desc();
                    sortField[1] = OAUTH_CLIENT_USERS.CLIENT_ID.desc();
                }
            }

            if (StringUtils.equals("createDate", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = OAUTH_CLIENT_USERS.CREATE_DATE.asc();
                } else {
                    sortField[0] = OAUTH_CLIENT_USERS.CREATE_DATE.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
