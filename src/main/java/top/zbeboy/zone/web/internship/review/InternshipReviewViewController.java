package top.zbeboy.zone.web.internship.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipReviewViewController {

    /**
     * 实习审核
     *
     * @return 实习审核页面
     */
    @GetMapping("/web/menu/internship/review")
    public String index() {
        return "web/internship/review/internship_review::#page-wrapper";
    }
}
