package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.TrainingAuthoritiesDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingAuthorities;
import top.zbeboy.zone.domain.tables.records.TrainingAuthoritiesRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.TRAINING_AUTHORITIES;
import static top.zbeboy.zone.domain.Tables.USERS;

@Service("trainingAuthoritiesService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingAuthoritiesServiceImpl implements TrainingAuthoritiesService {

    private final DSLContext create;

    @Resource
    private TrainingAuthoritiesDao trainingAuthoritiesDao;

    @Autowired
    TrainingAuthoritiesServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TrainingAuthorities findById(String id) {
        return trainingAuthoritiesDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.TRAINING_AUTHORITIES, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_AUTHORITIES)
                .leftJoin(USERS)
                .on(TRAINING_AUTHORITIES.USERNAME.eq(USERS.USERNAME))
                .where(TRAINING_AUTHORITIES.AUTHORITIES_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findByTrainingReleaseIdRelation(String trainingReleaseId) {
        return create.select()
                .from(TRAINING_AUTHORITIES)
                .leftJoin(USERS)
                .on(TRAINING_AUTHORITIES.USERNAME.eq(USERS.USERNAME))
                .where(TRAINING_AUTHORITIES.TRAINING_RELEASE_ID.eq(trainingReleaseId))
                .fetch();
    }

    @Override
    public Result<TrainingAuthoritiesRecord> findEffectiveByTrainingReleaseIdAndUsername(String trainingReleaseId, String username) {
        return create.selectFrom(TRAINING_AUTHORITIES)
                .where(TRAINING_AUTHORITIES.TRAINING_RELEASE_ID.eq(trainingReleaseId)
                        .and(TRAINING_AUTHORITIES.USERNAME.eq(username))
                        .and(TRAINING_AUTHORITIES.EXPIRE_DATE.gt(now()))
                        .and(TRAINING_AUTHORITIES.VALID_DATE.le(now())))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingAuthorities trainingAuthorities) {
        trainingAuthoritiesDao.insert(trainingAuthorities);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_AUTHORITIES, key = "#trainingAuthorities.authoritiesId")
    @Override
    public void update(TrainingAuthorities trainingAuthorities) {
        trainingAuthoritiesDao.update(trainingAuthorities);
    }

    @CacheEvict(cacheNames = CacheBook.TRAINING_AUTHORITIES, key = "#id")
    @Override
    public void deleteById(String id) {
        trainingAuthoritiesDao.deleteById(id);
    }
}
