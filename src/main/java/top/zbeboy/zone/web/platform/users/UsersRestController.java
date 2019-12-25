package top.zbeboy.zone.web.platform.users;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jooq.Record;
import org.jooq.Record11;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.UsersRecord;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.*;
import top.zbeboy.zone.web.bean.platform.users.UsersBean;
import top.zbeboy.zone.web.bean.system.application.ApplicationBean;
import top.zbeboy.zone.web.system.mail.SystemMailConfig;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BaseImgUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.user.ResetPasswordVo;
import top.zbeboy.zone.web.vo.platform.user.UsersProfileVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
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
        String param = StringUtils.deleteWhitespace(username);
        AjaxUtil<Map<String, Object>> ajaxUtil = checkUsername(username);
        if (BooleanUtils.isTrue(ajaxUtil.getState())) {
            UsersRecord users = usersService.findByUsernameUpper(param);
            if (!ObjectUtils.isEmpty(users)) {
                ajaxUtil.fail().msg("账号已被注册");
            } else {
                ajaxUtil.success().msg("账号未被注册");
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
    @PostMapping("/anyone/check/email")
    public ResponseEntity<Map<String, Object>> anyoneCheckEmail(@RequestParam("email") String email) {
        String param = StringUtils.deleteWhitespace(email);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = SystemMailConfig.MAIL_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("邮箱格式不正确");
        } else {
            Users users = usersService.findByEmail(param);
            if (!ObjectUtils.isEmpty(users)) {
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
            if (!ObjectUtils.isEmpty(users)) {
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
                    } else {
                        String mobile = users.getMobile();
                        isValid = !ObjectUtils.isEmpty(session.getAttribute(mobile + SystemMobileConfig.MOBILE_VALID)) &&
                                (boolean) session.getAttribute(mobile + SystemMobileConfig.MOBILE_VALID);
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
            if (StringUtils.equals("username", name)) {
                ajaxUtil = checkUsername(value);
                if (BooleanUtils.isTrue(ajaxUtil.getState())) {
                    Users own = usersService.getUserFromSession();
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
            }

            if (StringUtils.equals("realName", name)) {
                Users users = usersService.getUserFromSession();
                if (!StringUtils.equals(users.getRealName(), value)) {
                    users.setRealName(value);
                    usersService.update(users);
                    ajaxUtil.success().msg("姓名更新成功");
                } else {
                    ajaxUtil.fail().msg("姓名未改变");
                }
            }

            if (StringUtils.equals("email", name)) {
                if (Pattern.matches(SystemMailConfig.MAIL_REGEX, value)) {
                    Users own = usersService.getUserFromSession();
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

            if (StringUtils.equals("mobile", name)) {
                if (Pattern.matches(SystemMobileConfig.MOBILE_REGEX, value)) {
                    Users own = usersService.getUserFromSession();
                    if (!StringUtils.equals(own.getMobile(), value)) {
                        Result<UsersRecord> usersRecords = usersService.findByEmailNeOwn(value, own.getMobile());
                        if (usersRecords.isEmpty()) {
                            // step 2.手机号是否已验证
                            if (!ObjectUtils.isEmpty(session.getAttribute(value + SystemMobileConfig.MOBILE_VALID))) {
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

            if (StringUtils.equals("idCard", name)) {
                if (Pattern.matches(Workbook.ID_CARD_REGEX, value)) {
                    Users users = usersService.getUserFromSession();
                    if (!StringUtils.equals(users.getIdCard(), value)) {
                        // 检查是否已经存在该身份证号
                        Result<UsersRecord> records = usersService.findByIdCardNeOwn(value, users.getUsername());
                        if (records.isEmpty()) {
                            users.setIdCard(value);
                            usersService.update(users);
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
            }

        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
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
                ajaxUtil.fail().msg("密码为6-16位任意字母或数字，以及下划线");
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
        Result<Record11<String, String, String, String, String, String, String, Byte, Byte, String, Date>> records = usersService.findAllByPage(dataTablesUtil);
        List<UsersBean> usersBean = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            usersBean = records.into(UsersBean.class);
        }
        dataTablesUtil.setData(usersBean);
        dataTablesUtil.setiTotalRecords(usersService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(usersService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
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
