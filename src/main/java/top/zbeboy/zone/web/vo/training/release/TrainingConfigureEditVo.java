package top.zbeboy.zone.web.vo.training.release;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainingConfigureEditVo {
    @NotBlank(message = "实训发布ID不能为空")
    @Size(max = 64, message = "实训发布ID不正确")
    private String trainingReleaseId;
    @NotBlank(message = "实训配置ID不能为空")
    @Size(max = 64, message = "实训配置ID不正确")
    private String trainingConfigureId;
    @NotNull(message = "星期不能为空")
    private Byte weekDay;
    @NotBlank(message = "上课开始时间不能为空")
    private String startTime;
    @NotBlank(message = "上课结束时间不能为空")
    private String endTime;
    @NotNull(message = "教室ID不能为空")
    @Min(value = 1, message = "教室ID不正确")
    private Integer schoolroomId;

    public String getTrainingReleaseId() {
        return trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }

    public String getTrainingConfigureId() {
        return trainingConfigureId;
    }

    public void setTrainingConfigureId(String trainingConfigureId) {
        this.trainingConfigureId = trainingConfigureId;
    }

    public Byte getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Byte weekDay) {
        this.weekDay = weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getSchoolroomId() {
        return schoolroomId;
    }

    public void setSchoolroomId(Integer schoolroomId) {
        this.schoolroomId = schoolroomId;
    }
}
