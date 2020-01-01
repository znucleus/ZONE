package top.zbeboy.zone.web.data.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentViewController {

    /**
     * 学生数据
     *
     * @return 页面
     */
    @GetMapping("/web/menu/data/student")
    public String index() {
        return "web/data/student/student_data::#page-wrapper";
    }
}
