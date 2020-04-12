package top.zbeboy.zone.service.register;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.LeaverRegisterDataDao;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.LEAVER_REGISTER_DATA;

@Service("leaverRegisterDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterDataServiceImpl implements LeaverRegisterDataService {

    private final DSLContext create;

    @Resource
    private LeaverRegisterDataDao leaverRegisterDataDao;

    @Autowired
    LeaverRegisterDataServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<Record> findByLeaverRegisterReleaseIdAndStudentId(String leaverRegisterReleaseId, int studentId) {
        return create.select()
                .from(LEAVER_REGISTER_DATA)
                .where(LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId)
                        .and(LEAVER_REGISTER_DATA.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(LeaverRegisterData leaverRegisterData) {
        leaverRegisterDataDao.insert(leaverRegisterData);
    }

    @Override
    public void deleteByLeaverRegisterReleaseIdAndStudentId(String leaverRegisterReleaseId, int studentId) {
        create.deleteFrom(LEAVER_REGISTER_DATA)
                .where(LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId)
                        .and(LEAVER_REGISTER_DATA.STUDENT_ID.eq(studentId)))
                .execute();
    }
}
