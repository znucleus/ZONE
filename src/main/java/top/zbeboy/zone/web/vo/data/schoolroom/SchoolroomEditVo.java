package top.zbeboy.zone.web.vo.data.schoolroom;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SchoolroomEditVo {
    @NotNull(message = "教室不能为空")
    @Min(value = 1, message = "教室不正确")
    private int schoolroomId;
    @NotBlank(message = "教室不能为空")
    @Size(max = 10, message = "教室10个字符以内")
    private String buildingCode;
    private Byte schoolroomIsDel;
    @NotNull(message = "楼不能为空")
    @Min(value = 1, message = "楼不正确")
    private int buildingId;

    public int getSchoolroomId() {
        return schoolroomId;
    }

    public void setSchoolroomId(int schoolroomId) {
        this.schoolroomId = schoolroomId;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Byte getSchoolroomIsDel() {
        return schoolroomIsDel;
    }

    public void setSchoolroomIsDel(Byte schoolroomIsDel) {
        this.schoolroomIsDel = schoolroomIsDel;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }
}
