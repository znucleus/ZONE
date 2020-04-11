package top.zbeboy.zone.web.register.leaver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterLeaverViewController {

    /**
     * 离校登记
     *
     * @return 离校登记页面
     */
    @GetMapping("/web/menu/register/leaver")
    public String index() {
        return "web/register/leaver/leaver_release::#page-wrapper";
    }
}
