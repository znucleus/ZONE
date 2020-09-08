package top.zbeboy.zone.web.answer.release;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.answer.release.AnswerReleaseBean;
import top.zbeboy.zbase.domain.tables.pojos.AnswerRelease;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.service.answer.AnswerReleaseService;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@RestController
public class AnswerReleaseRestController {

    @Resource
    private AnswerReleaseService answerReleaseService;

    /**
     * 根据id获取数据
     *
     * @param id 发布id
     * @return 数据
     */
    @CrossOrigin()
    @GetMapping("/anyone/answer/release/{id}")
    public ResponseEntity<Map<String, Object>> data(@PathVariable("id") String id) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        AnswerRelease answerRelease = answerReleaseService.findById(id);
        AnswerReleaseBean bean = new AnswerReleaseBean();
        if (Objects.nonNull(answerRelease)) {
            bean.setAnswerReleaseId(answerRelease.getAnswerReleaseId());
            bean.setTitle(answerRelease.getTitle());
            bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(answerRelease.getStartTime()));
            bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(answerRelease.getEndTime()));
            bean.setAnswerBankId(answerRelease.getAnswerBankId());
            bean.setUsername(answerRelease.getUsername());
            bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(answerRelease.getReleaseTime()));
            ajaxUtil.success().msg("获取数据成功").put("release", bean);
        } else {
            ajaxUtil.fail().msg("无数据").put("release", bean);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
