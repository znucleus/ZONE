package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.OauthAccessTokenDao;
import top.zbeboy.zone.domain.tables.pojos.OauthAccessToken;

import javax.annotation.Resource;
import java.util.List;

import static top.zbeboy.zone.domain.Tables.OAUTH_ACCESS_TOKEN;

@Service("oauthAccessTokenService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OauthAccessTokenServiceImpl implements OauthAccessTokenService {

    private final DSLContext create;

    @Resource
    private OauthAccessTokenDao oauthAccessTokenDao;

    @Autowired
    OauthAccessTokenServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public List<OauthAccessToken> findByClientId(String clientId) {
        return oauthAccessTokenDao.fetchByClientId(clientId);
    }

    @Override
    public void deleteByClientId(String clientId) {
        create.deleteFrom(OAUTH_ACCESS_TOKEN).where(OAUTH_ACCESS_TOKEN.CLIENT_ID.eq(clientId)).execute();
    }
}
