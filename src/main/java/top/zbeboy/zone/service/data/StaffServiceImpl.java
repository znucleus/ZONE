package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.StaffDao;
import top.zbeboy.zone.domain.tables.pojos.Staff;
import top.zbeboy.zone.domain.tables.records.StaffRecord;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("staffService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class StaffServiceImpl implements StaffService {

    private final DSLContext create;

    @Resource
    private StaffDao staffDao;

    @Autowired
    StaffServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<StaffRecord> findByUsername(String username) {
        return create.selectFrom(STAFF)
                .where(STAFF.USERNAME.eq(username))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.STAFF, key = "#username")
    @Override
    public Optional<Record> findByUsernameRelation(String username) {
        return create.select()
                .from(STAFF)
                .join(DEPARTMENT)
                .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(STAFF.USERNAME.eq(username))
                .fetchOptional();
    }

    @Override
    public Result<Record> findAdmin(String authority, int collegeId) {
        return create.select()
                .from(STAFF)
                .join(USERS)
                .on(STAFF.USERNAME.eq(USERS.USERNAME))
                .join(AUTHORITIES)
                .on(STAFF.USERNAME.eq(AUTHORITIES.USERNAME))
                .join(DEPARTMENT)
                .on(STAFF.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .where(AUTHORITIES.AUTHORITY.eq(authority).and(DEPARTMENT.COLLEGE_ID.eq(collegeId)))
                .fetch();
    }

    @Override
    public Staff findByStaffNumber(String staffNumber) {
        return staffDao.fetchOneByStaffNumber(staffNumber);
    }

    @Override
    public Result<StaffRecord> findByStaffNumberNeUsername(String staffNumber, String username) {
        return create.selectFrom(STAFF)
                .where(STAFF.STAFF_NUMBER.eq(staffNumber).and(STAFF.USERNAME.ne(username))).fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(Staff staff) {
        staffDao.insert(staff);
    }

    @Override
    public void saveWithUsers(StaffAddVo staffAddVo) {
        create.transaction(configuration -> {
            DSL.using(configuration)
                    .insertInto(USERS, USERS.USERNAME, USERS.PASSWORD, USERS.ENABLED,
                            USERS.ACCOUNT_NON_EXPIRED, USERS.CREDENTIALS_NON_EXPIRED,
                            USERS.ACCOUNT_NON_LOCKED, USERS.AGREE_PROTOCOL, USERS.USERS_TYPE_ID,
                            USERS.REAL_NAME, USERS.EMAIL, USERS.MOBILE, USERS.AVATAR, USERS.MAILBOX_VERIFY_CODE,
                            USERS.MAILBOX_VERIFY_VALID, USERS.LANG_KEY, USERS.JOIN_DATE)
                    .values(staffAddVo.getUsername(), BCryptUtil.bCryptPassword(staffAddVo.getPassword()),
                            BooleanUtil.toByte(true), BooleanUtil.toByte(true), BooleanUtil.toByte(true),
                            BooleanUtil.toByte(true), staffAddVo.getAgreeProtocol(), staffAddVo.getUsersTypeId(), staffAddVo.getRealName(),
                            staffAddVo.getEmail(), staffAddVo.getMobile(), staffAddVo.getAvatar(), staffAddVo.getMailboxVerifyCode(),
                            staffAddVo.getMailboxVerifyValid(), staffAddVo.getLangKey(), staffAddVo.getJoinDate())
                    .execute();
            DSL.using(configuration)
                    .insertInto(STAFF, STAFF.USERNAME, STAFF.STAFF_NUMBER, STAFF.DEPARTMENT_ID)
                    .values(staffAddVo.getUsername(), staffAddVo.getStaffNumber(), staffAddVo.getDepartmentId())
                    .execute();
        });
    }

    @CacheEvict(cacheNames = CacheBook.STAFF, key = "#staff.username")
    @Override
    public void update(Staff staff) {
        staffDao.update(staff);
    }
}
