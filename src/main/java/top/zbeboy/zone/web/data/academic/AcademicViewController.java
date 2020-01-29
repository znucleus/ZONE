package top.zbeboy.zone.web.data.academic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcademicViewController {

    /**
     * 职称数据
     *
     * @return 职称数据页面
     */
    @GetMapping("/web/menu/data/academic")
    public String index() {
        return "web/data/academic/academic_data::#page-wrapper";
    }
}
