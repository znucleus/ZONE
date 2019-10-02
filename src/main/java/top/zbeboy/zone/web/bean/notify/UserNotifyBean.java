package top.zbeboy.zone.web.bean.notify;

import top.zbeboy.zone.domain.tables.pojos.UserNotify;

public class UserNotifyBean extends UserNotify {
    private String createDateStr;
    private String realName;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
