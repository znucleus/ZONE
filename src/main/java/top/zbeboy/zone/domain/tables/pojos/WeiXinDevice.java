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
public class WeiXinDevice implements Serializable {

    private static final long serialVersionUID = 1783695521;

    private String    deviceId;
    private String    brand;
    private String    model;
    private String    version;
    private Double    screenWidth;
    private Double    screenHeight;
    private String    systemInfo;
    private String    platform;
    private Byte      locationAuthorized;
    private Byte      notificationAuthorized;
    private String    username;
    private Timestamp createDate;

    public WeiXinDevice() {}

    public WeiXinDevice(WeiXinDevice value) {
        this.deviceId = value.deviceId;
        this.brand = value.brand;
        this.model = value.model;
        this.version = value.version;
        this.screenWidth = value.screenWidth;
        this.screenHeight = value.screenHeight;
        this.systemInfo = value.systemInfo;
        this.platform = value.platform;
        this.locationAuthorized = value.locationAuthorized;
        this.notificationAuthorized = value.notificationAuthorized;
        this.username = value.username;
        this.createDate = value.createDate;
    }

    public WeiXinDevice(
        String    deviceId,
        String    brand,
        String    model,
        String    version,
        Double    screenWidth,
        Double    screenHeight,
        String    systemInfo,
        String    platform,
        Byte      locationAuthorized,
        Byte      notificationAuthorized,
        String    username,
        Timestamp createDate
    ) {
        this.deviceId = deviceId;
        this.brand = brand;
        this.model = model;
        this.version = version;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.systemInfo = systemInfo;
        this.platform = platform;
        this.locationAuthorized = locationAuthorized;
        this.notificationAuthorized = notificationAuthorized;
        this.username = username;
        this.createDate = createDate;
    }

    @NotNull
    @Size(max = 64)
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Size(max = 100)
    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Size(max = 100)
    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Size(max = 30)
    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Double getScreenWidth() {
        return this.screenWidth;
    }

    public void setScreenWidth(Double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Double getScreenHeight() {
        return this.screenHeight;
    }

    public void setScreenHeight(Double screenHeight) {
        this.screenHeight = screenHeight;
    }

    @Size(max = 100)
    public String getSystemInfo() {
        return this.systemInfo;
    }

    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }

    @Size(max = 30)
    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Byte getLocationAuthorized() {
        return this.locationAuthorized;
    }

    public void setLocationAuthorized(Byte locationAuthorized) {
        this.locationAuthorized = locationAuthorized;
    }

    public Byte getNotificationAuthorized() {
        return this.notificationAuthorized;
    }

    public void setNotificationAuthorized(Byte notificationAuthorized) {
        this.notificationAuthorized = notificationAuthorized;
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
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("WeiXinDevice (");

        sb.append(deviceId);
        sb.append(", ").append(brand);
        sb.append(", ").append(model);
        sb.append(", ").append(version);
        sb.append(", ").append(screenWidth);
        sb.append(", ").append(screenHeight);
        sb.append(", ").append(systemInfo);
        sb.append(", ").append(platform);
        sb.append(", ").append(locationAuthorized);
        sb.append(", ").append(notificationAuthorized);
        sb.append(", ").append(username);
        sb.append(", ").append(createDate);

        sb.append(")");
        return sb.toString();
    }
}
