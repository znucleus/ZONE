package top.zbeboy.zone.web.educational.timetable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
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
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("isStudent", false);
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                modelMap.addAttribute("isStudent", true);
            }
        }
        return "web/educational/timetable/timetable_data::#page-wrapper";
    }


    /**
     * 新教务系统数据导入
     *
     * @return 页面
     */
    @GetMapping("/web/educational/timetable/import")
    public String timetableImport(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;

        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                if(optionalStudentBean.isPresent()){
                    StudentBean studentBean = optionalStudentBean.get();
                    modelMap.addAttribute("studentNumber", studentBean.getStudentNumber());
                    page = "web/educational/timetable/timetable_import::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到学生信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildWarningTip("操作警告", "抱歉，暂时仅支持学生用户导入");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到用户类型信息");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
