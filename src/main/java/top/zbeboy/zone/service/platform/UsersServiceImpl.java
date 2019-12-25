package top.zbeboy.zone.service.platform;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.daos.UsersDao;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.AuthoritiesRecord;
import top.zbeboy.zone.domain.tables.records.UsersRecord;
import top.zbeboy.zone.security.MyUserImpl;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.*;
import static org.jooq.impl.DSL.*;

@Service("usersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UsersServiceImpl implements UsersService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private UsersDao usersDao;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private RoleService roleService;

    @Autowired
    UsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Users findByUsername(String username) {
        return usersDao.findById(username);
    }

    @Override
    public UsersRecord findNormalByUsername(String username) {
        return create.selectFrom(USERS)
                .where(USERS.USERNAME.eq(username).and(USERS.VERIFY_MAILBOX.eq(BooleanUtil.toByte(true))).andExists(authoritiesService.existsAuthoritiesSelect()))
                .fetchOne();
    }

    @Override
    public UsersRecord findByUsernameUpper(String username) {
        return create.selectFrom(USERS)
                .where(USERS.USERNAME.upper().eq(username)).fetchOne();
    }

    @Override
    public Result<UsersRecord> findByUsernameNeOwn(String username, String own) {
        return create.selectFrom(USERS)
                .where(USERS.USERNAME.upper().eq(username).and(USERS.USERNAME.ne(own))).fetch();
    }

    @Override
    public Result<UsersRecord> findByIdCardNeOwn(String idCard, String own) {
        return create.selectFrom(USERS)
                .where(USERS.ID_CARD.eq(idCard).and(USERS.ID_CARD.ne(own))).fetch();
    }

    @Override
    public Result<UsersRecord> findByEmailNeOwn(String email, String own) {
        return create.selectFrom(USERS)
                .where(USERS.EMAIL.eq(email).and(USERS.EMAIL.ne(own))).fetch();
    }

    @Override
    public Result<UsersRecord> findByMobileNeOwn(String mobile, String own) {
        return create.selectFrom(USERS)
                .where(USERS.MOBILE.eq(mobile).and(USERS.MOBILE.ne(own))).fetch();
    }

    @Override
    public Users findByEmail(String email) {
        return usersDao.fetchOneByEmail(email);
    }

    @Override
    public Users findByMobile(String mobile) {
        return usersDao.fetchOneByMobile(mobile);
    }

    @Override
    public Result<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> findAllByPage(DataTablesUtil dataTablesUtil) {
        Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME));
        SelectOnConditionStep<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> selectConditionStep =
                create.select(USERS.REAL_NAME, USERS.USERNAME, USERS.EMAIL,USERS.MOBILE,USERS.ID_CARD,
                listAgg(ROLE.ROLE_NAME, " ").withinGroupOrderBy(ROLE.ROLE_NAME).as("roleName"),
                USERS_TYPE.USERS_TYPE_NAME, USERS.ENABLED, USERS.ACCOUNT_NON_LOCKED,USERS.LANG_KEY, USERS.JOIN_DATE)
                    .from(USERS)
                .join(USERS_TYPE)
                .on(USERS.USERS_TYPE_ID.eq(USERS_TYPE.USERS_TYPE_ID))
                .join(AUTHORITIES)
                .on(USERS.USERNAME.eq(AUTHORITIES.USERNAME))
                .join(ROLE)
                .on(ROLE.ROLE_EN_NAME.eq(AUTHORITIES.AUTHORITY));
        return queryAllByPage(selectConditionStep, dataTablesUtil, false,USERS.USERNAME);
    }

    @Override
    public int countAll() {
        return 0;
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return 0;
    }

    @Override
    public Result<UsersRecord> findByJoinDateAndVerifyMailbox(Date joinDate, Byte verifyMailbox) {
        return create.selectFrom(USERS)
                .where(USERS.JOIN_DATE.le(joinDate).and(USERS.VERIFY_MAILBOX.eq(verifyMailbox)))
                .fetch();
    }

    @Override
    public Users getUserFromSession() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = null;
        if (Objects.nonNull(principal) && principal instanceof MyUserImpl) {
            users = ((MyUserImpl) principal).getUsers();
        }
        return users;
    }

    @Override
    public List<String> getAuthoritiesFromSession() {
        List<String> authorities = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().forEachRemaining(i -> authorities.add(i.getAuthority()));
        return authorities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Users users) {
        usersDao.insert(users);
    }

    @Override
    public void update(Users users) {
        usersDao.update(users);
    }

    @Override
    public void updateUsername(String oldName, String newName) {
        create.update(USERS).set(USERS.USERNAME, newName).where(USERS.USERNAME.eq(oldName)).execute();
    }

    @Override
    public void unlockUsers() {
        create.update(USERS).set(USERS.ACCOUNT_NON_LOCKED, BooleanUtil.toByte(true))
                .where(USERS.ACCOUNT_NON_LOCKED.eq(BooleanUtil.toByte(false))).execute();
    }

    @Override
    public void delete(List<Users> users) {
        usersDao.delete(users);
    }

    public Result<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> queryAllByPage(SelectOnConditionStep<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> selectOnConditionStep,
                                         DataTablesUtil paginationUtil, boolean useExtraCondition, GroupField... groupFields) {
        Result<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> records;
        Condition a = useExtraCondition(paginationUtil, useExtraCondition);
        if (Objects.isNull(a)) {
            groupBy(selectOnConditionStep,groupFields);
            sortCondition(selectOnConditionStep, paginationUtil);
            pagination(selectOnConditionStep, paginationUtil);
            records = selectOnConditionStep.fetch();
        } else {
            SelectConditionStep<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> selectConditionStep = selectOnConditionStep.where(a);
            groupBy(selectConditionStep,groupFields);
            sortCondition(selectConditionStep, paginationUtil);
            pagination(selectConditionStep, paginationUtil);
            records = selectConditionStep.fetch();
        }
        return records;
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        return null;
    }

    public void sortCondition(SelectConnectByStep<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> step, DataTablesUtil paginationUtil) {

    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Users users = getUserFromSession();
        Condition a = USERS.USERNAME.ne(users.getUsername());
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String audited = StringUtils.trim(search.getString("audited"));
            if(StringUtils.isBlank(audited)){
                audited = "1";
            }
                int auditedInt = NumberUtils.toInt(audited);
                // 已审核
                if (auditedInt == 1) {
                    if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                        Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                                .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME).and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_SYSTEM.name())));
                        a = a.andExists(select);
                    } else {
                        Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                                .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME).and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_SYSTEM.name())).and(AUTHORITIES.AUTHORITY.ne(Workbook.authorities.ROLE_ACTUATOR.name())));
                        a = a.andExists(select);
                    }

                } else if (auditedInt == 2) {
                    // 未审核
                    Select<AuthoritiesRecord> select = create.selectFrom(AUTHORITIES)
                            .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME));
                    a = a.andNotExists(select);
                }
        }

        return a;
    }

    private void groupBy(SelectConnectByStep<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> step, GroupField... groupFields) {

    }

    public void pagination(SelectConnectByStep<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> step, DataTablesUtil paginationUtil) {

    }
}
