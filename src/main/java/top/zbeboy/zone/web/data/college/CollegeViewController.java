package top.zbeboy.zone.web.data.college;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CollegeViewController {

    /**
     * 院数据
     *
     * @return 院数据页面
     */
    @GetMapping("/web/menu/data/college")
    public String index() {
        return "web/data/college/college_data::#page-wrapper";
    }

    /**
     * 院数据添加
     *
     * @return 添加页面
     */
    @GetMapping("/web/data/college/add")
    public String add() {
        return "web/data/college/college_add::#page-wrapper";
    }
}
