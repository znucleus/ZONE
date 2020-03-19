package top.zbeboy.zone.web.bean.training.release;

import top.zbeboy.zone.domain.tables.pojos.TrainingConfigure;

public class TrainingConfigureBean extends TrainingConfigure {
    private String  buildingCode;
    private String  buildingName;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
