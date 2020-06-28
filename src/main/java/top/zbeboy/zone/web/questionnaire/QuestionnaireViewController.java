package top.zbeboy.zone.web.questionnaire;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.domain.tables.pojos.QuestionnaireRelease;
import top.zbeboy.zbase.feign.questionnaire.QuestionnaireService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class QuestionnaireViewController {

    @Resource
    private QuestionnaireService questionnaireService;

    /**
     * 外链添加页
     *
     * @return 外链添加页
     */
    @GetMapping("/anyone/questionnaire/result/add/{id}")
    public String anyoneQuestionnaireAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();
        QuestionnaireRelease questionnaireRelease = questionnaireService.findById(id);
        if (Objects.nonNull(questionnaireRelease) &&
                StringUtils.isNotBlank(questionnaireRelease.getQuestionnaireReleaseId())) {
            if (DateTimeUtil.nowRangeSqlDate(questionnaireRelease.getStartDate(), questionnaireRelease.getEndDate())) {
                modelMap.addAttribute("questionnaireRelease", questionnaireRelease);
                return "web/questionnaire/questionnaire_outer_add";
            } else {
                config.buildWarningTip(
                        "进入调查问卷失败。",
                        "该调查问卷已失效。");
                config.addLoginButton();
                config.addHomeButton();
                config.dataMerging(modelMap);
            }
        } else {
            config.buildDangerTip(
                    "进入调查问卷失败。",
                    "未查询到调查问卷信息。");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(modelMap);
        }
        return "tip";
    }

    /**
     * 问卷调查回答成功
     *
     * @param modelMap 页面数据
     * @return 成功
     */
    @GetMapping("/anyone/questionnaire/result/save/success")
    public String questionnaireSaveSuccess(ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig(
                "感谢您参与本次问卷调查。",
                "Z.核校园将一如既往为您提供安全放心的服务。");
        config.addLoginButton();
        config.addHomeButton();
        config.dataMerging(modelMap);
        return "tip";
    }
}
