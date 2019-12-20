package top.zbeboy.zone.web.bean.platform.authorize;

import top.zbeboy.zone.domain.tables.pojos.RoleApply;

public class RoleApplyBean extends RoleApply {
    private String authorizeTypeName;
    private String organizeName;
    private String roleName;
    private String realName;

    public String getAuthorizeTypeName() {
        return authorizeTypeName;
    }

    public void setAuthorizeTypeName(String authorizeTypeName) {
        this.authorizeTypeName = authorizeTypeName;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
