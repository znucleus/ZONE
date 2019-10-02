package top.zbeboy.zone.web.notify;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserNotifyViewController {

    @GetMapping("/user/notify")
    public String userSetting(ModelMap modelMap) {
        return "web/platform/user/user_notify::#page-wrapper";
    }
}
