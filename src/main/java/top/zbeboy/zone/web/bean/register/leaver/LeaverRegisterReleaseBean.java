package top.zbeboy.zone.web.bean.register.leaver;

import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;

import java.util.List;

public class LeaverRegisterReleaseBean extends LeaverRegisterRelease {
    private String releaseTimeStr;
    private Byte canOperator;
    private Byte canReview;
    private List<String> dataName;
    private String dataScopeName;

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

    public List<String> getDataName() {
        return dataName;
    }

    public void setDataName(List<String> dataName) {
        this.dataName = dataName;
    }

    public String getDataScopeName() {
        return dataScopeName;
    }

    public void setDataScopeName(String dataScopeName) {
        this.dataScopeName = dataScopeName;
    }
}
