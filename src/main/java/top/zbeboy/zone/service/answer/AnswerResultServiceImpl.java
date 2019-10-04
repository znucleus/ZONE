package top.zbeboy.zone.service.answer;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.domain.tables.daos.AnswerResultDao;
import top.zbeboy.zone.domain.tables.pojos.AnswerResult;
import top.zbeboy.zone.domain.tables.records.AnswerResultRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.ANSWER_RESULT;

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
    public Optional<AnswerResultRecord> findByUserIdAndIpAddress(String userId, String ipAddress) {
        return create.selectFrom(ANSWER_RESULT)
                .where(ANSWER_RESULT.IP_ADDRESS.eq(ipAddress).and(ANSWER_RESULT.USER_ID.eq(userId)))
                .fetchOptional();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(AnswerResult answerResult) {
        answerResultDao.insert(answerResult);
    }
}
