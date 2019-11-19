package top.zbeboy.zone.web.notify;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserNotifyViewController {

    @GetMapping("/user/notify")
    public String userSetting() {
        return "web/platform/user/user_notify::#page-wrapper";
    }
}
