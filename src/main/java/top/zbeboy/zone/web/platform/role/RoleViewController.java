package top.zbeboy.zone.web.platform.role;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleViewController {

    /**
     * 平台角色
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/role")
    public String index() {
        return "web/platform/role/role_data::#page-wrapper";
    }
}
