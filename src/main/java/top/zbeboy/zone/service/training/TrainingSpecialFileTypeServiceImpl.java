package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.TrainingSpecialFileTypeDao;
import top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType;
import top.zbeboy.zone.domain.tables.records.TrainingSpecialFileTypeRecord;

import javax.annotation.Resource;

import static top.zbeboy.zone.domain.Tables.TRAINING_SPECIAL_FILE_TYPE;

@Service("trainingSpecialFileTypeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialFileTypeServiceImpl implements TrainingSpecialFileTypeService {

    private final DSLContext create;

    @Resource
    private TrainingSpecialFileTypeDao trainingSpecialFileTypeDao;

    @Autowired
    TrainingSpecialFileTypeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TrainingSpecialFileType findById(String id) {
        return trainingSpecialFileTypeDao.findById(id);
    }

    @Override
    public Result<TrainingSpecialFileTypeRecord> findByTrainingSpecialId(String trainingSpecialId) {
        return create.selectFrom(TRAINING_SPECIAL_FILE_TYPE)
                .where(TRAINING_SPECIAL_FILE_TYPE.TRAINING_SPECIAL_ID.eq(trainingSpecialId))
                .orderBy(TRAINING_SPECIAL_FILE_TYPE.FILE_TYPE_ID.asc())
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingSpecialFileType trainingSpecialFileType) {
        trainingSpecialFileTypeDao.insert(trainingSpecialFileType);
    }

    @Override
    public void update(TrainingSpecialFileType trainingSpecialFileType) {
        trainingSpecialFileTypeDao.update(trainingSpecialFileType);
    }

    @Override
    public void deleteById(String id) {
        trainingSpecialFileTypeDao.deleteById(id);
    }
}
