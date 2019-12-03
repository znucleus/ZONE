package top.zbeboy.zone.web.system.role;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemRoleViewController {

    /**
     * 系统角色页面
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/role")
    public String index() {
        return "web/system/role/system_role::#page-wrapper";
    }
}
