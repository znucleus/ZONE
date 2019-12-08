package top.zbeboy.zone.web.system.sms;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemSmsLogViewController {

    /**
     * 系统短信
     *
     * @return 系统短信页面
     */
    @GetMapping("web/menu/system/sms")
    public String index()  {
        return "web/system/sms/system_sms::#page-wrapper";
    }
}
