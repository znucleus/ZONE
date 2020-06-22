package top.zbeboy.zone.web.bean.system.mailbox;

import top.zbeboy.zbase.domain.tables.pojos.SystemMailboxLog;

public class SystemMailboxLogBean extends SystemMailboxLog {

    private String sendTimeNew;

    public String getSendTimeNew() {
        return sendTimeNew;
    }

    public void setSendTimeNew(String sendTimeNew) {
        this.sendTimeNew = sendTimeNew;
    }
}
