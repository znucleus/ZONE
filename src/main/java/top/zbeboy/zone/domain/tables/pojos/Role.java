/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Role implements Serializable {

    private static final long serialVersionUID = 38441527;

    private String  roleId;
    private String  roleName;
    private String  roleEnName;
    private Integer roleType;

    public Role() {}

    public Role(Role value) {
        this.roleId = value.roleId;
        this.roleName = value.roleName;
        this.roleEnName = value.roleEnName;
        this.roleType = value.roleType;
    }

    public Role(
        String  roleId,
        String  roleName,
        String  roleEnName,
        Integer roleType
    ) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleEnName = roleEnName;
        this.roleType = roleType;
    }

    @NotNull
    @Size(max = 64)
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @NotNull
    @Size(max = 50)
    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @NotNull
    @Size(max = 64)
    public String getRoleEnName() {
        return this.roleEnName;
    }

    public void setRoleEnName(String roleEnName) {
        this.roleEnName = roleEnName;
    }

    @NotNull
    public Integer getRoleType() {
        return this.roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Role (");

        sb.append(roleId);
        sb.append(", ").append(roleName);
        sb.append(", ").append(roleEnName);
        sb.append(", ").append(roleType);

        sb.append(")");
        return sb.toString();
    }
}
