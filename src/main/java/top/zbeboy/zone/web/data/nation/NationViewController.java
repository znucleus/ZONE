package top.zbeboy.zone.web.data.nation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NationViewController {

    /**
     * 民族数据
     *
     * @return 民族数据页面
     */
    @GetMapping("/web/menu/data/nation")
    public String index() {
        return "web/data/nation/nation_data::#page-wrapper";
    }
}
