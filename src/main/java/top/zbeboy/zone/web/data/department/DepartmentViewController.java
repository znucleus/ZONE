package top.zbeboy.zone.web.data.department;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DepartmentViewController {

    /**
     * 系数据
     *
     * @return 系数据页面
     */
    @GetMapping("/web/menu/data/department")
    public String index() {
        return "web/data/department/department_data::#page-wrapper";
    }
}
