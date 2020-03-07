package top.zbeboy.zone.web.notify;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemNotifyViewController {

    @GetMapping("/web/menu/system/notify")
    public String index() {
        return "web/system/notify/system_notify::#page-wrapper";
    }

    /**
     * 通知添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/system/notify/add")
    public String add() {
        return "web/system/notify/system_notify_add::#page-wrapper";
    }
}
