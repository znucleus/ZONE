package top.zbeboy.zone.service.register;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterDataOption;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterDataOptionRecord;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.LEAVER_REGISTER_DATA_OPTION;

@Service("leaverRegisterDataOptionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterDataOptionServiceImpl implements LeaverRegisterDataOptionService {

    private final DSLContext create;

    @Autowired
    LeaverRegisterDataOptionServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<LeaverRegisterDataOptionRecord> findByLeaverRegisterDataIdAndLeaverRegisterOptionId(String leaverRegisterDataId, String leaverRegisterOptionId) {
        return create.selectFrom(LEAVER_REGISTER_DATA_OPTION)
                .where(LEAVER_REGISTER_DATA_OPTION.LEAVER_REGISTER_DATA_ID.eq(leaverRegisterDataId)
                        .and(LEAVER_REGISTER_DATA_OPTION.LEAVER_REGISTER_OPTION_ID.eq(leaverRegisterOptionId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(LeaverRegisterDataOption leaverRegisterDataOption) {
        create.insertInto(LEAVER_REGISTER_DATA_OPTION)
                .set(LEAVER_REGISTER_DATA_OPTION.LEAVER_REGISTER_DATA_ID, leaverRegisterDataOption.getLeaverRegisterDataId())
                .set(LEAVER_REGISTER_DATA_OPTION.LEAVER_REGISTER_OPTION_ID, leaverRegisterDataOption.getLeaverRegisterOptionId())
                .execute();
    }
}
