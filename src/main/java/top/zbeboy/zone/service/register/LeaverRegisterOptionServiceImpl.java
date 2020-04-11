package top.zbeboy.zone.service.register;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.LeaverRegisterOptionDao;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterOption;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterOptionRecord;
import top.zbeboy.zone.web.util.ByteUtil;

import javax.annotation.Resource;
import java.util.List;

import static org.jooq.impl.DSL.max;
import static top.zbeboy.zone.domain.Tables.LEAVER_REGISTER_OPTION;
import static top.zbeboy.zone.domain.Tables.LEAVER_REGISTER_RELEASE;

@Service("leaverRegisterOptionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaverRegisterOptionServiceImpl implements LeaverRegisterOptionService {

    private final DSLContext create;

    @Resource
    private LeaverRegisterOptionDao leaverRegisterOptionDao;

    @Autowired
    LeaverRegisterOptionServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public LeaverRegisterOption findById(String id) {
        return leaverRegisterOptionDao.findById(id);
    }

    @Override
    public Result<LeaverRegisterOptionRecord> findByLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        return create.selectFrom(LEAVER_REGISTER_OPTION)
                .where(LEAVER_REGISTER_OPTION.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId))
                .orderBy(LEAVER_REGISTER_OPTION.SORT.asc())
                .fetch();
    }

    @Override
    public byte findMaxSortByLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        Record1<Byte> sort = create.select(DSL.ifnull(max(LEAVER_REGISTER_OPTION.SORT), ByteUtil.toByte(0)))
                .from(LEAVER_REGISTER_OPTION)
                .where(LEAVER_REGISTER_OPTION.LEAVER_REGISTER_RELEASE_ID.eq(leaverRegisterReleaseId))
                .fetchOne();
        return sort.value1();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<LeaverRegisterOption> leaverRegisterOptions) {
        leaverRegisterOptionDao.insert(leaverRegisterOptions);
    }

    @Override
    public void update(LeaverRegisterOption leaverRegisterOption) {
        leaverRegisterOptionDao.update(leaverRegisterOption);
    }

    @Override
    public void deleteById(String id) {
        leaverRegisterOptionDao.deleteById(id);
    }
}
