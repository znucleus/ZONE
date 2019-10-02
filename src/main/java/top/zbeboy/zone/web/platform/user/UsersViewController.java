package top.zbeboy.zone.web.platform.user;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UsersViewController {

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private FilesService filesService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    /**
     * 用户设置页面
     *
     * @return 设置页面
     */
    @GetMapping("/user/setting")
    public String userSetting(ModelMap modelMap) {
        Users users = usersService.getUserFromSession();
        modelMap.addAttribute("username", users.getUsername());
        modelMap.addAttribute("realName", users.getRealName());
        modelMap.addAttribute("email", users.getEmail());
        modelMap.addAttribute("mobile", users.getMobile());
        modelMap.addAttribute("idCard", users.getIdCard());

        // avatar.
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Files files = filesService.findById(users.getAvatar());
            if (Objects.nonNull(files)) {
                modelMap.addAttribute("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
            }
        }

        int isStudent = 0;
        int isStaff = 0;
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                isStudent = 1;
                StudentBean studentBean = new StudentBean();
                Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    studentBean = record.get().into(StudentBean.class);
                }
                modelMap.addAttribute("student", studentBean);
            } else if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                isStaff = 1;
                StaffBean staffBean = new StaffBean();
                Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    staffBean = record.get().into(StaffBean.class);
                }
                modelMap.addAttribute("staff", staffBean);
            }
        }
        modelMap.addAttribute("isStudent", isStudent);
        modelMap.addAttribute("isStaff", isStaff);
        return "web/platform/user/user_setting::#page-wrapper";
    }
}
