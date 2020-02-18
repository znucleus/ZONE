package top.zbeboy.zone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统属性配置
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
@ConfigurationProperties(prefix = "zone", ignoreUnknownFields = false)
public class ZoneProperties {

    private Async async = new Async();

    private Constants constants = new Constants();

    private Mobile mobile = new Mobile();

    private Mail mail = new Mail();

    private WeiXin weiXin = new WeiXin();

    public Async getAsync() {
        return async;
    }

    public Constants getConstants() {
        return constants;
    }

    public Mobile getMobile() {
        return mobile;
    }

    public Mail getMail() {
        return mail;
    }

    public WeiXin getWeiXin() {
        return weiXin;
    }

    /**
     * 异常初始化参数
     */
    public class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    /**
     * 通用初始化参数
     */
    public class Constants {

        private String webRoot;

        private String jooqDialect;

        public String getWebRoot() {
            return webRoot;
        }

        public void setWebRoot(String webRoot) {
            this.webRoot = webRoot;
        }

        public String getJooqDialect() {
            return jooqDialect;
        }

        public void setJooqDialect(String jooqDialect) {
            this.jooqDialect = jooqDialect;
        }
    }

    /**
     * 短信参数
     */
    public class Mobile {

        private String url;

        private String userId;

        private String account;

        private String password;

        private String sign;

        private int validCodeTime;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getValidCodeTime() {
            return validCodeTime;
        }

        public void setValidCodeTime(int validCodeTime) {
            this.validCodeTime = validCodeTime;
        }
    }

    /**
     * 邮箱参数
     */
    public class Mail {

        private int sendMethod;

        private String mailFrom;

        private String user;

        private String apiUser;

        private String apiKey;

        private String fromName;

        private int validCodeTime;

        private int passwordResetTime;

        public int getSendMethod() {
            return sendMethod;
        }

        public void setSendMethod(int sendMethod) {
            this.sendMethod = sendMethod;
        }

        public String getMailFrom() {
            return mailFrom;
        }

        public void setMailFrom(String mailFrom) {
            this.mailFrom = mailFrom;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getApiUser() {
            return apiUser;
        }

        public void setApiUser(String apiUser) {
            this.apiUser = apiUser;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public int getValidCodeTime() {
            return validCodeTime;
        }

        public void setValidCodeTime(int validCodeTime) {
            this.validCodeTime = validCodeTime;
        }

        public int getPasswordResetTime() {
            return passwordResetTime;
        }

        public void setPasswordResetTime(int passwordResetTime) {
            this.passwordResetTime = passwordResetTime;
        }
    }

    /**
     * 微信参数
     */
    public class WeiXin {
        private String appId;
        private String secret;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
}
