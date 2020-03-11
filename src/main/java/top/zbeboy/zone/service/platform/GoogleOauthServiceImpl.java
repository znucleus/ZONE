package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.records.GoogleOauthRecord;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.GOOGLE_OAUTH;

@Service("googleOauthService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GoogleOauthServiceImpl implements GoogleOauthService {

    private final DSLContext create;

    @Autowired
    GoogleOauthServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<GoogleOauthRecord> findByUsername(String username) {
        return create.selectFrom(GOOGLE_OAUTH)
                .where(GOOGLE_OAUTH.USERNAME.eq(username))
                .fetchOptional();
    }
}
