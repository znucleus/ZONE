package top.zbeboy.zone.service.answer;

import top.zbeboy.zone.domain.tables.records.AnswerSubjectRecord;

public interface AnswerSubjectService {

    /**
     * 通过题库id与自定义id查询
     *
     * @param answerBankId 题库id
     * @param customId     自定义id
     * @return 数据
     */
    AnswerSubjectRecord findByAnswerBankIdAndCustomId(String answerBankId, int customId);
}
