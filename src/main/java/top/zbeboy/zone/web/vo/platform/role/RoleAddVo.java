package top.zbeboy.zone.web.vo.platform.role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoleAddVo {
    @NotBlank(message = "角色名不能为空")
    @Size(max = 50, message = "角色名50个字符以内")
    private String roleName;
    private int collegeId;
    @NotBlank(message = "应用ID不能为空")
    private String applicationIds;
    @NotBlank(message = "当前用户账号不能为空")
    @Size(max = 64, message = "当前用户账号不正确")
    private String username;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public String getApplicationIds() {
        return applicationIds;
    }

    public void setApplicationIds(String applicationIds) {
        this.applicationIds = applicationIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
