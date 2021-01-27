package top.zbeboy.zone.web.platform.users;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.SessionBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.GoogleOauthUtil;
import top.zbeboy.zbase.tools.web.util.PinYinUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.user.ResetPasswordVo;
import top.zbeboy.zbase.vo.platform.user.UsersProfileVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.BaseImgUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
public class UsersRestController {

    private final Logger log = LoggerFactory.getLogger(UsersRestController.class);

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private RosterReleaseService rosterReleaseService;

    @Resource
    private FilesService filesService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    /**
     * 检验账号是否被注册
     *
     * @param username 账号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/username")
    public ResponseEntity<Map<String, Object>> anyoneCheckUsername(@RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.anyoneCheckUsername(username);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验账号是否存在
     *
     * @param username 账号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/exist/username")
    public ResponseEntity<Map<String, Object>> anyoneCheckExistUsername(@RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.anyoneCheckExistUsername(username);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验邮箱是否被注册
     *
     * @param email 邮箱
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/email")
    public ResponseEntity<Map<String, Object>> anyoneCheckEmail(@RequestParam("email") String email) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.anyoneCheckEmail(email);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验手机号是否被注册
     *
     * @param mobile 手机号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/mobile")
    public ResponseEntity<Map<String, Object>> anyoneCheckMobile(@RequestParam("mobile") String mobile) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.anyoneCheckMobile(mobile);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验密码是否正确
     *
     * @param password 密码
     * @return 是否正确
     */
    @PostMapping("/users/check/password")
    public ResponseEntity<Map<String, Object>> userCheckPassword(@RequestParam("password") String password) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.userCheckPassword(users.getUsername(), password);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据账号检验是否存在以及该用户状态是否正常
     *
     * @param username 账号
     * @return 是否存在以及该用户状态是否正常
     */
    @PostMapping("/users/check/username/status")
    public ResponseEntity<Map<String, Object>> userCheckStatusByUsername(@RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.userCheckStatusByUsername(username);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验手机号是否被注册
     *
     * @param mobile 手机号
     * @return 是否被注册
     */
    @PostMapping("/users/check/mobile")
    public ResponseEntity<Map<String, Object>> usersCheckMobile(@RequestParam("mobile") String mobile) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.usersCheckMobile(users.getUsername(), mobile);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验邮箱是否被注册
     *
     * @param email 邮箱
     * @return 是否被注册
     */
    @PostMapping("/users/check/email")
    public ResponseEntity<Map<String, Object>> usersCheckEmail(@RequestParam("email") String email) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.usersCheckEmail(users.getUsername(), email);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 重置密码
     *
     * @param resetPasswordVo 数据
     * @param bindingResult   检验
     * @return 重置
     */
    @PostMapping("/reset_password")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid ResetPasswordVo resetPasswordVo, BindingResult bindingResult, HttpSession session) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (resetPasswordVo.getPassword().equals(resetPasswordVo.getOkPassword())) {
                Users users = usersService.findByUsername(resetPasswordVo.getUsername());
                if (Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername())) {
                    // 检验是否已通过验证
                    boolean isValid;
                    if (resetPasswordVo.getVerificationMode() == 0) {
                        Timestamp passwordResetKeyValid = users.getPasswordResetKeyValid();
                        Timestamp now = DateTimeUtil.getNowSqlTimestamp();
                        isValid = now.before(passwordResetKeyValid) && resetPasswordVo.getPasswordResetKey().equals(users.getPasswordResetKey());
                    } else if (resetPasswordVo.getVerificationMode() == 1) {
                        String mobile = users.getMobile();
                        String validKey = mobile + SystemMobileConfig.MOBILE_VALID;
                        isValid = Objects.nonNull(session.getAttribute(validKey)) &&
                                (boolean) session.getAttribute(validKey);
                        // 删除当次验证
                        session.removeAttribute(SystemMobileConfig.MOBILE);
                        session.removeAttribute(validKey);
                    } else {
                        String username = users.getUsername();
                        String validKey = username + SessionBook.DYNAMIC_PASSWORD_VALID;
                        isValid = Objects.nonNull(session.getAttribute(validKey)) &&
                                (boolean) session.getAttribute(validKey);
                        // 删除当次验证
                        session.removeAttribute(SessionBook.DYNAMIC_PASSWORD_USERNAME);
                        session.removeAttribute(validKey);
                    }
                    if (isValid) {
                        users.setPassword(BCryptUtil.bCryptPassword(resetPasswordVo.getPassword()));
                        usersService.update(users);
                        ajaxUtil.success().msg("重置密码成功");
                    } else {
                        ajaxUtil.fail().msg("当前验证方式未验证通过，重置失败");
                    }

                } else {
                    ajaxUtil.fail().msg("查询用户注册信息失败");
                }
            } else {
                ajaxUtil.fail().msg("密码不一致");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 用户基本信息更新
     *
     * @param usersProfileVo 信息
     * @param bindingResult  检验
     * @return 是否更新成功
     */
    @PostMapping("/users/update")
    public ResponseEntity<Map<String, Object>> usersUpdate(@Valid UsersProfileVo usersProfileVo, BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            String name = StringUtils.deleteWhitespace(usersProfileVo.getName());
            String value = StringUtils.deleteWhitespace(usersProfileVo.getValue());

            boolean canUpdate = false;
            Users own = SessionUtil.getUserFromSession();
            if (StringUtils.equals("email", name) ||
                    StringUtils.equals("mobile", name)) {
                int mode = usersProfileVo.getMode();
                if (mode == 0) {
                    String password = usersProfileVo.getPassword();
                    if (StringUtils.isNotBlank(password)) {
                        if (BCryptUtil.bCryptPasswordMatches(password, own.getPassword())) {
                            canUpdate = true;
                        } else {
                            ajaxUtil.fail().msg("登录密码错误");
                        }
                    } else {
                        ajaxUtil.fail().msg("请填写登录密码");
                    }
                } else if (mode == 1) {
                    String dynamicPassword = usersProfileVo.getDynamicPassword();
                    if (StringUtils.isNotBlank(dynamicPassword)) {
                        if (NumberUtils.isDigits(dynamicPassword)) {
                            GoogleOauth googleOauth = usersService.findGoogleOauthByUsername(own.getUsername());
                            if (Objects.nonNull(googleOauth) && StringUtils.isNotBlank(googleOauth.getUsername())) {
                                if (GoogleOauthUtil.validCode(googleOauth.getGoogleOauthKey(), NumberUtils.toInt(dynamicPassword))) {
                                    canUpdate = true;
                                } else {
                                    ajaxUtil.fail().msg("动态密码错误");
                                }
                            } else {
                                ajaxUtil.fail().msg("您未开启双因素认证");
                            }
                        } else {
                            ajaxUtil.fail().msg("动态密码错误，非数字");
                        }
                    } else {
                        ajaxUtil.fail().msg("请填写动态密码");
                    }
                } else {
                    ajaxUtil.fail().msg("不支持的验证模式");
                }
            }

            if (StringUtils.equals("username", name)) {
                ajaxUtil = usersService.checkUsername(value);
                if (BooleanUtils.isTrue(ajaxUtil.getState())) {
                    if (!StringUtils.equals(own.getUsername(), value)) {
                        List<Users> users = usersService.findByUsernameNeOwn(value, own.getUsername());
                        if (Objects.isNull(users) || users.size() <= 0) {
                            // 更新
                            usersService.updateUsername(own.getUsername(), value);
                            ajaxUtil.success().msg("账号更新成功");
                        } else {
                            ajaxUtil.fail().msg("账号已被注册");
                        }
                    } else {
                        ajaxUtil.fail().msg("账号未改变");
                    }
                }
            } else if (StringUtils.equals("realName", name)) {
                if (!StringUtils.equals(own.getRealName(), value)) {
                    own.setRealName(value);
                    usersService.update(own);
                    ajaxUtil.success().msg("姓名更新成功");

                    // 学生需要同步花名册
                    UsersType usersType = usersTypeService.findById(own.getUsersTypeId());
                    if (Objects.nonNull(usersType) && StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Student student = studentService.findByUsername(own.getUsername());
                        RosterData rosterData = rosterReleaseService.findRosterDataByStudentNumber(student.getStudentNumber());
                        if (Objects.nonNull(rosterData) && StringUtils.isNotBlank(rosterData.getRosterDataId())) {
                            rosterData.setRealName(value);
                            rosterData.setNamePinyin(PinYinUtil.changeToUpper(value));
                            rosterReleaseService.dataSync(rosterData);
                        }
                    }
                } else {
                    ajaxUtil.fail().msg("姓名未改变");
                }
            } else if (StringUtils.equals("email", name)) {
                if (canUpdate) {
                    if (Pattern.matches(Workbook.MAIL_REGEX, value)) {
                        if (!StringUtils.equals(own.getEmail(), value)) {
                            List<Users> usersList = usersService.findByEmailNeOwn(value, own.getEmail());
                            if (Objects.isNull(usersList) || usersList.size() <= 0) {
                                // 检查邮件推送是否被关闭
                                SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                                if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                    DateTime dateTime = DateTime.now();
                                    dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());

                                    Users users = SessionUtil.getUserFromSession();
                                    users.setEmail(value);
                                    users.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                                    users.setMailboxVerifyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                                    users.setVerifyMailbox(BooleanUtil.toByte(false));
                                    usersService.update(users);
                                    systemMailService.sendValidEmailMail(users, RequestUtil.getBaseUrl(request));
                                    ajaxUtil.success().msg("邮箱更新成功");
                                } else {
                                    ajaxUtil.fail().msg("邮件推送已被管理员关闭");
                                }
                            } else {
                                ajaxUtil.fail().msg("邮箱已被使用");
                            }
                        } else {
                            ajaxUtil.fail().msg("邮箱未改变");
                        }
                    } else {
                        ajaxUtil.fail().msg("邮箱格式不正确");
                    }
                }
            } else if (StringUtils.equals("mobile", name)) {
                if (canUpdate) {
                    if (Pattern.matches(Workbook.MOBILE_REGEX, value)) {
                        if (!StringUtils.equals(own.getMobile(), value)) {
                            List<Users> usersList = usersService.findByMobileNeOwn(value, own.getMobile());
                            if (Objects.isNull(usersList) || usersList.size() <= 0) {
                                // step 2.手机号是否已验证
                                if (Objects.nonNull(session.getAttribute(value + SystemMobileConfig.MOBILE_VALID))) {
                                    boolean isValid = (boolean) session.getAttribute(value + SystemMobileConfig.MOBILE_VALID);
                                    if (isValid) {
                                        Users users = SessionUtil.getUserFromSession();
                                        users.setMobile(value);
                                        usersService.update(users);
                                        ajaxUtil.success().msg("更新手机号成功");
                                    } else {
                                        ajaxUtil.fail().msg("验证手机号失败");
                                    }
                                } else {
                                    ajaxUtil.fail().msg("请重新验证手机号");
                                }
                            } else {
                                ajaxUtil.fail().msg("手机号已被使用");
                            }
                        } else {
                            ajaxUtil.fail().msg("手机号未改变");
                        }
                    } else {
                        ajaxUtil.fail().msg("手机号不正确");
                    }
                }
            } else if (StringUtils.equals("idCard", name)) {
                if (Pattern.matches(Workbook.ID_CARD_REGEX, value)) {
                    if (!StringUtils.equals(own.getIdCard(), value)) {
                        // 检查是否已经存在该身份证号
                        List<Users> usersList = usersService.findByIdCardNeOwn(value, own.getUsername());
                        if (Objects.isNull(usersList) || usersList.size() <= 0) {
                            own.setIdCard(value);
                            usersService.update(own);
                            ajaxUtil.success().msg("身份证号更新成功");

                            // 学生需要同步花名册
                            UsersType usersType = usersTypeService.findById(own.getUsersTypeId());
                            if (Objects.nonNull(usersType) && StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                                Student student = studentService.findByUsername(own.getUsername());
                                RosterData rosterData = rosterReleaseService.findRosterDataByStudentNumber(student.getStudentNumber());
                                if (Objects.nonNull(rosterData) && StringUtils.isNotBlank(rosterData.getRosterDataId())) {
                                    rosterData.setIdCard(value);
                                    rosterReleaseService.dataSync(rosterData);
                                }
                            }
                        } else {
                            ajaxUtil.fail().msg("身份证号已经存在");
                        }
                    } else {
                        ajaxUtil.fail().msg("身份证号未改变");
                    }
                } else {
                    ajaxUtil.fail().msg("身份证号不正确");
                }
            } else {
                ajaxUtil.fail().msg("未发现更新类型");
            }

        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 双因素认证开启
     *
     * @param password 当前密码
     * @return true or false
     */
    @PostMapping("/users/open/google_oauth")
    public ResponseEntity<Map<String, Object>> userOpenGoogleOauth(@RequestParam("password") String password) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.userOpenGoogleOauth(users.getUsername(), password);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 双因素认证关闭
     *
     * @param mode            验证模式
     * @param password        密码
     * @param dynamicPassword 动态密码
     * @return true or false
     */
    @PostMapping("/users/close/google_oauth")
    public ResponseEntity<Map<String, Object>> userCloseGoogleOauth(@RequestParam("mode") int mode, String password, String dynamicPassword) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.userCloseGoogleOauth(users.getUsername(), mode, password, dynamicPassword);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新密码
     *
     * @param oldPassword     旧密码
     * @param newPassword     新密码
     * @param confirmPassword 确认密码
     * @return 是否成功
     */
    @PostMapping("/users/password/update")
    public ResponseEntity<Map<String, Object>> userPasswordUpdate(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                                                                  @RequestParam("confirmPassword") String confirmPassword) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.userPasswordUpdate(users.getUsername(), oldPassword, newPassword, confirmPassword);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * upload users avatar.
     *
     * @param file     base64 file.
     * @param fileName name.
     * @param request  request.
     * @return success or fail.
     */
    @PostMapping("/users/avatar/upload")
    public ResponseEntity<Map<String, Object>> userAvatarUpload(@RequestParam("file") String file, @RequestParam("fileName") String fileName, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            Files files = BaseImgUtil.generateImage(file, fileName, request, Workbook.avatarPath(users.getUsername()), request.getRemoteAddr());
            filesService.save(files);
            users.setAvatar(files.getFileId());
            usersService.update(users);
            ajaxUtil.success().msg("更新头像成功");
        } catch (Exception e) {
            log.error("User upload avatar error.", e);
            ajaxUtil.fail().msg(String.format("上传头像异常:%s", e.getMessage()));
        }


        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * delete avatar.
     *
     * @param request request.
     * @return success or false
     */
    @GetMapping("/users/avatar/delete")
    public ResponseEntity<Map<String, Object>> userAvatarDelete(HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            String avatar = users.getAvatar();
            if (!StringUtils.equals(avatar, Workbook.USERS_AVATAR)) {
                users.setAvatar(Workbook.USERS_AVATAR);
                usersService.update(users);
                Files files = filesService.findById(avatar);
                if (Objects.nonNull(files) && StringUtils.isNotBlank(files.getFileId())) {
                    // delete file.
                    FilesUtil.deleteFile(RequestUtil.getRealPath(request) + files.getRelativePath());
                    filesService.delete(files);
                }
            }

            ajaxUtil.success().msg("更新头像成功");
        } catch (Exception e) {
            log.error("User upload avatar error.", e);
            ajaxUtil.fail().msg(String.format("上传头像异常:%s", e.getMessage()));
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/users/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("username");
        headers.add("email");
        headers.add("mobile");
        headers.add("idCard");
        headers.add("roleName");
        headers.add("usersTypeName");
        headers.add("enabled");
        headers.add("accountNonLocked");
        headers.add("langKey");
        headers.add("joinDate");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(usersService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 用户角色数据
     *
     * @param username 用户账号
     * @return 数据
     */
    @PostMapping("/web/platform/users/role/data")
    public ResponseEntity<Map<String, Object>> roleData(@RequestParam("username") String username) {
        AjaxUtil<Role> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        ajaxUtil.success().list(usersService.roleData(users.getUsername(), username)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @param request  请求
     * @return success or false
     */
    @PostMapping("/web/platform/users/role/save")
    public ResponseEntity<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("roles") String roles,
                                                        HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.roleSave(users.getUsername(), username, roles);

        if (ajaxUtil.getState()) {
            String notify = "您的权限已发生变更，请登录查看。";

            // 检查邮件推送是否被关闭
            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                systemMailService.sendNotifyMail(usersService.findByUsername(username), RequestUtil.getBaseUrl(request), notify);
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新状态
     *
     * @param userIds 账号
     * @param enabled 状态
     * @return 是否成功
     */
    @PostMapping("/web/platform/users/update/enabled")
    public ResponseEntity<Map<String, Object>> updateEnabled(String userIds, Byte enabled) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.updateEnabled(userIds, enabled);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/web/platform/users/update/locked")
    public ResponseEntity<Map<String, Object>> updateLocked(String userIds, Byte locked) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.updateLocked(userIds, locked);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新密码
     *
     * @param username 账号
     * @return success or fail
     */
    @PostMapping("/web/platform/users/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.updatePassword(username);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/web/platform/users/delete")
    public ResponseEntity<Map<String, Object>> delete(String userIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.delete(userIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 忘记密码动态密码验证
     *
     * @param username        账号
     * @param dynamicPassword 动态密码
     * @return true or false
     */
    @PostMapping("/forget_password/dynamic_password")
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestParam("username") String username,
                                                              @RequestParam("dynamicPassword") String dynamicPassword, HttpSession session) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.forgetPassword(username, dynamicPassword);
        if (ajaxUtil.getState()) {
            session.setAttribute(username + SessionBook.DYNAMIC_PASSWORD_VALID, true);
            session.setAttribute(SessionBook.DYNAMIC_PASSWORD_USERNAME, username);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
