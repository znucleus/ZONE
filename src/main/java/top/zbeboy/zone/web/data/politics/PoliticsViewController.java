package top.zbeboy.zone.web.data.politics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PoliticsViewController {

    /**
     * 政治面貌数据
     *
     * @return 政治面貌数据页面
     */
    @GetMapping("/web/menu/data/politics")
    public String index() {
        return "web/data/politics/politics_data::#page-wrapper";
    }
}
