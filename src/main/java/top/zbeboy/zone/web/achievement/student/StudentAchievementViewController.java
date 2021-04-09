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

    /**
     * 学生历史成绩
     *
     * @return 学生历史成绩页面
     */
    @GetMapping("/web/achievement/student/query/history")
    public String history() {
        return "web/achievement/student/student_achievement_history::#page-wrapper";
    }

}
