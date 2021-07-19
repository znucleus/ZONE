package top.zbeboy.zone.web.questionnaire;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.questionnaire.QuestionnaireSubjectBean;
import top.zbeboy.zbase.feign.questionnaire.QuestionnaireService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.questionnaire.QuestionnaireResultAddVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class QuestionnaireRestController {

    @Resource
    private QuestionnaireService questionnaireService;

    /**
     * 获取问题数据
     *
     * @return 问题数据
     */
    @GetMapping("/anyone/questionnaire/subjects/{id}")
    public ResponseEntity<Map<String, Object>> subjects(@PathVariable("id") String id) {
        Optional<List<QuestionnaireSubjectBean>> optionalQuestionnaireSubjectBeans = questionnaireService.subjects(id);
        AjaxUtil<QuestionnaireSubjectBean> ajaxUtil = AjaxUtil.of();
        if (optionalQuestionnaireSubjectBeans.isPresent()) {
            ajaxUtil.success().list(optionalQuestionnaireSubjectBeans.get()).msg("获取数据成功");
        } else {
            ajaxUtil.fail().msg("获取数据失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存结果
     *
     * @return 保存
     */
    @PostMapping("/anyone/questionnaire/result/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("data") String data) {
        List<QuestionnaireResultAddVo> questionnaireResultAddVos = JSON.parseArray(data, QuestionnaireResultAddVo.class);
        AjaxUtil<Map<String, Object>> ajaxUtil = questionnaireService.save(questionnaireResultAddVos);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
