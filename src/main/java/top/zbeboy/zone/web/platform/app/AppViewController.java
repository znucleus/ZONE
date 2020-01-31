package top.zbeboy.zone.web.platform.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppViewController {

    /**
     * 平台应用
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/app")
    public String index() {
        return "web/platform/app/app_data::#page-wrapper";
    }
}
