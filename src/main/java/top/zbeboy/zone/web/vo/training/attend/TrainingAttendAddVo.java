package top.zbeboy.zone.web.vo.training.attend;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainingAttendAddVo {
    @NotBlank(message = "实训发布ID不能为空")
    @Size(max = 64, message = "实训发布ID不正确")
    private String trainingReleaseId;
    @NotBlank(message = "日期不能为空")
    private String attendDate;
    @NotBlank(message = "上课开始时间不能为空")
    private String attendStartTime;
    @NotBlank(message = "上课结束时间不能为空")
    private String attendEndTime;
    @NotNull(message = "教室ID不能为空")
    @Min(value = 1, message = "教室ID不正确")
    private Integer attendRoom;
    private String remark;

    public String getTrainingReleaseId() {
        return trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }

    public String getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(String attendDate) {
        this.attendDate = attendDate;
    }

    public String getAttendStartTime() {
        return attendStartTime;
    }

    public void setAttendStartTime(String attendStartTime) {
        this.attendStartTime = attendStartTime;
    }

    public String getAttendEndTime() {
        return attendEndTime;
    }

    public void setAttendEndTime(String attendEndTime) {
        this.attendEndTime = attendEndTime;
    }

    public Integer getAttendRoom() {
        return attendRoom;
    }

    public void setAttendRoom(Integer attendRoom) {
        this.attendRoom = attendRoom;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
