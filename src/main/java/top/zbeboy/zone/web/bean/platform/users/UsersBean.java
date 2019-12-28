package top.zbeboy.zone.web.bean.platform.users;

import top.zbeboy.zone.domain.tables.pojos.Users;

public class UsersBean extends Users {
    private String roleName;
    private String usersTypeName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsersTypeName() {
        return usersTypeName;
    }

    public void setUsersTypeName(String usersTypeName) {
        this.usersTypeName = usersTypeName;
    }
}
