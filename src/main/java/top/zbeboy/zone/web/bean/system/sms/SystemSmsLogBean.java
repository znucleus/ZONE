package top.zbeboy.zone.web.bean.system.sms;

import top.zbeboy.zbase.domain.tables.pojos.SystemSmsLog;

public class SystemSmsLogBean extends SystemSmsLog {

    private String sendTimeNew;

    public String getSendTimeNew() {
        return sendTimeNew;
    }

    public void setSendTimeNew(String sendTimeNew) {
        this.sendTimeNew = sendTimeNew;
    }
}
