package top.zbeboy.zone.web.bean.training.attend;

import top.zbeboy.zone.domain.tables.pojos.TrainingAttend;

public class TrainingAttendBean extends TrainingAttend {
    private String buildingName;
    private String buildingCode;
    private String publishDateStr;
    private Byte canOperator;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getPublishDateStr() {
        return publishDateStr;
    }

    public void setPublishDateStr(String publishDateStr) {
        this.publishDateStr = publishDateStr;
    }

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }
}
