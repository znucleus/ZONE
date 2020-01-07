package top.zbeboy.zone.web.data.school;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SchoolViewController {

    /**
     * 学校数据
     *
     * @return 学校数据页面
     */
    @GetMapping("/web/menu/data/school")
    public String index() {
        return "web/data/school/school_data::#page-wrapper";
    }
}
