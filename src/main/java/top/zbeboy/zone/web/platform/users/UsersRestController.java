package top.zbeboy.zone.web.platform.users;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.jooq.Record;
import org.jooq.Record12;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.SessionBook;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.GoogleOauthRecord;
import top.zbeboy.zone.domain.tables.records.UsersRecord;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.*;
import top.zbeboy.zone.web.bean.platform.users.UsersBean;
import top.zbeboy.zone.web.system.mail.SystemMailConfig;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.*;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.user.ResetPasswordVo;
import top.zbeboy.zone.web.vo.platform.user.UsersProfileVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class UsersRestController {

    private final Logger log = LoggerFactory.getLogger(UsersRestController.class);

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private UserNotifyService userNotifyService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private CollegeRoleService collegeRoleService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private GoogleOauthService googleOauthService;

    /**
     * 检验账号是否被注册
     *
     * @param username 账号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/username")
    public ResponseEntity<Map<String, Object>> anyoneCheckUsername(@RequestParam("username") String username) {
        String param = StringUtils.deleteWhitespace(username);
        AjaxUtil<Map<String, Object>> ajaxUtil = checkUsername(username);
        if (BooleanUtils.isTrue(ajaxUtil.getState())) {
            UsersRecord users = usersService.findByUsernameUpper(param);
            if (Objects.nonNull(users)) {
                ajaxUtil.fail().msg("账号已被注册");
            } else {
                ajaxUtil.success().msg("账号未被注册");
            }
        }
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
        String param = StringUtils.deleteWhitespace(username);
        AjaxUtil<Map<String, Object>> ajaxUtil = checkUsername(username);
        Users users = usersService.findByUsername(param);
        if (Objects.isNull(users)) {
            ajaxUtil.fail().msg("账号不存在");
        } else {
            ajaxUtil.success().msg("账号存在");
        }
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
        String param = StringUtils.deleteWhitespace(email);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = SystemMailConfig.MAIL_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("邮箱格式不正确");
        } else {
            Users users = usersService.findByEmail(param);
            if (Objects.nonNull(users)) {
                ajaxUtil.fail().msg("邮箱已被注册");
            } else {
                ajaxUtil.success().msg("邮箱未被注册");
            }
        }
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
        String param = StringUtils.deleteWhitespace(mobile);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = SystemMobileConfig.MOBILE_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("手机号不正确");
        } else {
            Users users = usersService.findByMobile(param);
            if (Objects.nonNull(users)) {
                ajaxUtil.fail().msg("手机号已被注册");
            } else {
                ajaxUtil.success().msg("手机号未被注册");
            }
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromSession();
        if (BCryptUtil.bCryptPasswordMatches(password, users.getPassword())) {
            ajaxUtil.success().msg("密码正确");
        } else {
            ajaxUtil.fail().msg("密码错误");
        }
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
        String param = StringUtils.trim(username);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        UsersRecord record = usersService.findNormalByUsername(param);
        if (Objects.nonNull(record)) {
            ajaxUtil.success().msg("用户信息正常");
        } else {
            ajaxUtil.fail().msg("未查询到该用户信息或该用户状态异常");
        }
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
        String param = StringUtils.deleteWhitespace(mobile);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = SystemMobileConfig.MOBILE_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("手机号不正确");
        } else {
            Users users = usersService.getUserFromSession();
            Result<UsersRecord> usersRecords = usersService.findByMobileNeOwn(param, users.getMobile());
            if (usersRecords.isEmpty()) {
                ajaxUtil.success().msg("手机号未被注册");
            } else {
                ajaxUtil.fail().msg("手机号已被注册");
            }
        }
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
        String param = StringUtils.deleteWhitespace(email);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = SystemMailConfig.MAIL_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("邮箱格式不正确");
        } else {
            Users users = usersService.getUserFromSession();
            Result<UsersRecord> usersRecords = usersService.findByEmailNeOwn(param, users.getEmail());
            if (usersRecords.isEmpty()) {
                ajaxUtil.success().msg("邮箱未被注册");
            } else {
                ajaxUtil.fail().msg("邮箱已被注册");
            }
        }
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
                if (Objects.nonNull(users)) {
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
            Users own = usersService.getUserFromSession();
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
                            Optional<GoogleOauthRecord> googleOauthRecord = googleOauthService.findByUsername(own.getUsername());
                            if (googleOauthRecord.isPresent()) {
                                if (GoogleOauthUtil.validCode(googleOauthRecord.get().getGoogleOauthKey(), NumberUtils.toInt(dynamicPassword))) {
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
                ajaxUtil = checkUsername(value);
                if (BooleanUtils.isTrue(ajaxUtil.getState())) {
                    if (!StringUtils.equals(own.getUsername(), value)) {
                        Result<UsersRecord> usersRecords = usersService.findByUsernameNeOwn(value, own.getUsername());
                        if (usersRecords.isEmpty()) {
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
                Users users = usersService.getUserFromSession();
                if (!StringUtils.equals(users.getRealName(), value)) {
                    users.setRealName(value);
                    usersService.update(users);
                    ajaxUtil.success().msg("姓名更新成功");
                } else {
                    ajaxUtil.fail().msg("姓名未改变");
                }
            } else if (StringUtils.equals("email", name)) {
                if (canUpdate) {
                    if (Pattern.matches(SystemMailConfig.MAIL_REGEX, value)) {
                        if (!StringUtils.equals(own.getEmail(), value)) {
                            Result<UsersRecord> usersRecords = usersService.findByEmailNeOwn(value, own.getEmail());
                            if (usersRecords.isEmpty()) {
                                // 检查邮件推送是否被关闭
                                SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                                if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                    DateTime dateTime = DateTime.now();
                                    dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());

                                    Users users = usersService.getUserFromSession();
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
                    if (Pattern.matches(SystemMobileConfig.MOBILE_REGEX, value)) {
                        if (!StringUtils.equals(own.getMobile(), value)) {
                            Result<UsersRecord> usersRecords = usersService.findByEmailNeOwn(value, own.getMobile());
                            if (usersRecords.isEmpty()) {
                                // step 2.手机号是否已验证
                                if (Objects.nonNull(session.getAttribute(value + SystemMobileConfig.MOBILE_VALID))) {
                                    boolean isValid = (boolean) session.getAttribute(value + SystemMobileConfig.MOBILE_VALID);
                                    if (isValid) {
                                        Users users = usersService.getUserFromSession();
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
                        Result<UsersRecord> records = usersService.findByIdCardNeOwn(value, own.getUsername());
                        if (records.isEmpty()) {
                            own.setIdCard(value);
                            usersService.update(own);
                            ajaxUtil.success().msg("身份证号更新成功");
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromSession();
        if (BCryptUtil.bCryptPasswordMatches(password, users.getPassword())) {
            Optional<GoogleOauthRecord> googleOauthRecord = googleOauthService.findByUsername(users.getUsername());
            if (!googleOauthRecord.isPresent()) {
                String key = GoogleOauthUtil.createKey();
                GoogleOauth googleOauth = new GoogleOauth();
                googleOauth.setUsername(users.getUsername());
                googleOauth.setGoogleOauthKey(key);
                googleOauth.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                googleOauthService.save(googleOauth);
                ajaxUtil.success().msg("开启成功").put("googleOauthKey", key);
            } else {
                ajaxUtil.fail().msg("您已开启双因素认证");
            }
        } else {
            ajaxUtil.fail().msg("登录密码错误");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (mode == 0) {
            if (StringUtils.isNotBlank(password)) {
                Users users = usersService.getUserFromSession();
                if (BCryptUtil.bCryptPasswordMatches(password, users.getPassword())) {
                    googleOauthService.deleteByUsername(users.getUsername());
                    ajaxUtil.success().msg("关闭成功");
                } else {
                    ajaxUtil.fail().msg("登录密码错误");
                }
            } else {
                ajaxUtil.fail().msg("请填写密码");
            }
        } else if (mode == 1) {
            if (StringUtils.isNotBlank(dynamicPassword)) {
                if (NumberUtils.isDigits(dynamicPassword)) {
                    Users users = usersService.getUserFromSession();
                    Optional<GoogleOauthRecord> googleOauthRecord = googleOauthService.findByUsername(users.getUsername());
                    if (googleOauthRecord.isPresent()) {
                        if (GoogleOauthUtil.validCode(googleOauthRecord.get().getGoogleOauthKey(), NumberUtils.toInt(dynamicPassword))) {
                            googleOauthService.deleteByUsername(users.getUsername());
                            ajaxUtil.success().msg("关闭成功");
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromSession();
        if (BCryptUtil.bCryptPasswordMatches(oldPassword, users.getPassword())) {
            String regex = Workbook.PASSWORD_REGEX;
            if (Pattern.matches(regex, confirmPassword)) {
                if (StringUtils.equals(newPassword, confirmPassword)) {
                    users.setPassword(BCryptUtil.bCryptPassword(confirmPassword));
                    usersService.update(users);
                    ajaxUtil.success().msg("更新密码成功");
                } else {
                    ajaxUtil.fail().msg("密码不一致");
                }
            } else {
                ajaxUtil.fail().msg("密码为6-16位任意字母、数字或下划线");
            }
        } else {
            ajaxUtil.fail().msg("原密码错误");
        }
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
            Users users = usersService.getUserFromSession();
            Files files = BaseImgUtil.generateImage(file, fileName, request, Workbook.avatarPath(users), request.getRemoteAddr());
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
            Users users = usersService.getUserFromSession();
            String avatar = users.getAvatar();
            if (!StringUtils.equals(avatar, Workbook.USERS_AVATAR)) {
                users.setAvatar(Workbook.USERS_AVATAR);
                usersService.update(users);
                Files files = filesService.findById(avatar);
                if (Objects.nonNull(files)) {
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
        Result<Record12<String, String, String, String, String, Byte, String, String, Byte, Byte, String, Date>> records = usersService.findAllByPage(dataTablesUtil);
        List<UsersBean> usersBean = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            usersBean = records.into(UsersBean.class);
        }
        dataTablesUtil.setData(usersBean);
        dataTablesUtil.setiTotalRecords(usersService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(usersService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
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
        List<Role> roles = new ArrayList<>();
        Users users = usersService.findByUsername(username);
        if (Objects.nonNull(users)) {
            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                roles.add(roleService.findByRoleEnName(Workbook.authorities.ROLE_ADMIN.name()));
                roles.add(roleService.findByRoleEnName(Workbook.authorities.ROLE_ACTUATOR.name()));
            }

            int collegeId = 0;
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                Optional<Record> record = Optional.empty();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = staffService.findByUsernameRelation(users.getUsername());
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = studentService.findByUsernameRelation(users.getUsername());
                }

                if (record.isPresent()) {
                    collegeId = record.get().into(College.class).getCollegeId();
                }
            }

            if (collegeId > 0) {
                Result<Record> records = collegeRoleService.findByCollegeIdRelation(collegeId);
                if (records.isNotEmpty()) {
                    roles.addAll(records.into(Role.class));
                }
            }
        }

        ajaxUtil.success().list(roles).msg("获取数据成功");

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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(roles)) {
            Users users = usersService.findByUsername(username);
            if (Objects.nonNull(users)) {
                if (Objects.nonNull(users.getVerifyMailbox()) && BooleanUtil.toBoolean(users.getVerifyMailbox())) {
                    List<String> roleList = SmallPropsUtil.StringIdsToStringList(roles);
                    // 禁止非系统用户 提升用户权限到系统或管理员级别权限
                    if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) && (roleList.contains(Workbook.authorities.ROLE_SYSTEM.name()) ||
                            roleList.contains(Workbook.authorities.ROLE_ADMIN.name()) || roleList.contains(Workbook.authorities.ROLE_ACTUATOR.name()))) {
                        ajaxUtil.fail().msg("禁止非系统用户角色提升用户权限到系统或管理员级别权限");
                    } else {
                        authoritiesService.deleteByUsername(username);
                        List<Authorities> authorities = new ArrayList<>();
                        roleList.forEach(role -> authorities.add(new Authorities(username, role)));
                        authoritiesService.batchSave(authorities);

                        String notify = "您的权限已发生变更，请登录查看。";

                        // 检查邮件推送是否被关闭
                        SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                        if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                            systemMailService.sendNotifyMail(users, RequestUtil.getBaseUrl(request), notify);
                        }

                        Users curUsers = usersService.getUserFromSession();
                        UserNotify userNotify = new UserNotify();
                        userNotify.setUserNotifyId(UUIDUtil.getUUID());
                        userNotify.setSendUser(curUsers.getUsername());
                        userNotify.setAcceptUser(users.getUsername());
                        userNotify.setIsSee(BooleanUtil.toByte(false));
                        userNotify.setNotifyType(Workbook.notifyType.info.name());
                        userNotify.setNotifyTitle("权限变更");
                        userNotify.setNotifyContent(notify);
                        userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                        userNotifyService.save(userNotify);

                        ajaxUtil.success().msg("更改用户角色成功");
                    }
                } else {
                    ajaxUtil.fail().msg("该用户未激活邮箱");
                }
            } else {
                ajaxUtil.fail().msg("未查询到该用户信息");
            }
        } else {
            ajaxUtil.fail().msg("用户角色参数异常");
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(userIds)) {
            usersService.updateEnabled(SmallPropsUtil.StringIdsToStringList(userIds), enabled);
            ajaxUtil.success().msg("注销用户成功");
        } else {
            ajaxUtil.fail().msg("用户账号不能为空");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(userIds)) {
            usersService.updateLocked(SmallPropsUtil.StringIdsToStringList(userIds), locked);
            ajaxUtil.success().msg("修改用户锁定状态成功");
        } else {
            ajaxUtil.fail().msg("用户账号不能为空");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String password = RandomUtil.generatePassword();
        usersService.updatePassword(username, BCryptUtil.bCryptPassword(password));
        ajaxUtil.success().msg("更改用户密码成功，新密码为：" + password + "，请牢记或及时更改！");
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(userIds)) {
            usersService.deleteById(SmallPropsUtil.StringIdsToStringList(userIds));
            ajaxUtil.success().msg("删除用户成功");
        } else {
            ajaxUtil.fail().msg("用户账号不能为空");
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (NumberUtils.isDigits(dynamicPassword)) {
            Optional<GoogleOauthRecord> googleOauthRecord = googleOauthService.findByUsername(username);
            if (googleOauthRecord.isPresent()) {
                if (GoogleOauthUtil.validCode(googleOauthRecord.get().getGoogleOauthKey(), NumberUtils.toInt(dynamicPassword))) {
                    session.setAttribute(username + SessionBook.DYNAMIC_PASSWORD_VALID, true);
                    session.setAttribute(SessionBook.DYNAMIC_PASSWORD_USERNAME, username);
                    ajaxUtil.success().msg("验证通过");
                } else {
                    ajaxUtil.fail().msg("动态密码错误");
                }
            } else {
                ajaxUtil.fail().msg("您未开启双因素认证");
            }
        } else {
            ajaxUtil.fail().msg("动态密码错误，非数字");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 账号校验
     *
     * @param username 账号
     * @return 是否正常
     */
    private AjaxUtil<Map<String, Object>> checkUsername(String username) {
        String param = StringUtils.deleteWhitespace(username);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 禁止注册系统账号
        SystemConfigure systemConfigure = systemConfigureService
                .findByDataKey(Workbook.SystemConfigure.FORBIDDEN_REGISTER.name());
        String[] forbiddenRegister = systemConfigure.getDataValue().split(",");
        boolean isForbidden = false;
        for (String fr : forbiddenRegister) {
            if (fr.equalsIgnoreCase(param)) {
                isForbidden = true;
                break;
            }
        }
        // 只能是英文或数字
        String regex = Workbook.USERNAME_REGEX;
        if (isForbidden) {
            ajaxUtil.fail().msg("账号已被注册");
        } else if (!Pattern.matches(regex, username)) {
            ajaxUtil.fail().msg("账号1~20位英文或数字");
        } else {
            ajaxUtil.success();
        }
        return ajaxUtil;
    }
}
