package top.zbeboy.zone.web.data.organize;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrganizeViewController {

    /**
     * 班级数据
     *
     * @return 班级数据页面
     */
    @GetMapping("/web/menu/data/organize")
    public String index() {
        return "web/data/organize/organize_data::#page-wrapper";
    }
}
