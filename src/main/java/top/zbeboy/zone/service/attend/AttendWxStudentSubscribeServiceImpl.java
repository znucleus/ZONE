package top.zbeboy.zone.service.attend;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.domain.tables.daos.AttendWxStudentSubscribeDao;
import top.zbeboy.zone.domain.tables.pojos.AttendWxStudentSubscribe;
import top.zbeboy.zone.domain.tables.records.AttendWxStudentSubscribeRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.ATTEND_WX_STUDENT_SUBSCRIBE;

@Service("attendWxStudentSubscribeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendWxStudentSubscribeServiceImpl implements AttendWxStudentSubscribeService {

    private final DSLContext create;

    @Resource
    private AttendWxStudentSubscribeDao attendWxStudentSubscribeDao;

    @Autowired
    AttendWxStudentSubscribeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<AttendWxStudentSubscribeRecord> findByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId) {
        return create.selectFrom(ATTEND_WX_STUDENT_SUBSCRIBE)
                .where(ATTEND_WX_STUDENT_SUBSCRIBE.ATTEND_RELEASE_ID.eq(attendReleaseId)
                .and(ATTEND_WX_STUDENT_SUBSCRIBE.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AttendWxStudentSubscribe attendWxStudentSubscribe) {
        attendWxStudentSubscribeDao.insert(attendWxStudentSubscribe);
    }

    @Override
    public void deleteByAttendReleaseIdAndStudentId(String attendReleaseId, int studentId) {
        create.deleteFrom(ATTEND_WX_STUDENT_SUBSCRIBE)
                .where(ATTEND_WX_STUDENT_SUBSCRIBE.ATTEND_RELEASE_ID.eq(attendReleaseId).and(ATTEND_WX_STUDENT_SUBSCRIBE.STUDENT_ID.eq(studentId)))
                .execute();
    }
}
