package top.zbeboy.zone.service.training;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.pojos.TrainingSpecialDocumentContent;

import static top.zbeboy.zone.domain.Tables.TRAINING_SPECIAL_DOCUMENT_CONTENT;

@Service("trainingSpecialDocumentContentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TrainingSpecialDocumentContentServiceImpl implements TrainingSpecialDocumentContentService {

    private final DSLContext create;

    @Autowired
    TrainingSpecialDocumentContentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TrainingSpecialDocumentContent trainingSpecialDocumentContent) {
        create.insertInto(TRAINING_SPECIAL_DOCUMENT_CONTENT)
                .set(TRAINING_SPECIAL_DOCUMENT_CONTENT.TRAINING_SPECIAL_DOCUMENT_ID, trainingSpecialDocumentContent.getTrainingSpecialDocumentId())
                .set(TRAINING_SPECIAL_DOCUMENT_CONTENT.CONTENT, trainingSpecialDocumentContent.getContent())
                .execute();
    }

    @Override
    public void update(TrainingSpecialDocumentContent trainingSpecialDocumentContent) {
        create.update(TRAINING_SPECIAL_DOCUMENT_CONTENT)
                .set(TRAINING_SPECIAL_DOCUMENT_CONTENT.CONTENT, trainingSpecialDocumentContent.getContent())
                .where(TRAINING_SPECIAL_DOCUMENT_CONTENT.TRAINING_SPECIAL_DOCUMENT_ID.eq(trainingSpecialDocumentContent.getTrainingSpecialDocumentId()))
                .execute();
    }
}
