package top.zbeboy.zone.web.data.science;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScienceViewController {

    /**
     * 专业数据
     *
     * @return 专业数据页面
     */
    @GetMapping("/web/menu/data/science")
    public String index() {
        return "web/data/science/science_data::#page-wrapper";
    }
}
