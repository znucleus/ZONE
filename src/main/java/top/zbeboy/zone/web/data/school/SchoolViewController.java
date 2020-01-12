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

    /**
     * 学校数据添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/data/school/add")
    public String add() {
        return "web/data/school/school_add::#page-wrapper";
    }
}
