package top.zbeboy.zone.web.system.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemApplicationViewController {

    /**
     * 系统应用
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/application")
    public String index() {
        return "web/system/application/system_application::#page-wrapper";
    }
}
