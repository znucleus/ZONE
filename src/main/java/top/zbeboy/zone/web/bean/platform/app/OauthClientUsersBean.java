package top.zbeboy.zone.web.bean.platform.app;

import top.zbeboy.zbase.domain.tables.pojos.OauthClientUsers;

public class OauthClientUsersBean extends OauthClientUsers {
    private String realName;
    private String webServerRedirectUri;
    private String createDateStr;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
