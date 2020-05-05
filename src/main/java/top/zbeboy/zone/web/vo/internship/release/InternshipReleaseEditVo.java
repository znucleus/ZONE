package top.zbeboy.zone.web.vo.internship.release;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class InternshipReleaseEditVo {
    @NotBlank(message = "实习发布ID不能为空")
    @Size(max = 64, message = "实习发布ID不正确")
    private String internshipReleaseId;
    @NotBlank(message = "实习发布标题不能为空")
    @Size(max = 100, message = "标题100个字符以内")
    private String internshipTitle;
    private String teacherDistributionTime;
    private String time;
    private Byte internshipReleaseIsDel;
    private String files;
    private Byte isTimeLimit;

    public String getInternshipReleaseId() {
        return internshipReleaseId;
    }

    public void setInternshipReleaseId(String internshipReleaseId) {
        this.internshipReleaseId = internshipReleaseId;
    }

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    public String getTeacherDistributionTime() {
        return teacherDistributionTime;
    }

    public void setTeacherDistributionTime(String teacherDistributionTime) {
        this.teacherDistributionTime = teacherDistributionTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Byte getInternshipReleaseIsDel() {
        return internshipReleaseIsDel;
    }

    public void setInternshipReleaseIsDel(Byte internshipReleaseIsDel) {
        this.internshipReleaseIsDel = internshipReleaseIsDel;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public Byte getIsTimeLimit() {
        return isTimeLimit;
    }

    public void setIsTimeLimit(Byte isTimeLimit) {
        this.isTimeLimit = isTimeLimit;
    }
}
