package top.zbeboy.zone.web.system.log;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemLogViewController {

    /**
     * 系统日志页面
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/log")
    public String index() {
        return "web/system/log/system_log::#page-wrapper";
    }
}
