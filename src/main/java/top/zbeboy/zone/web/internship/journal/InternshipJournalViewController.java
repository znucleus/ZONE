package top.zbeboy.zone.web.internship.journal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InternshipJournalViewController {

    /**
     * 实习日志
     *
     * @return 实习日志页面
     */
    @GetMapping("/web/menu/internship/journal")
    public String index() {
        return "web/internship/journal/internship_journal::#page-wrapper";
    }
}
