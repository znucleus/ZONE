package top.zbeboy.zone.web.platform.user;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    @Resource
    private RoleService roleService;

    /**
     * 用户设置页面
     *
     * @return 设置页面
     */
    @GetMapping("/user/profile")
    public String userProfile(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;

        Users users = usersService.getUserFromSession();
        modelMap.addAttribute("realName", users.getRealName());
        modelMap.addAttribute("joinDate", users.getJoinDate());

        // avatar.
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Files files = filesService.findById(users.getAvatar());
            if (Objects.nonNull(files)) {
                modelMap.addAttribute("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
            }
        }

        // roles.
        List<Role> roles = roleService.findByUsername(users.getUsername());
        List<String> rList = new ArrayList<>();
        roles.forEach(r -> rList.add(r.getRoleName()));
        modelMap.addAttribute("roles", String.join(",", rList));

        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.SYSTEM_USERS_TYPE, usersType.getUsersTypeName())) {
                page = "web/platform/user/users_profile_system::#page-wrapper";
            } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = new StudentBean();
                Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    studentBean = record.get().into(StudentBean.class);
                }
                modelMap.addAttribute("student", studentBean);
                page = "web/platform/user/users_profile_student::#page-wrapper";
            } else if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                StaffBean staffBean = new StaffBean();
                Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                if (record.isPresent()) {
                    staffBean = record.get().into(StaffBean.class);
                }
                modelMap.addAttribute("staff", staffBean);
                page = "web/platform/user/users_profile_staff::#page-wrapper";
            } else {
                config.buildDangerTip("数据错误", "暂不支持您的用户类型进行修改");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到您的用户类型");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 用户资料编辑页面
     *
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/user/profile/edit")
    public String userProfileEdit(ModelMap modelMap) {
        Users users = usersService.getUserFromSession();
        modelMap.addAttribute("realName", users.getRealName());
        modelMap.addAttribute("joinDate", users.getJoinDate());

        // avatar.
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Files files = filesService.findById(users.getAvatar());
            if (Objects.nonNull(files)) {
                modelMap.addAttribute("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
            }
        }
        return "web/platform/user/users_profile_edit::#page-wrapper";
    }

    /**
     * 用户设置界面
     *
     * @return 页面
     */
    @GetMapping("/user/setting")
    public String userSetting(ModelMap modelMap) {
        Users users = usersService.getUserFromSession();
        modelMap.addAttribute("username", users.getUsername());
        modelMap.addAttribute("email", users.getEmail());
        modelMap.addAttribute("mobile", users.getMobile());
        modelMap.addAttribute("idCard", users.getIdCard());
        return "web/platform/user/user_setting::#page-wrapper";
    }
}
