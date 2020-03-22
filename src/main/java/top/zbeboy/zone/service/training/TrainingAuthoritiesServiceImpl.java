package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.domain.tables.daos.TrainingAuthoritiesDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingAuthorities;

import javax.annotation.Resource;

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
    public Result<Record> findByTrainingReleaseIdRelation(String trainingReleaseId) {
        return create.select()
                .from(TRAINING_AUTHORITIES)
                .leftJoin(USERS)
                .on(TRAINING_AUTHORITIES.USERNAME.eq(USERS.USERNAME))
                .where(TRAINING_AUTHORITIES.TRAINING_RELEASE_ID.eq(trainingReleaseId))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingAuthorities trainingAuthorities) {
        trainingAuthoritiesDao.insert(trainingAuthorities);
    }

    @Override
    public void update(TrainingAuthorities trainingAuthorities) {
        trainingAuthoritiesDao.update(trainingAuthorities);
    }

    @Override
    public void deleteById(String id) {
        trainingAuthoritiesDao.deleteById(id);
    }
}
