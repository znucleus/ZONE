package top.zbeboy.zone.web.system.health;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemHealthViewController {
    /**
     * 系统状况
     *
     * @return 系统状况页面
     */
    @GetMapping("/web/menu/system/health")
    public String index() {
        return "web/system/health/system_health::#page-wrapper";
    }
}
