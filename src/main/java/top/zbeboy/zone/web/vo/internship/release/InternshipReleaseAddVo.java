package top.zbeboy.zone.web.vo.internship.release;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class InternshipReleaseAddVo {
    @NotNull(message = "实习类型不能为空")
    @Min(value = 1, message = "实习类型不正确")
    private int internshipTypeId;
    private String teacherDistributionTime;
    private String time;
    private int schoolId;
    private int collegeId;
    private int departmentId;
    @NotNull(message = "专业ID不能为空")
    @Min(value = 1, message = "专业ID不正确")
    private int scienceId;
    private Byte internshipReleaseIsDel;
    private String files;
    private Byte isTimeLimit;

    public int getInternshipTypeId() {
        return internshipTypeId;
    }

    public void setInternshipTypeId(int internshipTypeId) {
        this.internshipTypeId = internshipTypeId;
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

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getScienceId() {
        return scienceId;
    }

    public void setScienceId(int scienceId) {
        this.scienceId = scienceId;
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
