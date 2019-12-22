package top.zbeboy.zone.service.system;

import io.jstack.sendcloud4j.SendCloud;
import io.jstack.sendcloud4j.mail.Email;
import io.jstack.sendcloud4j.mail.GeneralEmail;
import io.jstack.sendcloud4j.mail.Result;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.domain.tables.pojos.SystemMailboxLog;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.system.mail.SystemMailConfig;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service("systemMailService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemMailServiceImpl implements SystemMailService {

    private final Logger log = LoggerFactory.getLogger(SystemMailServiceImpl.class);

    @Autowired
    private ZoneProperties ZoneProperties;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Resource
    private MessageSource messageSource;

    @Resource
    private SpringTemplateEngine springTemplateEngine;

    @Resource
    private SystemMailboxLogService systemMailboxLogService;

    @Async
    @Override
    public void sendEmail(String to, String subject, String content, Boolean isMultipart, Boolean isHtml) {
        SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
        if (StringUtils.equals("0", systemConfigure.getDataValue())) {
            log.info(" 管理员已关闭邮件发送 ");
            return;
        }

        switch (ZoneProperties.getMail().getSendMethod()) {
            case 1:
                sendDefaultMail(to, subject, content, isMultipart, isHtml);
                log.info("使用默认邮件服务发送");
                break;
            case 2:
                sendCloudMail(to, subject, content);
                log.info("使用sendCloud邮件服务发送");
                break;
            default:
                log.info("未配置邮箱发送方式");
        }
    }

    @Async
    @Override
    public void sendActivationEmail(Users users, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", users.getUsername());
        Locale locale = Locale.forLanguageTag(users.getLangKey());
        Context data = new Context();
        data.setLocale(locale);
        data.setVariable("user", users);
        data.setVariable("baseUrl", baseUrl);
        sendEmail(users.getEmail(), messageSource.getMessage("email.activation.title", null, locale), springTemplateEngine.process("mails/activation_email", data), false, true);
    }

    @Async
    @Override
    public void sendCreationEmail(Users users, String baseUrl) {
        log.debug("Sending creation e-mail to '{}'", users.getUsername());
        Locale locale = Locale.forLanguageTag(users.getLangKey());
        Context data = new Context();
        data.setLocale(locale);
        data.setVariable("user", users);
        data.setVariable("baseUrl", baseUrl);
        sendEmail(users.getEmail(), messageSource.getMessage("email.creation.title", null, locale), springTemplateEngine.process("mails/creation_email", data), false, true);
    }

    @Async
    @Override
    public void sendPasswordResetMail(Users users, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", users.getUsername());
        Locale locale = Locale.forLanguageTag(users.getLangKey());
        Context data = new Context();
        data.setLocale(locale);
        data.setVariable("user", users);
        data.setVariable("resetLink", baseUrl + SystemMailConfig.WEB_RESET_PASSWORD_MAIL + "/" + users.getPasswordResetKey() + "/" + users.getUsername());
        data.setVariable("baseUrl", baseUrl);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        String content = springTemplateEngine.process("mails/password_reset_email", data);
        sendEmail(users.getEmail(), subject, content, false, true);
    }

    @Async
    @Override
    public void sendValidEmailMail(Users users, String baseUrl) {
        log.debug("Sending valid e-mail to '{}'", users.getUsername());
        Locale locale = Locale.forLanguageTag(users.getLangKey());
        Context data = new Context();
        data.setLocale(locale);
        data.setVariable("user", users);
        data.setVariable("validLink", baseUrl + SystemMailConfig.WEB_VALID_EMAIL + "/" + users.getMailboxVerifyCode() + "/" + users.getUsername());
        data.setVariable("baseUrl", baseUrl);
        String subject = messageSource.getMessage("email.valid.title", null, locale);
        String content = springTemplateEngine.process("mails/valid_email", data);
        sendEmail(users.getEmail(), subject, content, false, true);
    }

    @Async
    @Override
    public void sendNotifyMail(Users users, String baseUrl, String notify) {
        log.debug("Sending notify e-mail to '{}'", users.getUsername());
        Locale locale = Locale.forLanguageTag(users.getLangKey());
        Context data = new Context();
        data.setLocale(locale);
        data.setVariable("user", users);
        data.setVariable("loginLink", baseUrl + "/login");
        data.setVariable("notify", notify);
        data.setVariable("baseUrl", baseUrl);
        String subject = messageSource.getMessage("email.notify.title", null, locale);
        String content = springTemplateEngine.process("mails/notify_email", data);
        sendEmail(users.getEmail(), subject, content, false, true);
    }

    @Async
    @Override
    public void sendDefaultMail(String to, String subject, String content, Boolean isMultipart, Boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);
        String sendCondition;
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(ZoneProperties.getMail().getMailFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
            sendCondition = "方式:默认邮箱, 发送成功";
        } catch (Exception e) {
            log.error("E-mail could not be sent to user '{}', exception is: {}", to, e);
            sendCondition = "方式:默认邮箱, 发送失败 " + e.getMessage();
        }

        SystemMailboxLog systemMailboxLog = new SystemMailboxLog();
        systemMailboxLog.setLogId(UUIDUtil.getUUID());
        systemMailboxLog.setSendTime(DateTimeUtil.getNowSqlTimestamp());
        systemMailboxLog.setAcceptMail(to);
        systemMailboxLog.setSendCondition(sendCondition);
        systemMailboxLogService.save(systemMailboxLog);
    }

    @Async
    @Override
    public void sendCloudMail(String userMail, String subject, String content) {
        SendCloud webApi = SendCloud.createWebApi(ZoneProperties.getMail().getApiUser(), ZoneProperties.getMail().getApiKey());
        GeneralEmail email = Email.general()
                .from(ZoneProperties.getMail().getUser())
                .fromName(ZoneProperties.getMail().getFromName())
                .html(content)          // or .plain()
                .subject(subject)
                .to(userMail);
        Result result = webApi.mail().send(email);
        String sendCondition;
        if (result.isSuccess()) {
            sendCondition = "方式:sendCloud邮箱, 发送成功 " + result.getStatusCode() + " : " + result.getMessage();
        } else {
            sendCondition = "方式:sendCloud邮箱, 发送失败 " + result.getStatusCode() + " : " + result.getMessage();
        }

        SystemMailboxLog systemMailboxLog = new SystemMailboxLog();
        systemMailboxLog.setLogId(UUIDUtil.getUUID());
        systemMailboxLog.setSendTime(DateTimeUtil.getNowSqlTimestamp());
        systemMailboxLog.setAcceptMail(userMail);
        systemMailboxLog.setSendCondition(sendCondition);
        systemMailboxLogService.save(systemMailboxLog);
    }
}
