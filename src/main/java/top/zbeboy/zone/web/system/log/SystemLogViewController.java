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
        return "web/system/log/system_login_log::#page-wrapper";
    }

    /**
     * 系统API日志页面
     *
     * @return 页面
     */
    @GetMapping("/web/system/log/api")
    public String api() {
        return "web/system/log/system_api_log::#page-wrapper";
    }

    /**
     * 系统操作日志页面
     *
     * @return 页面
     */
    @GetMapping("/web/system/log/operator")
    public String operator() {
        return "web/system/log/system_operator_log::#page-wrapper";
    }
}
