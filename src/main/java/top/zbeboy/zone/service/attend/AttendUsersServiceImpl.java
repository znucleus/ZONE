package top.zbeboy.zone.service.attend;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendUsersDao;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendDataRecord;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.ATTEND_DATA;
import static top.zbeboy.zone.domain.Tables.ATTEND_USERS;

@Service("attendUsersService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendUsersServiceImpl implements AttendUsersService {

    private final DSLContext create;

    @Resource
    private AttendUsersDao attendUsersDao;

    @Autowired
    AttendUsersServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public List<AttendUsers> findByAttendReleaseId(String attendReleaseId) {
        return attendUsersDao.fetchByAttendReleaseId(attendReleaseId);
    }

    @Override
    public Optional<AttendUsersRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId) {
        return create.selectFrom(ATTEND_USERS)
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).and(ATTEND_USERS.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public Result<AttendUsersRecord> findHasAttendedStudent(String attendReleaseId, int attendReleaseSubId) {
        Select<AttendDataRecord> select = create.selectFrom(ATTEND_DATA)
                .where(ATTEND_DATA.ATTEND_USERS_ID.eq(ATTEND_USERS.ATTEND_USERS_ID).and(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)));
        return create.selectFrom(ATTEND_USERS)
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).andExists(select)).fetch();
    }

    @Override
    public Result<AttendUsersRecord> findNotAttendedStudent(String attendReleaseId, int attendReleaseSubId) {
        Select<AttendDataRecord> select = create.selectFrom(ATTEND_DATA)
                .where(ATTEND_DATA.ATTEND_USERS_ID.eq(ATTEND_USERS.ATTEND_USERS_ID).and(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)));
        return create.selectFrom(ATTEND_USERS)
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).andNotExists(select)).fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<AttendUsers> attendUsers) {
        attendUsersDao.insert(attendUsers);
    }
}
