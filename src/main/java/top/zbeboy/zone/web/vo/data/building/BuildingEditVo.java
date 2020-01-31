package top.zbeboy.zone.web.vo.data.building;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BuildingEditVo {
    @NotNull(message = "楼不能为空")
    @Min(value = 1, message = "楼不正确")
    private int buildingId;
    @NotBlank(message = "楼名不能为空")
    @Size(max = 30, message = "楼名30个字符以内")
    private String buildingName;
    private Byte buildingIsDel;
    @NotNull(message = "院不能为空")
    @Min(value = 1, message = "院不正确")
    private int collegeId;

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Byte getBuildingIsDel() {
        return buildingIsDel;
    }

    public void setBuildingIsDel(Byte buildingIsDel) {
        this.buildingIsDel = buildingIsDel;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }
}
