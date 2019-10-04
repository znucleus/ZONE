package top.zbeboy.zone.service.answer;

import top.zbeboy.zone.domain.tables.pojos.AnswerSubject;
import top.zbeboy.zone.domain.tables.records.AnswerSubjectRecord;

import java.util.Optional;

public interface AnswerSubjectService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    AnswerSubject findById(String id);

    /**
     * 通过题库id与自定义id查询
     *
     * @param answerBankId 题库id
     * @param customId     自定义id
     * @return 数据
     */
    Optional<AnswerSubjectRecord> findByAnswerBankIdAndCustomId(String answerBankId, int customId);
}
