package top.zbeboy.zone.web.system.mailbox;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemMailboxLogViewController {

    /**
     * 系统邮件页面
     *
     * @return 页面
     */
    @GetMapping("web/menu/system/mailbox")
    public String index() {
        return "web/system/mailbox/system_mailbox::#page-wrapper";
    }
}
