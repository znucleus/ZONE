/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OauthClientUsers implements Serializable {

    private static final long serialVersionUID = -704388805;

    private String    clientId;
    private String    username;
    private String    appName;
    private String    secret;
    private String    remark;
    private Timestamp createDate;

    public OauthClientUsers() {}

    public OauthClientUsers(OauthClientUsers value) {
        this.clientId = value.clientId;
        this.username = value.username;
        this.appName = value.appName;
        this.secret = value.secret;
        this.remark = value.remark;
        this.createDate = value.createDate;
    }

    public OauthClientUsers(
        String    clientId,
        String    username,
        String    appName,
        String    secret,
        String    remark,
        Timestamp createDate
    ) {
        this.clientId = clientId;
        this.username = username;
        this.appName = appName;
        this.secret = secret;
        this.remark = remark;
        this.createDate = createDate;
    }

    @NotNull
    @Size(max = 255)
    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Size(max = 100)
    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @NotNull
    @Size(max = 300)
    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Size(max = 300)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @NotNull
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("OauthClientUsers (");

        sb.append(clientId);
        sb.append(", ").append(username);
        sb.append(", ").append(appName);
        sb.append(", ").append(secret);
        sb.append(", ").append(remark);
        sb.append(", ").append(createDate);

        sb.append(")");
        return sb.toString();
    }
}
