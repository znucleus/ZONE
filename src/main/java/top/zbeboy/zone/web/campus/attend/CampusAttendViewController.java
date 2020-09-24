package top.zbeboy.zone.web.campus.attend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CampusAttendViewController {

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
    @GetMapping("/web/campus/attend/details/{attendReleaseSubId}")
    public String details(@PathVariable("attendReleaseSubId") String attendReleaseSubId, ModelMap modelMap) {
        modelMap.addAttribute("attendReleaseSubId", attendReleaseSubId);
        return "web/campus/attend/attend_details::#page-wrapper";
    }
}
