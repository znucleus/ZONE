package top.zbeboy.zone.web.vo.training.release;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TrainingReleaseAddVo {
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String title;
    @NotBlank(message = "开课时间不能为空")
    private String startDate;
    @NotBlank(message = "结课时间不能为空")
    private String endDate;
    @NotNull(message = "课程ID不能为空")
    @Min(value = 1, message = "课程ID不正确")
    private Integer courseId;
    @NotNull(message = "班级ID不能为空")
    @Min(value = 1, message = "班级ID不正确")
    private Integer organizeId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(Integer organizeId) {
        this.organizeId = organizeId;
    }
}
