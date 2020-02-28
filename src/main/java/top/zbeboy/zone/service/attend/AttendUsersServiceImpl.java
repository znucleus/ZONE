package top.zbeboy.zone.service.attend;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendUsersDao;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendDataRecord;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.web.util.BooleanUtil;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.*;

@Service("attendUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendUsersServiceImpl implements AttendUsersService {

    private final DSLContext create;

    @Resource
    private AttendUsersDao attendUsersDao;

    @Resource
    private AuthoritiesService authoritiesService;

    @Autowired
    AttendUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public AttendUsers findById(String id) {
        return attendUsersDao.findById(id);
    }

    @Override
    public Result<Record> findByAttendReleaseIdRelation(String attendReleaseId) {
        return create.select()
                .from(ATTEND_USERS)
                .leftJoin(STUDENT)
                .on(ATTEND_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId))
                .orderBy(STUDENT.STUDENT_NUMBER.asc())
                .fetch();
    }

    @Override
    public Result<Record12<String, String, Timestamp, String, Integer, String, String, String, String, Byte, Timestamp, String>> findByAttendReleaseIdAndAttendReleaseSubId(String attendReleaseId, int attendReleaseSubId) {
        return create.select(ATTEND_USERS.ATTEND_USERS_ID, ATTEND_USERS.ATTEND_RELEASE_ID,
                ATTEND_USERS.CREATE_DATE, ATTEND_USERS.REMARK, ATTEND_USERS.STUDENT_ID,
                STUDENT.STUDENT_NUMBER, USERS.REAL_NAME, ATTEND_DATA.LOCATION, ATTEND_DATA.ADDRESS, ATTEND_DATA.DEVICE_SAME,
                ATTEND_DATA.ATTEND_DATE, ATTEND_DATA.ATTEND_REMARK)
                .from(ATTEND_USERS)
                .leftJoin(STUDENT)
                .on(ATTEND_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ATTEND_DATA)
                .on(ATTEND_USERS.ATTEND_USERS_ID.eq(ATTEND_DATA.ATTEND_USERS_ID).and(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)))
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId))
                .orderBy(STUDENT.STUDENT_NUMBER.asc())
                .fetch();
    }

    @Override
    public Optional<AttendUsersRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId) {
        return create.selectFrom(ATTEND_USERS)
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).and(ATTEND_USERS.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public Result<Record> findHasAttendedStudent(String attendReleaseId, int attendReleaseSubId) {
        return create.select()
                .from(ATTEND_USERS)
                .leftJoin(STUDENT)
                .on(ATTEND_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .join(ATTEND_DATA)
                .on(ATTEND_USERS.ATTEND_USERS_ID.eq(ATTEND_DATA.ATTEND_USERS_ID))
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).and(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)))
                .orderBy(STUDENT.STUDENT_NUMBER.asc())
                .fetch();
    }

    @Override
    public Result<Record> findNotAttendedStudent(String attendReleaseId, int attendReleaseSubId) {
        Select<AttendDataRecord> select = create.selectFrom(ATTEND_DATA)
                .where(ATTEND_DATA.ATTEND_USERS_ID.eq(ATTEND_USERS.ATTEND_USERS_ID).and(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)));
        return create.select()
                .from(ATTEND_USERS)
                .leftJoin(STUDENT)
                .on(ATTEND_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).andNotExists(select))
                .orderBy(STUDENT.STUDENT_NUMBER.asc())
                .fetch();
    }

    @Override
    public Result<Record> findStudentNotExistsAttendUsers(String attendReleaseId, int organizeId) {
        Select<AttendUsersRecord> select = create.selectFrom(ATTEND_USERS)
                .where(ATTEND_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID).and(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId)));
        return create.select()
                .from(STUDENT)
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .where(STUDENT.ORGANIZE_ID.eq(organizeId).andNotExists(select)
                        .and(USERS.VERIFY_MAILBOX.eq(BooleanUtil.toByte(true))).andExists(authoritiesService.existsAuthoritiesSelect()))
                .fetch();
    }

    @Override
    public Result<Record> findFutureAttendByStudentId(int studentId) {
        return create.select()
                .from(ATTEND_USERS)
                .leftJoin(STUDENT)
                .on(ATTEND_USERS.STUDENT_ID.eq(STUDENT.STUDENT_ID))
                .leftJoin(USERS)
                .on(STUDENT.USERNAME.eq(USERS.USERNAME))
                .leftJoin(ATTEND_RELEASE_SUB)
                .on(ATTEND_USERS.ATTEND_RELEASE_ID.eq(ATTEND_RELEASE_SUB.ATTEND_RELEASE_ID))
                .where(ATTEND_USERS.STUDENT_ID.eq(studentId)
                        .and(ATTEND_RELEASE_SUB.ATTEND_END_TIME.gt(ATTEND_RELEASE_SUB.ATTEND_START_TIME))
                        .and(ATTEND_RELEASE_SUB.ATTEND_START_TIME.ge(now())))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AttendUsers attendUsers) {
        attendUsersDao.insert(attendUsers);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<AttendUsers> attendUsers) {
        attendUsersDao.insert(attendUsers);
    }

    @Override
    public void deleteById(String id) {
        attendUsersDao.deleteById(id);
    }

    @Override
    public void update(AttendUsers attendUsers) {
        attendUsersDao.update(attendUsers);
    }
}
