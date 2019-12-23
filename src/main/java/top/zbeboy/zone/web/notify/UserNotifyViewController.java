package top.zbeboy.zone.web.notify;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserNotifyViewController {

    @GetMapping("/users/notify")
    public String userNotify() {
        return "web/platform/users/users_notify::#page-wrapper";
    }
}
