package top.zbeboy.zone.service.answer;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.AnswerSubjectDao;
import top.zbeboy.zbase.domain.tables.pojos.AnswerSubject;
import top.zbeboy.zbase.domain.tables.records.AnswerSubjectRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.ANSWER_SUBJECT;

@Service("answerSubjectService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerSubjectServiceImpl implements AnswerSubjectService {

    private final DSLContext create;

    @Resource
    private AnswerSubjectDao answerSubjectDao;

    @Autowired
    AnswerSubjectServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public AnswerSubject findById(String id) {
        return answerSubjectDao.findById(id);
    }

    @Override
    public Optional<AnswerSubjectRecord> findByAnswerBankIdAndCustomId(String answerBankId, int customId) {
        return create.selectFrom(ANSWER_SUBJECT)
                .where(ANSWER_SUBJECT.ANSWER_BANK_ID.eq(answerBankId).and(ANSWER_SUBJECT.CUSTOM_ID.eq(customId)))
                .fetchOptional();
    }
}
