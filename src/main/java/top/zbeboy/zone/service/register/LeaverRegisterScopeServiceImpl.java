package top.zbeboy.zone.service.register;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterScope;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;

import static top.zbeboy.zone.domain.Tables.LEAVER_REGISTER_SCOPE;

@Service("leaverRegisterScopeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterScopeServiceImpl implements LeaverRegisterScopeService {

    private final DSLContext create;

    @Autowired
    LeaverRegisterScopeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<LeaverRegisterScopeRecord> findByLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        return create.selectFrom(LEAVER_REGISTER_SCOPE)
                .where(LEAVER_REGISTER_SCOPE.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(LeaverRegisterScope leaverRegisterScope) {
        create.insertInto(LEAVER_REGISTER_SCOPE)
                .set(LEAVER_REGISTER_SCOPE.LEAVER_REGISTER_RELEASE_ID, leaverRegisterScope.getLeaverRegisterReleaseId())
                .set(LEAVER_REGISTER_SCOPE.DATA_ID, leaverRegisterScope.getDataId())
                .execute();
    }

    @Override
    public void deleteByLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        create.deleteFrom(LEAVER_REGISTER_SCOPE).where(LEAVER_REGISTER_SCOPE.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId)).execute();
    }


}
