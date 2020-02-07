package top.zbeboy.zone.web.internship.distribution;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipTeacherDistributionViewController {

    /**
     * 实习教师分配
     *
     * @return 实习教师分配页面
     */
    @GetMapping("/web/menu/internship/teacher_distribution")
    public String index() {
        return "web/internship/distribution/internship_teacher_distribution::#page-wrapper";
    }
}
