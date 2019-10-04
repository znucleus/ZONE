package top.zbeboy.zone.service.answer;

import top.zbeboy.zone.domain.tables.pojos.AnswerSolving;

import java.util.List;

public interface AnswerSolvingService {

    /**
     * 保存
     *
     * @param answerSolvings 数据
     */
    void save(List<AnswerSolving> answerSolvings);
}
