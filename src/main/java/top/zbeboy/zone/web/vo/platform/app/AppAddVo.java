package top.zbeboy.zone.web.vo.platform.app;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AppAddVo {
    @NotBlank(message = "ID不能为空")
    @Size(max = 255, message = "ID 255个字符以内")
    private String clientId;
    @NotBlank(message = "KEY不能为空")
    @Size(max = 300, message = "KEY 300个字符以内")
    private String secret;
    @NotBlank(message = "应用名不能为空")
    @Size(max = 100, message = "应用名100个字符以内")
    private String appName;
    @NotBlank(message = "回调地址不能为空")
    @Size(max = 255, message = "回调地址255个字符以内")
    private String webServerRedirectUri;
    private String remark;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
