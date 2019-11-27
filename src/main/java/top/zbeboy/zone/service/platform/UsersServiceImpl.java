package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.UsersDao;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.UsersRecord;
import top.zbeboy.zone.security.MyUserImpl;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.web.util.BooleanUtil;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.USERS;

@Service("usersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UsersServiceImpl implements UsersService {

    private final DSLContext create;

    @Resource
    private UsersDao usersDao;

    @Resource
    private AuthoritiesService authoritiesService;

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
}
