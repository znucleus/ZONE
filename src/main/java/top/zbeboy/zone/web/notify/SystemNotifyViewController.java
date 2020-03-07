package top.zbeboy.zone.web.notify;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemNotifyViewController {

    @GetMapping("/web/menu/system/notify")
    public String index() {
        return "web/system/notify/system_notify::#page-wrapper";
    }
}
