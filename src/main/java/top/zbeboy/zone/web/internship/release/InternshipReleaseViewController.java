package top.zbeboy.zone.web.internship.release;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipReleaseViewController {


    /**
     * 实习发布数据
     *
     * @return 实习发布数据页面
     */
    @GetMapping("/web/menu/internship/release")
    public String index() {
        return "web/internship/release/internship_release::#page-wrapper";
    }
}
