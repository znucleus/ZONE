package top.zbeboy.zone.web.campus.attend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
