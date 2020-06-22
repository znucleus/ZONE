package top.zbeboy.zone.web.bean.internship.regulate;

import top.zbeboy.zbase.domain.tables.pojos.InternshipRegulate;

public class InternshipRegulateBean extends InternshipRegulate {
    private String createDateStr;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
