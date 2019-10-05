package top.zbeboy.zone.service.answer;

import top.zbeboy.zone.domain.tables.pojos.AnswerResult;
import top.zbeboy.zone.domain.tables.records.AnswerResultRecord;

import java.util.Optional;

public interface AnswerResultService {

    /**
     * 根据发布id与用户id查询
     *
     * @param answerReleaseId 发布id
     * @param userId          用户id
     * @return 数据
     */
    Optional<AnswerResultRecord> findByAnswerReleaseIdAndUserId(String answerReleaseId, String userId);

    /**
     * 根据ip与发布id查询
     *
     * @param answerReleaseId    发布id
     * @param ipAddress ip
     * @return 数据
     */
    Optional<AnswerResultRecord> findByAnswerReleaseIdAndIpAddress(String answerReleaseId, String ipAddress);

    /**
     * 保存
     *
     * @param answerResult 数据o
     */
    void save(AnswerResult answerResult);
}
