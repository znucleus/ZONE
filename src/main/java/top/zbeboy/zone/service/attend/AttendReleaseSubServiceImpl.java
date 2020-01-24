package top.zbeboy.zone.service.attend;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AttendReleaseSubDao;
import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;

import javax.annotation.Resource;
import java.util.List;

import static org.jooq.impl.DSL.selectFrom;
import static top.zbeboy.zone.domain.Tables.ATTEND_RELEASE;
import static top.zbeboy.zone.domain.Tables.ATTEND_RELEASE_SUB;

@Service("attendReleaseSubService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendReleaseSubServiceImpl implements AttendReleaseSubService {

    private final DSLContext create;

    @Resource
    private AttendReleaseSubDao attendReleaseSubDao;

    @Autowired
    AttendReleaseSubServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void copyAttendRelease(String attendReleaseId) {
        create.insertInto(ATTEND_RELEASE_SUB)
                .select(selectFrom(ATTEND_RELEASE).where(ATTEND_RELEASE.ATTEND_RELEASE_ID.eq(attendReleaseId)))
                .execute();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<AttendReleaseSub> attendReleaseSubs) {
        attendReleaseSubDao.insert(attendReleaseSubs);
    }
}
