package top.zbeboy.zone.service.system;

import org.jooq.DSLContext;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.zbeboy.zone.domain.tables.daos.AuthoritiesDao;
import top.zbeboy.zone.domain.tables.pojos.Authorities;
import top.zbeboy.zone.domain.tables.records.AuthoritiesRecord;

import javax.annotation.Resource;
import java.util.List;

import static top.zbeboy.zone.domain.Tables.AUTHORITIES;
import static top.zbeboy.zone.domain.Tables.USERS;

@Service("authoritiesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AuthoritiesServiceImpl implements AuthoritiesService {

    private final DSLContext create;

    @Resource
    private AuthoritiesDao authoritiesDao;

    @Autowired
    AuthoritiesServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public List<Authorities> findByUsername(String username) {
        return authoritiesDao.fetchByUsername(username);
    }

    @Override
    public Boolean isAnonymousAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !ObjectUtils.isEmpty(authentication) && new AuthenticationTrustResolverImpl().isAnonymous(authentication);
    }

    @Override
    public Select<AuthoritiesRecord> existsAuthoritiesSelect() {
        return create.selectFrom(AUTHORITIES)
                .where(AUTHORITIES.USERNAME.eq(USERS.USERNAME));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void batchSave(List<Authorities> authorities) {
        authoritiesDao.insert(authorities);
    }

    @Override
    public void deleteByAuthorities(String authorities) {
        create.deleteFrom(AUTHORITIES)
                .where(AUTHORITIES.AUTHORITY.eq(authorities))
                .execute();
    }

    @Override
    public void deleteByUsername(String username) {
        create.deleteFrom(AUTHORITIES)
                .where(AUTHORITIES.USERNAME.eq(username))
                .execute();
    }
}
