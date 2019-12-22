package top.zbeboy.zone.web.vo.platform.authorize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthorizeEditVo {
    @NotBlank(message = "申请ID不能为空")
    @Size(max = 64, message = "申请ID64个字符以内")
    private String roleApplyId;
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
    @NotNull(message = "班级ID不能为空")
    @Min(value = 1, message = "班级ID最小值为1")
    private int organizeId;
    @NotBlank(message = "申请原因不能为空")
    @Size(max = 200, message = "申请原因200个字符以内")
    private String reason;
    @NotNull(message = "院ID不能为空")
    @Min(value = 1, message = "院ID最小值为1")
    private int collegeId;

    public String getRoleApplyId() {
        return roleApplyId;
    }

    public void setRoleApplyId(String roleApplyId) {
        this.roleApplyId = roleApplyId;
    }

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

    public int getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(int organizeId) {
        this.organizeId = organizeId;
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
