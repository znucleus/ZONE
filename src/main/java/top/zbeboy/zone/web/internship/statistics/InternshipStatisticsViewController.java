package top.zbeboy.zone.web.internship.statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipStatisticsViewController {

    /**
     * 实习统计
     *
     * @return 实习统计页面
     */
    @GetMapping("/web/menu/internship/statistical")
    public String index() {
        return "web/internship/statistics/internship_statistics::#page-wrapper";
    }
}
