package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static top.zbeboy.zone.domain.Tables.FILES;
import static top.zbeboy.zone.domain.Tables.TRAINING_SPECIAL_FILE;

@Service("trainingSpecialFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialFileServiceImpl implements TrainingSpecialFileService {

    private final DSLContext create;

    @Autowired
    TrainingSpecialFileServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findByFileTypeId(String fileTypeId) {
        return create.select()
                .from(TRAINING_SPECIAL_FILE)
                .leftJoin(FILES)
                .on(TRAINING_SPECIAL_FILE.FILE_ID.eq(FILES.FILE_ID))
                .where(TRAINING_SPECIAL_FILE.FILE_TYPE_ID.eq(fileTypeId))
                .fetch();
    }

    @Override
    public Result<Record> findByFileTypeIdAndMapping(String fileTypeId, Byte mapping) {
        return create.select()
                .from(TRAINING_SPECIAL_FILE)
                .leftJoin(FILES)
                .on(TRAINING_SPECIAL_FILE.FILE_ID.eq(FILES.FILE_ID))
                .where(TRAINING_SPECIAL_FILE.FILE_TYPE_ID.eq(fileTypeId)
                        .and(TRAINING_SPECIAL_FILE.MAPPING.eq(mapping)))
                .fetch();
    }
}
