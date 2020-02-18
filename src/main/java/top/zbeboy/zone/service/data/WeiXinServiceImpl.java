package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.WeiXinDao;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.domain.tables.records.WeiXinRecord;

import javax.annotation.Resource;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.WEI_XIN;

@Service("weiXinService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WeiXinServiceImpl implements WeiXinService {

    private final DSLContext create;

    @Resource
    private WeiXinDao weiXinDao;

    @Autowired
    WeiXinServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<WeiXinRecord> findByUsername(String username) {
        return create.selectFrom(WEI_XIN).where(WEI_XIN.USERNAME.eq(username)).fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(WeiXin weiXin) {
        weiXinDao.insert(weiXin);
    }

    @Override
    public void update(WeiXin weiXin) {
        weiXinDao.update(weiXin);
    }
}
