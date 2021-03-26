package top.zbeboy.zone.web.achievement.software;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SoftwareViewController {

    /**
     * 软考成绩
     *
     * @return 软考成绩页面
     */
    @GetMapping("/web/menu/achievement/software/query")
    public String index() {
        return "web/achievement/software/software_exam_query::#page-wrapper";
    }


}
