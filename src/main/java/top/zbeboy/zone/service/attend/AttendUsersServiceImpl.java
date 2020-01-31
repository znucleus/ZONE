package top.zbeboy.zone.service.attend;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendUsersDao;
import top.zbeboy.zone.domain.tables.pojos.AttendUsers;
import top.zbeboy.zone.domain.tables.records.AttendUsersRecord;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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
    public Optional<AttendUsersRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId) {
        return create.selectFrom(ATTEND_USERS)
                .where(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId).and(ATTEND_USERS.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<AttendUsers> attendUsers) {
        attendUsersDao.insert(attendUsers);
    }
}
