package top.zbeboy.zone.web.register.epidemic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterEpidemicViewController {

    /**
     * 疫情登记
     *
     * @return 疫情登记页面
     */
    @GetMapping("/web/menu/register/epidemic")
    public String index() {
        return "web/register/epidemic/epidemic_release::#page-wrapper";
    }
}
