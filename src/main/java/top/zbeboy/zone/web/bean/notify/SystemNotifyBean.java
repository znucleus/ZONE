package top.zbeboy.zone.web.bean.notify;

import top.zbeboy.zbase.domain.tables.pojos.SystemNotify;

public class SystemNotifyBean extends SystemNotify {
    private String validDateStr;
    private String expireDateStr;
    private String realName;
    private String createDateStr;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
