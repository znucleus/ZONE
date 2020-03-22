package top.zbeboy.zone.web.bean.training.release;

import top.zbeboy.zone.domain.tables.pojos.TrainingAuthorities;

import java.sql.Timestamp;

public class TrainingAuthoritiesBean extends TrainingAuthorities {
    private String validDateStr;
    private String expireDateStr;
    private String realName;

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
}
