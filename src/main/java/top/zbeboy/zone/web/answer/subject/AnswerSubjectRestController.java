package top.zbeboy.zone.web.answer.subject;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AnswerOption;
import top.zbeboy.zone.domain.tables.records.AnswerOptionRecord;
import top.zbeboy.zone.domain.tables.records.AnswerSubjectRecord;
import top.zbeboy.zone.service.answer.AnswerOptionService;
import top.zbeboy.zone.service.answer.AnswerSubjectService;
import top.zbeboy.zone.web.bean.answer.subject.AnswerSubjectBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.answer.subject.AnswerSubjectSearchVo;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@RestController
public class AnswerSubjectRestController {

    @Resource
    private AnswerSubjectService answerSubjectService;

    @Resource
    private AnswerOptionService answerOptionService;

    /**
     * 根据id获取数据
     *
     * @param answerSubjectSearchVo 搜索数据
     * @return 数据
     */
    @CrossOrigin()
    @GetMapping("/anyone/answer/subject")
    public ResponseEntity<Map<String, Object>> data(@RequestBody AnswerSubjectSearchVo answerSubjectSearchVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<AnswerSubjectRecord> answerSubjectRecordOptional =
                answerSubjectService.findByAnswerBankIdAndCustomId(answerSubjectSearchVo.getAnswerBankId(), answerSubjectSearchVo.getCustomId());
        AnswerSubjectBean bean = new AnswerSubjectBean();
        if (answerSubjectRecordOptional.isPresent()) {
            AnswerSubjectRecord answerSubjectRecord = answerSubjectRecordOptional.get();
            bean.setAnswerSubjectId(answerSubjectRecord.getAnswerSubjectId());
            bean.setContent(answerSubjectRecord.getContent());
            bean.setSubjectType(answerSubjectRecord.getSubjectType());
            bean.setAnswerBankId(answerSubjectRecord.getAnswerBankId());
            bean.setScore(answerSubjectRecord.getScore());
            bean.setCustomId(answerSubjectRecord.getCustomId());
            // 选项
            Result<AnswerOptionRecord> records = answerOptionService.findByAnswerSubjectId(answerSubjectRecord.getAnswerSubjectId());
            if (records.isNotEmpty()) {
                bean.setOptions(records.into(AnswerOption.class));
            }
            ajaxUtil.success().msg("获取数据成功").put("subject", bean);
        } else {
            ajaxUtil.fail().msg("无数据").put("subject", bean);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
