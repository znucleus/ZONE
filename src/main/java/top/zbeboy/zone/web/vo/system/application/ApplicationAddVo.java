package top.zbeboy.zone.web.vo.system.application;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ApplicationAddVo {
    private String applicationId;
    @NotBlank
    @Size(max = 30)
    private String applicationName;
    private int applicationSort;
    @NotBlank
    private String applicationPid;
    @NotBlank
    @Size(max = 300)
    private String applicationUrl;
    @NotBlank
    @Size(max = 100)
    private String applicationCode;
    @NotBlank
    @Size(max = 100)
    private String applicationEnName;
    @Size(max = 20)
    private String icon;
    @Size(max = 300)
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
