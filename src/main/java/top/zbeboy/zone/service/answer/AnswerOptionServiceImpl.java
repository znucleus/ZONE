package top.zbeboy.zone.service.answer;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.records.AnswerOptionRecord;

import static top.zbeboy.zone.domain.Tables.ANSWER_OPTION;

@Service("answerOptionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerOptionServiceImpl implements AnswerOptionService {

    private final DSLContext create;

    @Autowired
    AnswerOptionServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<AnswerOptionRecord> findByAnswerSubjectId(String answerSubjectId) {
        return create.selectFrom(ANSWER_OPTION)
                .where(ANSWER_OPTION.ANSWER_SUBJECT_ID.eq(answerSubjectId))
                .orderBy(ANSWER_OPTION.SORT.asc())
                .fetch();
    }
}
