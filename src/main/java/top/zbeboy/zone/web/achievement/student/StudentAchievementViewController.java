package top.zbeboy.zone.web.achievement.student;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class StudentAchievementViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    /**
     * 学生成绩
     *
     * @return 学生成绩页面
     */
    @GetMapping("/web/menu/achievement/student/query")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                if (optionalStudentBean.isPresent()) {
                    StudentBean studentBean = optionalStudentBean.get();
                    modelMap.put("studentNumber", studentBean.getStudentNumber());
                }
            }
        }
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
