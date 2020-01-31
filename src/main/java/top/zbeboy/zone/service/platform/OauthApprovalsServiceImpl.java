package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static top.zbeboy.zone.domain.Tables.OAUTH_APPROVALS;

@Service("oauthApprovalsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OauthApprovalsServiceImpl implements OauthApprovalsService {

    private final DSLContext create;

    @Autowired
    OauthApprovalsServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public void deleteByClientId(String clientId) {
        create.deleteFrom(OAUTH_APPROVALS).where(OAUTH_APPROVALS.CLIENTID.eq(clientId)).execute();
    }
}
