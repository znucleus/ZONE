package top.zbeboy.zone.web.data.staff;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaffViewController {

    /**
     * 教职工数据
     *
     * @return 页面
     */
    @GetMapping("/web/menu/data/staff")
    public String index() {
        return "web/data/staff/staff_data::#page-wrapper";
    }
}
