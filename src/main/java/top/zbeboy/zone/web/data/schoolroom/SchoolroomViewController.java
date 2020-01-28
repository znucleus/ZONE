package top.zbeboy.zone.web.data.schoolroom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SchoolroomViewController {

    /**
     * 教室数据
     *
     * @return 教室数据页面
     */
    @GetMapping("/web/menu/data/schoolroom")
    public String index() {
        return "web/data/schoolroom/schoolroom_data::#page-wrapper";
    }
}
