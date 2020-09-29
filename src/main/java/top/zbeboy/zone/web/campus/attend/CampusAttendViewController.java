package top.zbeboy.zone.web.campus.attend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;

@Controller
public class CampusAttendViewController {

    @Resource
    private AttendReleaseService attendReleaseService;

    /**
     * 校园签到
     *
     * @return 校园签到页面
     */
    @GetMapping("/web/menu/campus/attend")
    public String index() {
        return "web/campus/attend/attend_data::#page-wrapper";
    }

    /**
     * 校园签到详情
     *
     * @return 校园签到详情页面
     */
    @GetMapping("/web/campus/attend/details/{attendReleaseId}/{attendReleaseSubId}")
    public String details(@PathVariable("attendReleaseId") String attendReleaseId,
                          @PathVariable("attendReleaseSubId") String attendReleaseSubId, ModelMap modelMap) {

        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (attendReleaseService.canReview(users.getUsername(), attendReleaseId)) {
            modelMap.addAttribute("attendReleaseId", attendReleaseId);
            modelMap.addAttribute("attendReleaseSubId", attendReleaseSubId);
            page = "web/campus/attend/attend_details::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
