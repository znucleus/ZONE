package top.zbeboy.zone.web.vo.system.application;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ApplicationEditVo {
    @NotBlank(message = "应用ID不能为空")
    @Size(max = 64, message = "应用ID64个字符以内")
    private String applicationId;
    @NotBlank(message = "应用名不能为空")
    @Size(max = 30, message = "应用名30个字符以内")
    private String applicationName;
    private int applicationSort;
    @NotBlank(message = "应用父ID不能为空")
    private String applicationPid;
    @NotBlank(message = "应用URL不能为空")
    @Size(max = 300, message = "应用URL300个字符以内")
    private String applicationUrl;
    @NotBlank(message = "应用识别名不能为空")
    @Size(max = 100, message = "应用识别名100个字符以内")
    private String applicationCode;
    @NotBlank(message = "应用英文名不能为空")
    @Size(max = 100, message = "应用英文名100个字符以内")
    private String applicationEnName;
    @Size(max = 20, message = "图标20个字符以内")
    private String icon;
    @Size(max = 300, message = "应用过滤链300个字符以内")
    private String applicationDataUrlStartWith;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getApplicationSort() {
        return applicationSort;
    }

    public void setApplicationSort(int applicationSort) {
        this.applicationSort = applicationSort;
    }

    public String getApplicationPid() {
        return applicationPid;
    }

    public void setApplicationPid(String applicationPid) {
        this.applicationPid = applicationPid;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getApplicationEnName() {
        return applicationEnName;
    }

    public void setApplicationEnName(String applicationEnName) {
        this.applicationEnName = applicationEnName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getApplicationDataUrlStartWith() {
        return applicationDataUrlStartWith;
    }

    public void setApplicationDataUrlStartWith(String applicationDataUrlStartWith) {
        this.applicationDataUrlStartWith = applicationDataUrlStartWith;
    }
}
