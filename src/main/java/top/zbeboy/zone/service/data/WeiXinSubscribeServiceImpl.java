package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.WeiXinSubscribeDao;
import top.zbeboy.zone.domain.tables.pojos.WeiXinSubscribe;
import top.zbeboy.zone.domain.tables.records.WeiXinSubscribeRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.WEI_XIN;
import static top.zbeboy.zone.domain.Tables.WEI_XIN_SUBSCRIBE;

@Service("weiXinSubscribeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WeiXinSubscribeServiceImpl implements WeiXinSubscribeService {

    private final DSLContext create;

    @Resource
    private WeiXinSubscribeDao weiXinSubscribeDao;

    @Autowired
    WeiXinSubscribeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<WeiXinSubscribeRecord> findByUsernameAndTemplateId(String username, String templateId) {
        return create.selectFrom(WEI_XIN_SUBSCRIBE)
                .where(WEI_XIN_SUBSCRIBE.USERNAME.eq(username).and(WEI_XIN_SUBSCRIBE.TEMPLATE_ID.eq(templateId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(WeiXinSubscribe weiXinSubscribe) {
        weiXinSubscribeDao.insert(weiXinSubscribe);
    }

    @Override
    public void deleteByByUsernameAndTemplateId(String username, String templateId) {
        create.deleteFrom(WEI_XIN_SUBSCRIBE)
                .where(WEI_XIN_SUBSCRIBE.USERNAME.eq(username).and(WEI_XIN_SUBSCRIBE.TEMPLATE_ID.eq(templateId)))
                .execute();
    }
}
