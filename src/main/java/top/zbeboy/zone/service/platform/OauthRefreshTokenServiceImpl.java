package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static top.zbeboy.zone.domain.Tables.OAUTH_REFRESH_TOKEN;

@Service("oauthRefreshTokenService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OauthRefreshTokenServiceImpl implements OauthRefreshTokenService {

    private final DSLContext create;

    @Autowired
    OauthRefreshTokenServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public void deleteByTokenId(String tokenId) {
        create.deleteFrom(OAUTH_REFRESH_TOKEN).where(OAUTH_REFRESH_TOKEN.TOKEN_ID.eq(tokenId)).execute();
    }
}
