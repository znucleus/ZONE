package top.zbeboy.zone.web.internship.apply;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipApplyViewController {


    /**
     * 实习申请
     *
     * @return 实习申请页面
     */
    @GetMapping("/web/menu/internship/apply")
    public String index(){
        return "web/internship/apply/internship_apply::#page-wrapper";
    }
}
