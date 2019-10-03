package top.zbeboy.zone.service.answer;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.records.AnswerSubjectRecord;

import static top.zbeboy.zone.domain.Tables.*;

@Service("answerSubjectService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerSubjectServiceImpl implements AnswerSubjectService {

    private final DSLContext create;

    @Autowired
    AnswerSubjectServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public AnswerSubjectRecord findByAnswerBankIdAndCustomId(String answerBankId, int customId) {
        return create.selectFrom(ANSWER_SUBJECT)
                .where(ANSWER_SUBJECT.ANSWER_BANK_ID.eq(answerBankId).and(ANSWER_SUBJECT.CUSTOM_ID.eq(customId)))
                .fetchOne();
    }
}
