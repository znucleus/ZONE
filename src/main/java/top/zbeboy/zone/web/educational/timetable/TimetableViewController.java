package top.zbeboy.zone.web.educational.timetable;

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
public class TimetableViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    /**
     * 课表
     *
     * @return 课表页面
     */
    @GetMapping("/web/menu/educational/timetable")
    public String index() {
        return "web/educational/timetable/timetable_data::#page-wrapper";
    }


    /**
     * 新教务系统数据导入
     *
     * @return 页面
     */
    @GetMapping("/web/educational/timetable/import")
    public String timetableImport(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                if (optionalStudentBean.isPresent()) {
                    StudentBean studentBean = optionalStudentBean.get();
                    if (studentBean.getCollegeId() == 1) {
                        modelMap.addAttribute("studentNumber", studentBean.getStudentNumber());
                    }
                }
            }
        }
        return "web/educational/timetable/timetable_import::#page-wrapper";
    }
}
