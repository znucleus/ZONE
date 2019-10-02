package top.zbeboy.zone.web.system.mail;

public class SystemMailConfig {
    public static final String WEB_VALID_EMAIL = "/anyone/valid/mail";
    static final String WEB_RESEND_MAIL = "/anyone/resend/valid/mail";
    public static final String WEB_RESET_PASSWORD_MAIL = "/anyone/reset_password/mail";
    public static final String MAIL_REGEX = "^\\w+((-\\w+)|(\\.\\w+))*@[A-Za-z0-9]+(([.\\-])[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
}
