package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.WeiXinDeviceDao;
import top.zbeboy.zone.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.domain.tables.records.WeiXinDeviceRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.WEI_XIN_DEVICE;

@Service("weiXinDeviceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WeiXinDeviceServiceImpl implements WeiXinDeviceService {

    private final DSLContext create;

    @Resource
    private WeiXinDeviceDao weiXinDeviceDao;

    @Autowired
    WeiXinDeviceServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<WeiXinDeviceRecord> findByUsername(String username) {
        return create.selectFrom(WEI_XIN_DEVICE)
                .where(WEI_XIN_DEVICE.USERNAME.eq(username)).fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(WeiXinDevice weiXinDevice) {
        weiXinDeviceDao.insert(weiXinDevice);
    }

    @Override
    public void update(WeiXinDevice weiXinDevice) {
        weiXinDeviceDao.update(weiXinDevice);
    }
}
