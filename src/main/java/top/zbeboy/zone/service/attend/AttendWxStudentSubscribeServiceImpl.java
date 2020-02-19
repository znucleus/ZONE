package top.zbeboy.zone.service.attend;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendWxStudentSubscribeDao;
import top.zbeboy.zone.domain.tables.pojos.AttendWxStudentSubscribe;
import top.zbeboy.zone.domain.tables.records.AttendWxStudentSubscribeRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.ATTEND_RELEASE_SUB;
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

    @Override
    public Result<Record> findSubscribe() {
        return create.select()
                .from(ATTEND_WX_STUDENT_SUBSCRIBE)
                .leftJoin(ATTEND_RELEASE_SUB)
                .on(ATTEND_WX_STUDENT_SUBSCRIBE.ATTEND_RELEASE_ID.eq(ATTEND_RELEASE_SUB.ATTEND_RELEASE_ID))
                .where(ATTEND_RELEASE_SUB.ATTEND_END_TIME.gt(now())
                        .and(ATTEND_RELEASE_SUB.ATTEND_END_TIME.gt(ATTEND_RELEASE_SUB.ATTEND_START_TIME)))
                .fetch();
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

    @Override
    public void deleteByAttendReleaseId(String attendReleaseId) {
        create.deleteFrom(ATTEND_WX_STUDENT_SUBSCRIBE)
                .where(ATTEND_WX_STUDENT_SUBSCRIBE.ATTEND_RELEASE_ID.eq(attendReleaseId))
                .execute();
    }

    @Override
    public void deleteOverdueRecord() {
        SelectConditionStep<Record1<String>> records = create.select(ATTEND_RELEASE_SUB.ATTEND_RELEASE_ID).from(ATTEND_RELEASE_SUB)
                .where(ATTEND_RELEASE_SUB.ATTEND_END_TIME.gt(now()));
        create.deleteFrom(ATTEND_WX_STUDENT_SUBSCRIBE)
                .where(ATTEND_WX_STUDENT_SUBSCRIBE.ATTEND_RELEASE_ID.notIn(records))
                .execute();
    }
}
