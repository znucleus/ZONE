package top.zbeboy.zone.web.bean.data.weixin;

import top.zbeboy.zone.domain.tables.pojos.WeiXin;

public class WeiXinBean extends WeiXin {
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
