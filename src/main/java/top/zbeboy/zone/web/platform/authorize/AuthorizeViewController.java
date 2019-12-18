package top.zbeboy.zone.web.platform.authorize;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizeViewController {

    /**
     * 平台授权限
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/authorize")
    public String index() {
        return "web/platform/authorize/authorize_data::#page-wrapper";
    }
}
