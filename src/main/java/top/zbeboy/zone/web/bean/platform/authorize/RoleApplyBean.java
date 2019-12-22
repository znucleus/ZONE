package top.zbeboy.zone.web.bean.platform.authorize;

import top.zbeboy.zone.domain.tables.pojos.RoleApply;

public class RoleApplyBean extends RoleApply {
    private String authorizeTypeName;
    private String dataName;
    private String roleName;
    private String realName;
    private String validDateStr;
    private String expireDateStr;
    private String createDateStr;
    private int durationInt;

    public String getAuthorizeTypeName() {
        return authorizeTypeName;
    }

    public void setAuthorizeTypeName(String authorizeTypeName) {
        this.authorizeTypeName = authorizeTypeName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getValidDateStr() {
        return validDateStr;
    }

    public void setValidDateStr(String validDateStr) {
        this.validDateStr = validDateStr;
    }

    public String getExpireDateStr() {
        return expireDateStr;
    }

    public void setExpireDateStr(String expireDateStr) {
        this.expireDateStr = expireDateStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public int getDurationInt() {
        return durationInt;
    }

    public void setDurationInt(int durationInt) {
        this.durationInt = durationInt;
    }
}
