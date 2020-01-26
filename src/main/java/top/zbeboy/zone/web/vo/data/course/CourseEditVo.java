package top.zbeboy.zone.web.vo.data.course;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CourseEditVo {
    @NotNull(message = "课程不能为空")
    @Min(value = 1, message = "课程不正确")
    private int courseId;
    @NotBlank(message = "课程名不能为空")
    @Size(max = 100, message = "课程名100个字符以内")
    private String courseName;
    private double courseCredit;
    private double courseHours;
    @NotNull(message = "课程类型不能为空")
    private Byte courseType;
    @NotNull(message = "年级不能为空")
    private Byte schoolYear;
    @NotNull(message = "学期不能为空")
    private Byte term;
    private String courseCode;
    private String courseBrief;
    private Byte courseIsDel;
    @NotNull(message = "院不能为空")
    @Min(value = 1, message = "院不正确")
    private int collegeId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(double courseCredit) {
        this.courseCredit = courseCredit;
    }

    public double getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(double courseHours) {
        this.courseHours = courseHours;
    }

    public Byte getCourseType() {
        return courseType;
    }

    public void setCourseType(Byte courseType) {
        this.courseType = courseType;
    }

    public Byte getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Byte schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Byte getTerm() {
        return term;
    }

    public void setTerm(Byte term) {
        this.term = term;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseBrief() {
        return courseBrief;
    }

    public void setCourseBrief(String courseBrief) {
        this.courseBrief = courseBrief;
    }

    public Byte getCourseIsDel() {
        return courseIsDel;
    }

    public void setCourseIsDel(Byte courseIsDel) {
        this.courseIsDel = courseIsDel;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }
}
