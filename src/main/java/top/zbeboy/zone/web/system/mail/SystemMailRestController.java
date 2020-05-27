package top.zbeboy.zone.web.system.mail;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.system.SystemConfigureService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RandomUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
public class SystemMailRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    /**
     * 忘记密码邮箱提交验证
     *
     * @param email 邮箱
     * @return 是否验证通过
     */
    @PostMapping("/forget_password/mail")
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestParam("email") String email, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(email);
        if (StringUtils.isNotBlank(param)) {
            if (Pattern.matches(SystemMailConfig.MAIL_REGEX, param)) {
                Users users = usersService.findByEmail(param);
                if (Objects.nonNull(users)) {
                    if (Objects.isNull(users.getVerifyMailbox()) || !BooleanUtil.toBoolean(users.getVerifyMailbox())) {
                        ajaxUtil.fail().msg("邮箱未验证通过");
                    } else {
                        // 检查邮件推送是否被关闭
                        SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                        if ("1".equals(mailConfigure.getDataValue())) {
                            DateTime dateTime = DateTime.now();
                            dateTime = dateTime.plusDays(ZoneProperties.getMail().getPasswordResetTime());
                            users.setPasswordResetKey(RandomUtil.generateResetKey());
                            users.setPasswordResetKeyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                            usersService.update(users);
                            systemMailService.sendPasswordResetMail(users, RequestUtil.getBaseUrl(request));
                            ajaxUtil.success().msg("验证通过");
                        } else {
                            ajaxUtil.fail().msg("邮件推送已被管理员关闭");
                        }
                    }
                } else {
                    ajaxUtil.fail().msg("邮箱未注册");
                }
            } else {
                ajaxUtil.fail().msg("邮箱格式不正确");
            }
        } else {
            ajaxUtil.fail().msg("邮箱不能为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 发送邮件
     *
     * @param to          接收方
     * @param subject     标题
     * @param content     内容
     * @param isMultipart 多段
     * @param isHtml      是html?
     */
    @PostMapping("/anyone/system/mail/send")
    public void sendEmail(@RequestParam("to") String to, @RequestParam("subject") String subject,
                          @RequestParam("content") String content, @RequestParam("isMultipart") Boolean isMultipart, @RequestParam("isHtml") Boolean isHtml) {
        systemMailService.sendEmail(to, subject, content, isMultipart, isHtml);
    }

    /**
     * 发送激活邮件
     *
     * @param users   用户
     * @param baseUrl 服务路径
     */
    @PostMapping("/anyone/system/mail/activation")
    public void sendActivationEmail(@RequestBody Users users, @RequestParam("baseUrl") String baseUrl) {
        systemMailService.sendActivationEmail(users, baseUrl);
    }

    /**
     * 发送账号创建成功邮件
     *
     * @param users   用户
     * @param baseUrl 服务路径
     */
    @PostMapping("/anyone/system/mail/creation")
    public void sendCreationEmail(@RequestBody Users users, @RequestParam("baseUrl") String baseUrl) {
        systemMailService.sendCreationEmail(users, baseUrl);
    }

    /**
     * 发送密码重置邮件
     *
     * @param users   用户
     * @param baseUrl 服务路径
     */
    @PostMapping("/anyone/system/mail/reset")
    public void sendPasswordResetMail(@RequestBody Users users, @RequestParam("baseUrl") String baseUrl) {
        systemMailService.sendPasswordResetMail(users, baseUrl);
    }

    /**
     * 发送邮箱验证邮件
     *
     * @param users   用户
     * @param baseUrl 服务路径
     */
    @PostMapping("/anyone/system/mail/valid")
    public void sendValidEmailMail(@RequestBody Users users, @RequestParam("baseUrl") String baseUrl, HttpServletRequest request) {
        String ip = RequestUtil.getIpAddress(request);
        systemMailService.sendValidEmailMail(users, baseUrl);
    }

    /**
     * 发送通知邮件
     *
     * @param users   用户
     * @param baseUrl 服务路径
     * @param notify  通知内容
     */
    @PostMapping("/anyone/system/mail/notify")
    public void sendNotifyMail(@RequestBody Users users, @RequestParam("baseUrl") String baseUrl, @RequestParam("notify") String notify) {
        systemMailService.sendNotifyMail(users, baseUrl, notify);
    }

    /**
     * 使用内置方式发送
     *
     * @param to          接收方
     * @param subject     标题
     * @param content     内容
     * @param isMultipart 多段
     * @param isHtml      是html?
     */
    @PostMapping("/anyone/system/mail/send/default")
    public void sendDefaultMail(@RequestParam("to") String to, @RequestParam("subject") String subject,
                                @RequestParam("content") String content, @RequestParam("isMultipart") Boolean isMultipart, @RequestParam("isHtml") Boolean isHtml) {
        systemMailService.sendDefaultMail(to, subject, content, isMultipart, isHtml);
    }

    /**
     * sendCloud邮箱服务
     *
     * @param userMail 用户邮箱
     * @param subject  标题
     * @param content  内容
     */
    @PostMapping("/anyone/system/mail/send/cloud")
    public void sendCloudMail(@RequestParam("userMail") String userMail, @RequestParam("subject") String subject, @RequestParam("content") String content) {
        systemMailService.sendCloudMail(userMail, subject, content);
    }
}
