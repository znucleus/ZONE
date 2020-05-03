package top.zbeboy.zone.web.training.report;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;

import javax.annotation.Resource;

@Controller
public class TrainingReportViewController {

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/report")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("canOperator", trainingConditionCommon.reportCondition());
        return "web/training/report/training_report::#page-wrapper";
    }
}
