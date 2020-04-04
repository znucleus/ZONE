package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingConfigureDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingConfigure;

import javax.annotation.Resource;

import java.util.Optional;

import static org.jooq.impl.DSL.currentDate;
import static org.jooq.impl.DSL.now;
import static top.zbeboy.zone.domain.Tables.*;

@Service("trainingConfigureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingConfigureServiceImpl implements TrainingConfigureService {

    private final DSLContext create;

    @Resource
    private TrainingConfigureDao trainingConfigureDao;

    @Autowired
    TrainingConfigureServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TrainingConfigure findById(String id) {
        return trainingConfigureDao.findById(id);
    }

    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(TRAINING_CONFIGURE)
                .leftJoin(SCHOOLROOM)
                .on(TRAINING_CONFIGURE.SCHOOLROOM_ID.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .where(TRAINING_CONFIGURE.TRAINING_CONFIGURE_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findByTrainingReleaseIdRelation(String trainingReleaseId) {
        return create.select()
                .from(TRAINING_CONFIGURE)
                .leftJoin(SCHOOLROOM)
                .on(TRAINING_CONFIGURE.SCHOOLROOM_ID.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .where(TRAINING_CONFIGURE.TRAINING_RELEASE_ID.eq(trainingReleaseId))
                .fetch();
    }

    @Override
    public Result<Record> findIsAuto(byte dayOfWeek) {
        return create.select()
                .from(TRAINING_CONFIGURE)
                .leftJoin(TRAINING_RELEASE)
                .on(TRAINING_CONFIGURE.TRAINING_RELEASE_ID.eq(TRAINING_RELEASE.TRAINING_RELEASE_ID))
                .where(TRAINING_CONFIGURE.WEEK_DAY.eq(dayOfWeek)
                        .and(TRAINING_RELEASE.START_DATE.le(currentDate()))
                .and(TRAINING_RELEASE.END_DATE.ge(currentDate())))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingConfigure trainingConfigure) {
        trainingConfigureDao.insert(trainingConfigure);
    }

    @Override
    public void update(TrainingConfigure trainingConfigure) {
        trainingConfigureDao.update(trainingConfigure);
    }

    @Override
    public void deleteById(String id) {
        trainingConfigureDao.deleteById(id);
    }
}
