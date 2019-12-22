package top.zbeboy.zone.web.vo.platform.authorize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthorizeAddVo {
    @Size(max = 64, message = "账号64个字符以内")
    private String username;
    @NotNull(message = "权限类型不能为空")
    @Min(value = 1, message = "权限类型ID最小值为1")
    private int authorizeTypeId;
    @NotBlank(message = "角色ID不能为空")
    @Size(max = 64, message = "角色ID64个字符以内")
    private String roleId;
    @NotNull(message = "时长不能为空")
    @Min(value = 1, message = "时长最小值为1")
    private int duration;
    @NotBlank(message = "生效时间不能为空")
    private String validDate;
    @NotNull(message = "数据域不能为空")
    @Min(value = 1, message = "数据域最小值为1")
    private int dataScope;
    @NotNull(message = "数据ID不能为空")
    @Min(value = 1, message = "数据ID最小值为1")
    private int dataId;
    @NotBlank(message = "申请原因不能为空")
    @Size(max = 200, message = "申请原因200个字符以内")
    private String reason;
    @NotNull(message = "院ID不能为空")
    @Min(value = 1, message = "院ID最小值为1")
    private int collegeId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAuthorizeTypeId() {
        return authorizeTypeId;
    }

    public void setAuthorizeTypeId(int authorizeTypeId) {
        this.authorizeTypeId = authorizeTypeId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public int getDataScope() {
        return dataScope;
    }

    public void setDataScope(int dataScope) {
        this.dataScope = dataScope;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }
}
