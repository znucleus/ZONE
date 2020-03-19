package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static top.zbeboy.zone.domain.Tables.BUILDING;
import static top.zbeboy.zone.domain.Tables.SCHOOLROOM;
import static top.zbeboy.zone.domain.Tables.TRAINING_CONFIGURE;

@Service("trainingConfigureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingConfigureServiceImpl implements TrainingConfigureService {

    private final DSLContext create;

    @Autowired
    TrainingConfigureServiceImpl(DSLContext dslContext) {
        create = dslContext;
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
}
