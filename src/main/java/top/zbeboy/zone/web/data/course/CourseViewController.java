package top.zbeboy.zone.web.data.course;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseViewController {

    /**
     * 课程数据
     *
     * @return 课程数据页面
     */
    @GetMapping("/web/menu/data/course")
    public String index() {
        return "web/data/course/course_data::#page-wrapper";
    }
}
