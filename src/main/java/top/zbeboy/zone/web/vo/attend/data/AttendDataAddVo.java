package top.zbeboy.zone.web.vo.attend.data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AttendDataAddVo {
    @NotNull(message = "签到发布子表ID不能为空")
    @Min(value = 1, message = "签到发布子表ID不正确")
    private int attendReleaseSubId;
    private String location;
    private String address;
    @Size(max = 100, message = "设备品牌100个字符以内")
    private String brand;
    @Size(max = 100, message = "设备机型100个字符以内")
    private String model;
    @Size(max = 30, message = "微信版本号30个字符以内")
    private String version;
    private Double screenWidth;
    private Double screenHeight;
    @Size(max = 100, message = "系统信息100个字符以内")
    private String systemInfo;
    @Size(max = 30, message = "系统平台30个字符以内")
    private String platform;
    private Byte locationAuthorized;
    private Byte notificationAuthorized;
    @NotBlank(message = "当前用户账号不能为空")
    @Size(max = 64, message = "当前用户账号不正确")
    private String username;

    public int getAttendReleaseSubId() {
        return attendReleaseSubId;
    }

    public void setAttendReleaseSubId(int attendReleaseSubId) {
        this.attendReleaseSubId = attendReleaseSubId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Byte getLocationAuthorized() {
        return locationAuthorized;
    }

    public void setLocationAuthorized(Byte locationAuthorized) {
        this.locationAuthorized = locationAuthorized;
    }

    public Byte getNotificationAuthorized() {
        return notificationAuthorized;
    }

    public void setNotificationAuthorized(Byte notificationAuthorized) {
        this.notificationAuthorized = notificationAuthorized;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
