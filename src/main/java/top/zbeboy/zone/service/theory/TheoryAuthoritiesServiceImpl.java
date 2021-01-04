package top.zbeboy.zone.service.theory;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.daos.TheoryAuthoritiesDao;
import top.zbeboy.zbase.domain.tables.pojos.TheoryAuthorities;
import top.zbeboy.zbase.domain.tables.records.TheoryAuthoritiesRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zbase.domain.Tables.THEORY_AUTHORITIES;
import static top.zbeboy.zbase.domain.Tables.USERS;

@Service("theoryAuthoritiesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryAuthoritiesServiceImpl implements TheoryAuthoritiesService {

    private final DSLContext create;

    @Resource
    private TheoryAuthoritiesDao theoryAuthoritiesDao;

    @Autowired
    TheoryAuthoritiesServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TheoryAuthorities findById(String id) {
        return theoryAuthoritiesDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.THEORY_AUTHORITIES, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(THEORY_AUTHORITIES)
                .leftJoin(USERS)
                .on(THEORY_AUTHORITIES.USERNAME.eq(USERS.USERNAME))
                .where(THEORY_AUTHORITIES.AUTHORITIES_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findByTheoryReleaseIdRelation(String theoryReleaseId) {
        return create.select()
                .from(THEORY_AUTHORITIES)
                .leftJoin(USERS)
                .on(THEORY_AUTHORITIES.USERNAME.eq(USERS.USERNAME))
                .where(THEORY_AUTHORITIES.THEORY_RELEASE_ID.eq(theoryReleaseId))
                .fetch();
    }

    @Override
    public Result<TheoryAuthoritiesRecord> findEffectiveByTheoryReleaseIdAndUsername(String theoryReleaseId, String username) {
        return create.selectFrom(THEORY_AUTHORITIES)
                .where(THEORY_AUTHORITIES.THEORY_RELEASE_ID.eq(theoryReleaseId)
                        .and(THEORY_AUTHORITIES.USERNAME.eq(username))
                        .and(THEORY_AUTHORITIES.EXPIRE_DATE.gt(now()))
                        .and(THEORY_AUTHORITIES.VALID_DATE.le(now())))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TheoryAuthorities theoryAuthorities) {
        theoryAuthoritiesDao.insert(theoryAuthorities);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_AUTHORITIES, key = "#theoryAuthorities.authoritiesId")
    @Override
    public void update(TheoryAuthorities theoryAuthorities) {
        theoryAuthoritiesDao.update(theoryAuthorities);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_AUTHORITIES, key = "#id")
    @Override
    public void deleteById(String id) {
        theoryAuthoritiesDao.deleteById(id);
    }
}
