package top.zbeboy.zone.service.attend;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendDataDao;
import top.zbeboy.zone.domain.tables.pojos.AttendData;
import top.zbeboy.zone.domain.tables.records.AttendDataRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.ATTEND_DATA;
import static top.zbeboy.zone.domain.Tables.ATTEND_USERS;

@Service("attendDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendDataServiceImpl implements AttendDataService {

    private final DSLContext create;

    @Resource
    private AttendDataDao attendDataDao;

    @Autowired
    AttendDataServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<AttendDataRecord> findByAttendUsersIdAndAttendReleaseSubId(String attendUsersId, int attendReleaseSubId) {
        return create.selectFrom(ATTEND_DATA)
                .where(ATTEND_DATA.ATTEND_USERS_ID.eq(attendUsersId)
                        .and(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)))
                .fetchOptional();
    }

    @Override
    public Optional<Record> findByStudentIdAndAttendReleaseIdAndAttendReleaseSubId(int studentId, String attendReleaseId, int attendReleaseSubId) {
        return create.select()
                .from(ATTEND_DATA)
                .join(ATTEND_USERS)
                .on(ATTEND_DATA.ATTEND_USERS_ID.eq(ATTEND_USERS.ATTEND_USERS_ID))
                .where(ATTEND_DATA.ATTEND_RELEASE_SUB_ID.eq(attendReleaseSubId)
                .and(ATTEND_USERS.STUDENT_ID.eq(studentId))
                .and(ATTEND_USERS.ATTEND_RELEASE_ID.eq(attendReleaseId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AttendData attendData) {
        attendDataDao.insert(attendData);
    }
}
