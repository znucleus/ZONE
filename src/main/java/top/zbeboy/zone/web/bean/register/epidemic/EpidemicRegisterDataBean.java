package top.zbeboy.zone.web.bean.register.epidemic;

import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;

public class EpidemicRegisterDataBean extends EpidemicRegisterData {
    private String channelName;
    private String registerDateStr;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRegisterDateStr() {
        return registerDateStr;
    }

    public void setRegisterDateStr(String registerDateStr) {
        this.registerDateStr = registerDateStr;
    }
}
