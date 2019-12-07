package top.zbeboy.zone.web.bean.system.log;

import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;

public class SystemOperatorLogBean extends SystemOperatorLog {

    private String operatingTimeNew;

    public String getOperatingTimeNew() {
        return operatingTimeNew;
    }

    public void setOperatingTimeNew(String operatingTimeNew) {
        this.operatingTimeNew = operatingTimeNew;
    }
}
