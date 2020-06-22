package top.zbeboy.zone.service.answer;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.AnswerResultDao;
import top.zbeboy.zbase.domain.tables.pojos.AnswerResult;
import top.zbeboy.zbase.domain.tables.records.AnswerResultRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zbase.domain.Tables.ANSWER_RESULT;

@Service("answerResultService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AnswerResultServiceImpl implements AnswerResultService {

    private final DSLContext create;

    @Resource
    private AnswerResultDao answerResultDao;

    @Autowired
    AnswerResultServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Optional<AnswerResultRecord> findByAnswerReleaseIdAndUserId(String answerReleaseId, String userId) {
        return create.selectFrom(ANSWER_RESULT)
                .where(ANSWER_RESULT.ANSWER_RELEASE_ID.eq(answerReleaseId).and(ANSWER_RESULT.USER_ID.eq(userId)))
                .fetchOptional();
    }

    @Override
    public Optional<AnswerResultRecord> findByAnswerReleaseIdAndIpAddress(String answerReleaseId, String ipAddress) {
        return create.selectFrom(ANSWER_RESULT)
                .where(ANSWER_RESULT.IP_ADDRESS.eq(ipAddress).and(ANSWER_RESULT.ANSWER_RELEASE_ID.eq(answerReleaseId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AnswerResult answerResult) {
        answerResultDao.insert(answerResult);
    }
}
