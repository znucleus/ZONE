package top.zbeboy.zone.web.vo.register.epidemic;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EpidemicRegisterDataAddVo {
    @NotBlank(message = "发布ID不能为空")
    @Size(max = 64, message = "发布ID不正确")
    private String    epidemicRegisterReleaseId;
    private String    location;
    @NotBlank(message = "当前位置不能为空")
    @Size(max = 300, message = "当前位置300个字符以内")
    private String    address;
    @NotBlank(message = "身体状况不能为空")
    private String    epidemicStatus;
    private String    remark;

    public String getEpidemicRegisterReleaseId() {
        return epidemicRegisterReleaseId;
    }

    public void setEpidemicRegisterReleaseId(String epidemicRegisterReleaseId) {
        this.epidemicRegisterReleaseId = epidemicRegisterReleaseId;
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

    public String getEpidemicStatus() {
        return epidemicStatus;
    }

    public void setEpidemicStatus(String epidemicStatus) {
        this.epidemicStatus = epidemicStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
