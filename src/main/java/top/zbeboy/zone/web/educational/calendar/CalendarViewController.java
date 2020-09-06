package top.zbeboy.zone.web.educational.calendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarViewController {

    /**
     * 校历
     *
     * @return 校历页面
     */
    @GetMapping("/web/menu/educational/calendar")
    public String index() {
        return "web/educational/calendar/calendar_look::#page-wrapper";
    }

    /**
     * 数据管理
     *
     * @return 数据页面
     */
    @GetMapping("/web/educational/calendar/list")
    public String list() {
        return "web/educational/calendar/calendar_data::#page-wrapper";
    }
}
