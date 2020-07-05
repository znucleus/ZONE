package top.zbeboy.zone.web.educational.timetable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimetableViewController {

    /**
     * 课表
     *
     * @return 课表页面
     */
    @GetMapping("/web/menu/educational/timetable")
    public String index() {
        return "web/educational/timetable/timetable_data::#page-wrapper";
    }
}
