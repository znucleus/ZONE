package top.zbeboy.zone.web.achievement.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentAchievementViewController {

    /**
     * 学生成绩
     *
     * @return 学生成绩页面
     */
    @GetMapping("/web/menu/achievement/student/query")
    public String index() {
        return "web/achievement/student/student_achievement_query::#page-wrapper";
    }

}
