package top.zbeboy.zone.web.bean.register.epidemic;

import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterRelease;

public class EpidemicRegisterReleaseBean extends EpidemicRegisterRelease {
    private String releaseTimeStr;
    private Byte canOperator;
    private Byte canReview;

    public String getReleaseTimeStr() {
        return releaseTimeStr;
    }

    public void setReleaseTimeStr(String releaseTimeStr) {
        this.releaseTimeStr = releaseTimeStr;
    }

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }

    public Byte getCanReview() {
        return canReview;
    }

    public void setCanReview(Byte canReview) {
        this.canReview = canReview;
    }
}
