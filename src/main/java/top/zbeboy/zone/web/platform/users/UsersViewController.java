package top.zbeboy.zone.web.platform.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.config.SessionBook;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.RoleService;
import top.zbeboy.zone.feign.platform.UsersService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.feign.system.FilesService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * 重置密码
     *
     * @param modelMap 页面对象
     * @return 消息
     */
    @GetMapping("/anyone/reset_password/dynamic_password")
    public String resetPasswordDynamicPassword(HttpSession session, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();
        final String usernameKey = SessionBook.DYNAMIC_PASSWORD_USERNAME;
        if (Objects.nonNull(session.getAttribute(usernameKey))) {
            String username = (String) session.getAttribute(usernameKey);
            Users users = usersService.findByUsername(username);
            if (Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername())) {
                String validKey = username + SessionBook.DYNAMIC_PASSWORD_VALID;
                if (Objects.nonNull(session.getAttribute(validKey))) {
                    boolean isValid = (boolean) session.getAttribute(validKey);
                    if (isValid) {
                        modelMap.addAttribute("username", users.getUsername());
                        modelMap.addAttribute("verificationMode", 2);
                        return "reset_password";
                    } else {
                        config.buildDangerTip(
                                "重置密码失败。",
                                "动态密码验证未通过。");
                        config.addLoginButton();
                        config.addHomeButton();
                        config.dataMerging(modelMap);
                    }
                } else {
                    config.buildDangerTip(
                            "重置密码失败。",
                            "动态密码未验证。");
                    config.addLoginButton();
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                }
            } else {
                config.buildDangerTip(
                        "重置密码失败。",
                        "未发现您的账号注册信息！");
                config.addLoginButton();
                config.addHomeButton();
                config.dataMerging(modelMap);
            }
        } else {
            config.buildDangerTip(
                    "重置密码失败。",
                    "获取账号失败！");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(modelMap);
        }

        return "tip";
    }

    /**
     * 用户设置页面
     *
     * @return 设置页面
     */
    @GetMapping("/users/profile")
    public String usersProfile(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;

        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("realName", users.getRealName());
        modelMap.addAttribute("joinDate", users.getJoinDate());

        // avatar.
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Files files = filesService.findById(users.getAvatar());
            if (Objects.nonNull(files) && StringUtils.isNotBlank(files.getFileId())) {
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
                page = "web/platform/users/users_profile_system::#page-wrapper";
            } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                modelMap.addAttribute("student", studentBean);
                page = "web/platform/users/users_profile_student::#page-wrapper";
            } else if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                modelMap.addAttribute("staff", bean);
                page = "web/platform/users/users_profile_staff::#page-wrapper";
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
    @GetMapping("/users/profile/edit")
    public String usersProfileEdit(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("realName", users.getRealName());
        modelMap.addAttribute("joinDate", users.getJoinDate());

        // avatar.
        if (StringUtils.isNotBlank(users.getAvatar())) {
            Files files = filesService.findById(users.getAvatar());
            if (Objects.nonNull(files) && StringUtils.isNotBlank(files.getFileId())) {
                modelMap.addAttribute("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
            }
        }
        return "web/platform/users/users_profile_edit::#page-wrapper";
    }

    /**
     * 用户设置界面
     *
     * @return 页面
     */
    @GetMapping("/users/setting")
    public String userSetting(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("username", users.getUsername());
        modelMap.addAttribute("email", StringUtils.overlay(users.getEmail(), "****", 1, users.getEmail().lastIndexOf("@")));
        modelMap.addAttribute("mobile", StringUtils.overlay(users.getMobile(), "****", 3, 6));
        modelMap.addAttribute("idCard", StringUtils.isNotBlank(users.getIdCard()) ? StringUtils.overlay(users.getIdCard(), "****", 3, users.getIdCard().length() - 4) : "");

        GoogleOauth googleOauth = usersService.findGoogleOauthByUsername(users.getUsername());
        if (Objects.nonNull(googleOauth) && StringUtils.isNotBlank(googleOauth.getUsername())) {
            modelMap.addAttribute("isOpenGoogleOauth", 1);
        } else {
            modelMap.addAttribute("isOpenGoogleOauth", 0);
        }

        return "web/platform/users/users_setting::#page-wrapper";
    }

    /**
     * 平台用户
     *
     * @return 平台用户页面
     */
    @GetMapping("/web/menu/platform/users")
    public String index() {
        return "web/platform/users/users_data::#page-wrapper";
    }
}
