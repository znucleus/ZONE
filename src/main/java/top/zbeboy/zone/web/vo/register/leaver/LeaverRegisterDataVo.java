package top.zbeboy.zone.web.vo.register.leaver;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LeaverRegisterDataVo {
    @NotBlank(message = "离校登记发布ID不能为空")
    @Size(max = 64, message = "离校登记发布ID不正确")
    private String leaverRegisterReleaseId;
    @NotBlank(message = "离校活动地点不能为空")
    @Size(max = 300, message = "离校活动地点300个字符以内")
    private String leaverAddress;
    @NotNull(message = "离校选项不能为空")
    private String[] leaverRegisterOptionId;
    private String remark;

    public String getLeaverRegisterReleaseId() {
        return leaverRegisterReleaseId;
    }

    public void setLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        this.leaverRegisterReleaseId = leaverRegisterReleaseId;
    }

    public String getLeaverAddress() {
        return leaverAddress;
    }

    public void setLeaverAddress(String leaverAddress) {
        this.leaverAddress = leaverAddress;
    }

    public String[] getLeaverRegisterOptionId() {
        return leaverRegisterOptionId;
    }

    public void setLeaverRegisterOptionId(String[] leaverRegisterOptionId) {
        this.leaverRegisterOptionId = leaverRegisterOptionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
