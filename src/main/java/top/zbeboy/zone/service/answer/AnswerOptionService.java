package top.zbeboy.zone.service.answer;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.AnswerOptionRecord;

public interface AnswerOptionService {

    /**
     * 通过题目id查询带排序
     *
     * @param answerSubjectId 题目id
     * @return 数据
     */
    Result<AnswerOptionRecord> findByAnswerSubjectId(String answerSubjectId);
}
