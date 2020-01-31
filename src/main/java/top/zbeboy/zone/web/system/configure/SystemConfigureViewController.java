package top.zbeboy.zone.web.system.configure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemConfigureViewController {

    /**
     * 系统配置页面
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/config")
    public String index() {
        return "web/system/config/system_config::#page-wrapper";
    }
}
