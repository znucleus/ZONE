package top.zbeboy.zone.service.notify;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.records.SystemNotifyRecord;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.SYSTEM_NOTIFY;

@Service("systemNotifyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemNotifyServiceImpl implements SystemNotifyService {

    private final DSLContext create;

    @Autowired
    SystemNotifyServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<SystemNotifyRecord> findByEffective() {
        return create.selectFrom(SYSTEM_NOTIFY)
                .where(SYSTEM_NOTIFY.EXPIRE_DATE.ge(now())
                        .and(SYSTEM_NOTIFY.VALID_DATE.le(now()))
                        .and(SYSTEM_NOTIFY.EXPIRE_DATE.gt(SYSTEM_NOTIFY.VALID_DATE))).fetch();
    }
}
