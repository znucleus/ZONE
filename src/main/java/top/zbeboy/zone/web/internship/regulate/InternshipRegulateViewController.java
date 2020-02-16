package top.zbeboy.zone.web.internship.regulate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipRegulateViewController {

    /**
     * 实习监管
     *
     * @return 实习监管页面
     */
    @GetMapping("/web/menu/internship/regulate")
    public String index() {
        return "web/internship/regulate/internship_regulate::#page-wrapper";
    }
}
