package top.zbeboy.zone.web.vo.system.notify;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SystemNotifyEditVo {
    @NotBlank(message = "通知ID不能为空")
    @Size(max = 64, message = "通知ID不正确")
    private String systemNotifyId;
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String notifyTitle;
    @NotBlank(message = "内容不能为空")
    @Size(max = 500, message = "内容500个字符以内")
    private String notifyContent;
    @NotBlank(message = "生效时间不能为空")
    private String validDate;
    @NotBlank(message = "失效时间不能为空")
    private String expireDate;
    @NotBlank(message = "类型不能为空")
    private String notifyType;

    public String getSystemNotifyId() {
        return systemNotifyId;
    }

    public void setSystemNotifyId(String systemNotifyId) {
        this.systemNotifyId = systemNotifyId;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }
}