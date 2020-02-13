package top.zbeboy.zone.web.internship.review;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;

@Controller
public class InternshipReviewViewController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    /**
     * 实习审核
     *
     * @return 实习审核页面
     */
    @GetMapping("/web/menu/internship/review")
    public String index() {
        return "web/internship/review/internship_review::#page-wrapper";
    }

    /**
     * 权限
     *
     * @param id       实习id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/review/authorize/{id}")
    public String authorize(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewAuthorizeCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/review/internship_review_authorize::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 权限
     *
     * @param id       实习id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/review/audit/{id}")
    public String audit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/review/internship_review_audit::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 通过数据
     *
     * @param id       实习发布id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/review/pass/{id}")
    public String pass(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.reviewCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/review/internship_review_pass::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
