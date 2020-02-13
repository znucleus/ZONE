package top.zbeboy.zone.web.internship.statistical;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;

@Controller
public class InternshipStatisticalViewController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    /**
     * 实习统计
     *
     * @return 实习统计页面
     */
    @GetMapping("/web/menu/internship/statistical")
    public String index() {
        return "web/internship/statistical/internship_statistical::#page-wrapper";
    }

    /**
     * 已提交列表
     *
     * @return 已提交列表 统计页面
     */
    @GetMapping("/web/internship/statistical/submitted/{id}")
    public String submitted(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/statistical/internship_statistical_submitted::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 申请记录列表
     *
     * @return 申请记录列表页面
     */
    @GetMapping("/web/internship/statistical/record/apply/{id}/{studentId}")
    public String changeHistory(@PathVariable("id") String id, @PathVariable("studentId") int studentId, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            modelMap.addAttribute("studentId", studentId);
            page = "web/internship/statistical/internship_change_history::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 申请记录列表
     *
     * @return 申请记录列表页面
     */
    @GetMapping("/web/internship/statistical/record/company/{id}/{studentId}")
    public String changeCompanyHistory(@PathVariable("id") String id, @PathVariable("studentId") int studentId, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            modelMap.addAttribute("studentId", studentId);
            page = "web/internship/statistical/internship_change_company_history::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 未提交列表
     *
     * @return 未提交列表 统计页面
     */
    @GetMapping("/web/internship/statistical/unsubmitted/{id}")
    public String unSubmitted(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/statistical/internship_statistical_unsubmitted::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
