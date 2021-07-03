package top.zbeboy.zone.web.platform.users;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
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
import top.zbeboy.zbase.config.WeiXinAppBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.WeiXinSubscribeService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.MD5Util;
import top.zbeboy.zbase.tools.web.util.QRCodeUtil;
import top.zbeboy.zbase.tools.web.util.SmallPropsUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.weixin.WeiXinSubscribeSendVo;
import top.zbeboy.zbase.vo.platform.user.ResetPasswordVo;
import top.zbeboy.zbase.vo.platform.user.UsersProfileVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.platform.common.PlatformControllerCommon;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.BaseImgUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class UsersRestController {

    private final Logger log = LoggerFactory.getLogger(UsersRestController.class);

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;

    @Resource
    private PlatformControllerCommon platformControllerCommon;

    /**
     * 检验账号是否被注册
     *
     * @param username 账号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check-username")
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
    @PostMapping("/anyone/check-exist-username")
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
    @PostMapping("/anyone/check-email")
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
    @PostMapping("/anyone/check-mobile")
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
    @PostMapping("/users/check-password")
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
    @PostMapping("/users/check-status")
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
    @PostMapping("/users/check-mobile")
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
    @PostMapping("/users/check-email")
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
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid ResetPasswordVo resetPasswordVo, BindingResult bindingResult, HttpSession session) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (resetPasswordVo.getPassword().equals(resetPasswordVo.getOkPassword())) {
                Optional<Users> result = usersService.findByUsername(resetPasswordVo.getUsername());
                if (result.isPresent()) {
                    Users users = result.get();
                    // 检验是否已通过验证
                    boolean isValid;
                    if (resetPasswordVo.getVerificationMode() == 0) {
                        LocalDateTime passwordResetKeyValid = users.getPasswordResetKeyValid();
                        LocalDateTime now = DateTimeUtil.getNowLocalDateTime();
                        isValid = now.isBefore(passwordResetKeyValid) && resetPasswordVo.getPasswordResetKey().equals(users.getPasswordResetKey());
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
            Users own = SessionUtil.getUserFromSession();
            ajaxUtil = platformControllerCommon.usersUpdate(usersProfileVo, own, session, request, Workbook.channel.WEB.name());
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
    @PostMapping("/users/open-google-oauth")
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
    @PostMapping("/users/close-google-oauth")
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
            Files files = BaseImgUtil.generateImage(file, fileName, request, Workbook.avatarPath(users.getUsername()), 500, 500, 0.5f);
            filesService.save(files);

            String avatar = users.getAvatar();
            users.setAvatar(files.getFileId());
            usersService.update(users);
            if (!StringUtils.equals(avatar, Workbook.USERS_AVATAR)) {
                Optional<Files> optionalFiles = filesService.findById(avatar);
                if (optionalFiles.isPresent()) {
                    Files oldFiles = optionalFiles.get();
                    // delete file.
                    FilesUtil.deleteFile(RequestUtil.getRealPath(request) + oldFiles.getRelativePath());
                    filesService.delete(oldFiles);
                }
            }

            String path = Workbook.qrCodePath() + MD5Util.getMD5(users.getUsername() + avatar) + ".jpg";
            FilesUtil.deleteFile(RequestUtil.getRealPath(request) + path);

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
                Optional<Files> optionalFiles = filesService.findById(avatar);
                if (optionalFiles.isPresent()) {
                    Files files = optionalFiles.get();
                    // delete file.
                    FilesUtil.deleteFile(RequestUtil.getRealPath(request) + files.getRelativePath());
                    filesService.delete(files);
                }
            }

            String path = Workbook.qrCodePath() + MD5Util.getMD5(users.getUsername() + avatar) + ".jpg";
            FilesUtil.deleteFile(RequestUtil.getRealPath(request) + path);

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
    @GetMapping("/web/platform/users/paging")
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
        List<Role> roles = new ArrayList<>();
        Optional<List<Role>> optionalRoles = usersService.roleData(users.getUsername(), username);
        if (optionalRoles.isPresent()) {
            roles = optionalRoles.get();
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.roleSave(users.getUsername(), username, roles);

        if (ajaxUtil.getState()) {
            Optional<Users> result = usersService.findByUsername(username);
            String notify = "您的权限已发生变更，请登录查看。";
            // 检查邮件推送是否被关闭
            Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
            if (optionalSystemConfigure.isPresent() && StringUtils.equals("1", optionalSystemConfigure.get().getDataValue())) {
                result.ifPresent(value -> systemMailService.sendNotifyMail(value, RequestUtil.getBaseUrl(request), notify));
            }

            // 微信订阅通知
            if (result.isPresent()) {
                WeiXinSubscribeSendVo weiXinSubscribeSendVo = new WeiXinSubscribeSendVo();
                weiXinSubscribeSendVo.setUsername(username);
                weiXinSubscribeSendVo.setBusiness(WeiXinAppBook.subscribeBusiness.REGISTRATION_REVIEW_RESULT.name());
                weiXinSubscribeSendVo.setThing1("审核通过");
                weiXinSubscribeSendVo.setName4(result.get().getRealName());
                weiXinSubscribeSendVo.setDate2(DateTimeUtil.getNowLocalDateTime(DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                weiXinSubscribeSendVo.setThing3(notify);
                weiXinSubscribeSendVo.setStartTime(DateTimeUtil.getNowLocalDateTime());
                weiXinSubscribeService.sendByBusinessAndUsername(weiXinSubscribeSendVo);
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
    public ResponseEntity<Map<String, Object>> delete(String userIds, HttpServletRequest request) {
        if (StringUtils.isNotBlank(userIds)) {
            List<String> usersList = SmallPropsUtil.StringIdsToStringList(userIds);
            String realPath = RequestUtil.getRealPath(request);
            for (String user : usersList) {
                Optional<Users> result = usersService.findByUsername(user);
                String notify = "您因不满足审核条件，注册信息已被删除。";
                // 微信订阅通知
                if (result.isPresent()) {
                    Users users = result.get();
                    WeiXinSubscribeSendVo weiXinSubscribeSendVo = new WeiXinSubscribeSendVo();
                    weiXinSubscribeSendVo.setUsername(users.getUsername());
                    weiXinSubscribeSendVo.setBusiness(WeiXinAppBook.subscribeBusiness.REGISTRATION_REVIEW_RESULT.name());
                    weiXinSubscribeSendVo.setThing1("审核未通过");
                    weiXinSubscribeSendVo.setName4(result.get().getRealName());
                    weiXinSubscribeSendVo.setDate2(DateTimeUtil.getNowLocalDateTime(DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                    weiXinSubscribeSendVo.setThing3(notify);
                    weiXinSubscribeSendVo.setStartTime(DateTimeUtil.getNowLocalDateTime());
                    weiXinSubscribeService.sendByBusinessAndUsername(weiXinSubscribeSendVo);

                    String path = Workbook.qrCodePath() + MD5Util.getMD5(users.getUsername() + users.getAvatar()) + ".jpg";
                    FilesUtil.deleteFile(realPath + path);
                }


            }
        }

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
    @PostMapping("/forget-password/dynamic-password")
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestParam("username") String username,
                                                              @RequestParam("dynamicPassword") String dynamicPassword, HttpSession session) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.forgetPassword(username, dynamicPassword);
        if (ajaxUtil.getState()) {
            session.setAttribute(username + SessionBook.DYNAMIC_PASSWORD_VALID, true);
            session.setAttribute(SessionBook.DYNAMIC_PASSWORD_USERNAME, username);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 用户个人二维码信息
     *
     * @param channel  渠道
     * @param request  请求
     */
    @GetMapping(value = {"/users/qr_code", "/api/platform/users/qr_code"})
    public ResponseEntity<Map<String, Object>> qrCode(String channel, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserByChannel(channel, principal);
            if (Objects.nonNull(users)) {
                String realPath = RequestUtil.getRealPath(request);
                String path = Workbook.qrCodePath() + MD5Util.getMD5(users.getUsername() + users.getAvatar()) + ".jpg";
                File file = new File(realPath + path);
                if (!file.exists()) {
                    String logoPath = "";
                    if (!StringUtils.equals(users.getAvatar(), Workbook.USERS_AVATAR)) {
                        Optional<Files> optionalFiles = filesService.findById(users.getAvatar());
                        if (optionalFiles.isPresent()) {
                            Files files = optionalFiles.get();
                            logoPath = RequestUtil.getRealPath(request) + files.getRelativePath();
                        }
                    }

                    Map<String, Object> info = new HashMap<>();
                    info.put("username", users.getUsername());
                    info.put("usersTypeId", users.getUsersTypeId());

                    //生成二维码
                    String text = JSON.toJSONString(info);
                    QRCodeUtil.encode(text, StringUtils.isBlank(logoPath) ? Workbook.SYSTEM_LOGO_PATH : logoPath, realPath + path, true);
                }
                ajaxUtil.success().msg("获取成功").put("path", path);
            } else {
                ajaxUtil.fail().msg("获取用户信息为空");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("生成失败: 异常: " + e.getMessage());
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
