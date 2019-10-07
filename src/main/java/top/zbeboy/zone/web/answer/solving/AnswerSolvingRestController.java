package top.zbeboy.zone.web.answer.solving;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AnswerResult;
import top.zbeboy.zone.domain.tables.pojos.AnswerSolving;
import top.zbeboy.zone.domain.tables.pojos.AnswerSubject;
import top.zbeboy.zone.domain.tables.records.AnswerResultRecord;
import top.zbeboy.zone.service.answer.AnswerResultService;
import top.zbeboy.zone.service.answer.AnswerSolvingService;
import top.zbeboy.zone.service.answer.AnswerSubjectService;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class AnswerSolvingRestController {

    @Resource
    private AnswerSolvingService answerSolvingService;

    @Resource
    private AnswerResultService answerResultService;

    @Resource
    private AnswerSubjectService answerSubjectService;

    /**
     * 保存结果
     *
     * @param answerSolvingList 数据
     * @return 数据
     */
    @CrossOrigin()
    @PostMapping("/rest/answer/result")
    public ResponseEntity<Map<String, Object>> save(@RequestBody List<AnswerSolving> answerSolvingList, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Objects.nonNull(answerSolvingList) && !answerSolvingList.isEmpty()) {

            Optional<AnswerSolving> solvingOptional = answerSolvingList.stream().findFirst();
            AnswerSolving answerSolvingCheck = solvingOptional.get();
            Optional<AnswerResultRecord> answerResultRecordOptional = answerResultService.findByAnswerReleaseIdAndUserId(answerSolvingCheck.getAnswerReleaseId(), answerSolvingCheck.getUserId());
            if (!answerResultRecordOptional.isPresent()) {
                String ip = RequestUtil.getIpAddress(request);
                Optional<AnswerResultRecord> ipCheck = answerResultService.findByAnswerReleaseIdAndIpAddress(answerSolvingCheck.getAnswerReleaseId(), ip);
                if(!ipCheck.isPresent()){
                    // 计算最后结果
                    double lastScore = 0;
                    double totalScore = 0;
                    boolean hasError = false;

                    int count = 0;
                    AnswerResult answerResult = new AnswerResult();
                    answerResult.setAnswerResultId(UUIDUtil.getUUID());
                    answerResult.setIpAddress(ip);
                    for (AnswerSolving answerSolving : answerSolvingList) {
                        AnswerSubject answerSubject = answerSubjectService.findById(answerSolving.getAnswerSubjectId());
                        if (Objects.isNull(answerSubject)) {
                            ajaxUtil.fail().msg("未查询到题目id ：" + answerSolving.getAnswerSubjectId());
                            hasError = true;
                            break;
                        }
                        answerSolving.setAnswerSolvingId(UUIDUtil.getUUID());
                        answerSolving.setRightKey(answerSubject.getRightKey());
                        totalScore += answerSubject.getScore();
                        if (StringUtils.equals(answerSolving.getSelectKey(), answerSubject.getRightKey())) {
                            lastScore += answerSubject.getScore();
                        }

                        if (count == 0) {
                            count++;
                            answerResult.setAnswerReleaseId(answerSolving.getAnswerReleaseId());
                            answerResult.setUserId(answerSolving.getUserId());
                            answerResult.setUserName(answerSolving.getUserName());
                        }
                    }

                    answerResult.setLastSocre(lastScore);
                    answerResult.setTotalScore(totalScore);

                    if (!hasError) {
                        answerSolvingService.save(answerSolvingList);
                        answerResultService.save(answerResult);
                        ajaxUtil.success().msg("保存成功，得分：" + totalScore);
                    }
                } else {
                    ajaxUtil.fail().msg("您已提交过");
                }
            } else {
                ajaxUtil.fail().msg("您本次考试已提交过");
            }
        } else {
            ajaxUtil.fail().msg("无数据可保存");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
