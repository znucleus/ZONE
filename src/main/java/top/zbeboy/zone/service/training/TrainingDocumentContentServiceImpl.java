package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.pojos.TrainingDocumentContent;

import static top.zbeboy.zone.domain.Tables.TRAINING_DOCUMENT_CONTENT;

@Service("trainingDocumentContentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingDocumentContentServiceImpl implements TrainingDocumentContentService {

    private final DSLContext create;

    @Autowired
    TrainingDocumentContentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingDocumentContent trainingDocumentContent) {
        create.insertInto(TRAINING_DOCUMENT_CONTENT)
                .set(TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_ID, trainingDocumentContent.getTrainingDocumentId())
                .set(TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_CONTENT_, trainingDocumentContent.getTrainingDocumentContent())
                .execute();
    }

    @Override
    public void update(TrainingDocumentContent trainingDocumentContent) {
        create.update(TRAINING_DOCUMENT_CONTENT)
                .set(TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_CONTENT_, trainingDocumentContent.getTrainingDocumentContent())
                .where(TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_ID.eq(trainingDocumentContent.getTrainingDocumentId()))
                .execute();
    }
}
