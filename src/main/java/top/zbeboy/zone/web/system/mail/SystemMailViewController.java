package top.zbeboy.zone.web.system.mail;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zbase.bean.data.potential.PotentialBean;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.config.system.mail.SystemMailConfig;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.campus.roster.CampusRosterService;
import top.zbeboy.zbase.feign.data.PotentialService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.AuthorizeService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.feign.system.SystemMailService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Controller
public class SystemMailViewController {

    @Autowired
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private RoleService roleService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private PotentialService potentialService;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private CampusRosterService campusRosterService;

    /**
     * 用户验证邮箱
     *
     * @param mailboxVerifyCode 邮箱验证码
     * @param username          用户账号
     * @param modelMap          页面数据
     * @return 是否验证成功
     */
    @GetMapping(SystemMailConfig.WEB_VALID_EMAIL + "/{mailboxVerifyCode}/{username}")
    public String anyoneValidMail(@PathVariable("mailboxVerifyCode") String mailboxVerifyCode,
                                  @PathVariable("username") String username, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();
        Optional<Users> result = usersService.findByUsername(username);
        if (result.isPresent()) {
            Users users = result.get();
            LocalDateTime mailboxVerifyValid = users.getMailboxVerifyValid();
            LocalDateTime now = DateTimeUtil.getNowLocalDateTime();
            if (now.isBefore(mailboxVerifyValid)) {
                if (StringUtils.equals(mailboxVerifyCode, users.getMailboxVerifyCode())) {
                    users.setVerifyMailbox(BooleanUtil.toByte(true));
                    usersService.update(users);
                    // 检查是否有默认角色
                    String subTitle = "您可以登录系统进行资料完善，耐心等待管理员审核您的角色权限，审核结果会发致您的邮箱，请注意查收。";
                    Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                    if (optionalUsersType.isPresent()) {
                        UsersType usersType = optionalUsersType.get();
                        int collegeId = 0;
                        if (StringUtils.equals(usersType.getUsersTypeName(), Workbook.STAFF_USERS_TYPE)) {
                            Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                            if (optionalStaffBean.isPresent()) {
                                collegeId = optionalStaffBean.get().getCollegeId();
                            }
                        } else if (StringUtils.equals(usersType.getUsersTypeName(), Workbook.STUDENT_USERS_TYPE)) {
                            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                            if (optionalStudentBean.isPresent()) {
                                StudentBean bean = optionalStudentBean.get();
                                collegeId = bean.getCollegeId();

                                // 进行花名册同步
                                Optional<RosterData> optionalRosterData = campusRosterService.findRosterDataByStudentNumber(bean.getStudentNumber());
                                if (optionalRosterData.isPresent()) {
                                    RosterData rosterData = optionalRosterData.get();
                                    rosterData.setPhoneNumber(bean.getMobile());
                                    rosterData.setEmail(bean.getEmail());
                                    campusRosterService.dataSync(rosterData);
                                }
                            }


                        } else if (StringUtils.equals(usersType.getUsersTypeName(), Workbook.POTENTIAL_USERS_TYPE)) {
                            Optional<PotentialBean> optionalPotentialBean = potentialService.findByUsernameRelation(users.getUsername());
                            if (optionalPotentialBean.isPresent()) {
                                collegeId = optionalPotentialBean.get().getCollegeId();
                            }
                        }
                        if (collegeId > 0) {
                            Optional<List<Role>> optionalRoles = roleService.findDefaultRoleByCollegeIdAndUsersTypeId(collegeId, usersType.getUsersTypeId());
                            if (optionalRoles.isPresent()) {
                                List<Authorities> authorities = new ArrayList<>();
                                optionalRoles.get().forEach(role -> authorities.add(new Authorities(username, role.getRoleEnName())));
                                authorizeService.authoritiesBatchSave(authorities);
                                subTitle = "邮箱已验证成功，请及时登录系统进行资料完善。";
                            }
                        }

                    }

                    config.buildSuccessTip(
                            "您的邮箱已验证成功。",
                            subTitle);
                    config.addLoginButton();
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                } else {
                    config.buildDangerTip(
                            "您的邮箱验证失败。",
                            "您的邮箱验证码与系统不一致，点击重新验证按钮，或请及时联系管理员获得帮助。");
                    config.addLoginButton();
                    config.addCustomButton(SystemTipConfig.ButtonClass.OUTLINE_WARNING, "重新验证",
                            SystemMailConfig.WEB_RESEND_MAIL + "/" + username);
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                }
            } else {
                config.buildDangerTip(
                        "您的邮箱验证失败。",
                        "您的邮箱验证码有效期已过，点击重新验证按钮，请及时联系管理员获得帮助。");
                config.addLoginButton();
                config.addCustomButton(SystemTipConfig.ButtonClass.OUTLINE_WARNING, "重新验证",
                        SystemMailConfig.WEB_RESEND_MAIL + "/" + username);
                config.addHomeButton();
                config.dataMerging(modelMap);
            }
        } else {
            config.buildDangerTip(
                    "验证失败。",
                    "未发现您的账号注册信息！");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(modelMap);
        }
        return "tip";
    }

    /**
     * 重新发送验证邮件
     *
     * @param username 用户账号
     * @param request  请求
     * @param modelMap 页面对象
     * @return 消息
     */
    @GetMapping(SystemMailConfig.WEB_RESEND_MAIL)
    public String reSendValidMail(@RequestParam("username") String username, HttpServletRequest request, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();

        Optional<Users> result = Optional.empty();
        boolean hasUser = false;
        if (Pattern.matches(Workbook.MAIL_REGEX, username)) {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("email", username);
            result = usersService.findByCondition(paramMap);
            hasUser = result.isPresent();
        }

        if (!hasUser && Pattern.matches(Workbook.MOBILE_REGEX, username)) {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("mobile", username);
            result = usersService.findByCondition(paramMap);
            hasUser = result.isPresent();
        }

        if (!hasUser) {
            result = usersService.findByUsername(username);
            hasUser = result.isPresent();
        }

        if (hasUser) {
            Users users = result.get();
            if (!Objects.equals(users.getVerifyMailbox(), BooleanUtil.toByte(true))) {
                Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                if (optionalSystemConfigure.isPresent() && StringUtils.equals("1", optionalSystemConfigure.get().getDataValue())) {
                    DateTime dateTime = DateTime.now();
                    dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                    users.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                    users.setMailboxVerifyValid(DateTimeUtil.utilDateToLocalDateTime(dateTime.toDate()));
                    usersService.update(users);
                    systemMailService.sendValidEmailMail(users, RequestUtil.getBaseUrl(request));
                    config.buildSuccessTip(
                            "邮件发送成功。",
                            "验证邮件已发送致您的邮箱: " + users.getEmail() + "，请注意查收，并及时点击邮箱验证。");
                    config.addLoginButton();
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                } else {
                    config.buildDangerTip(
                            "发送失败。",
                            "邮件推送已被管理员关闭。");
                    config.addLoginButton();
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                }
            } else {
                config.buildWarningTip(
                        "发送失败。",
                        "您的邮箱已验证通过。");
                config.addLoginButton();
                config.addHomeButton();
                config.dataMerging(modelMap);
            }
        } else {
            config.buildDangerTip(
                    "发送失败。",
                    "未发现您的账号注册信息！");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(modelMap);
        }
        return "tip";
    }

    /**
     * 重置密码邮件
     *
     * @param passwordResetKey key
     * @param username         账号
     * @param modelMap         页面对象
     * @return 消息
     */
    @GetMapping(SystemMailConfig.WEB_RESET_PASSWORD_MAIL + "/{passwordResetKey}/{username}")
    public String resetPasswordMail(@PathVariable("passwordResetKey") String passwordResetKey,
                                    @PathVariable("username") String username, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();
        Optional<Users> result = usersService.findByUsername(username);
        if (result.isPresent()) {
            Users users = result.get();
            LocalDateTime passwordResetKeyValid = users.getPasswordResetKeyValid();
            LocalDateTime now = DateTimeUtil.getNowLocalDateTime();
            if (now.isBefore(passwordResetKeyValid)) {
                if (StringUtils.equals(passwordResetKey, users.getPasswordResetKey())) {
                    modelMap.addAttribute("username", username);
                    modelMap.addAttribute("verificationMode", 0);
                    modelMap.addAttribute("passwordResetKey", passwordResetKey);
                    return "reset_password";
                } else {
                    config.buildDangerTip(
                            "重置密码失败。",
                            "您的重置验证码与系统不一致。");
                    config.addLoginButton();
                    config.addHomeButton();
                    config.dataMerging(modelMap);
                }
            } else {
                config.buildDangerTip(
                        "重置密码失败。",
                        "您的重置验证码有效期已过。");
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
        return "tip";
    }
}
