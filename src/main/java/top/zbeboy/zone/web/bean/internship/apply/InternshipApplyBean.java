package top.zbeboy.zone.web.bean.internship.apply;

import top.zbeboy.zone.domain.tables.pojos.InternshipApply;

import java.sql.Timestamp;

public class InternshipApplyBean extends InternshipApply {
    private String internshipTitle;
    private Timestamp releaseTime;
    private String username;
    private Timestamp teacherDistributionStartTime;
    private Timestamp teacherDistributionEndTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private Byte internshipReleaseIsDel;
    private String teacherDistributionStartTimeStr;
    private String teacherDistributionEndTimeStr;
    private String startTimeStr;
    private String endTimeStr;
    private String releaseTimeStr;
    private String applyTimeStr;
    private String publisher;
    private String scienceName;
    private String departmentName;
    private String schoolName;
    private String collegeName;
    private String internshipTypeName;
    private String originalFileName;
    private String ext;
    private String schoolGuidanceTeacher;
    private String schoolGuidanceTeacherTel;

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTeacherDistributionStartTime() {
        return teacherDistributionStartTime;
    }

    public void setTeacherDistributionStartTime(Timestamp teacherDistributionStartTime) {
        this.teacherDistributionStartTime = teacherDistributionStartTime;
    }

    public Timestamp getTeacherDistributionEndTime() {
        return teacherDistributionEndTime;
    }

    public void setTeacherDistributionEndTime(Timestamp teacherDistributionEndTime) {
        this.teacherDistributionEndTime = teacherDistributionEndTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Byte getInternshipReleaseIsDel() {
        return internshipReleaseIsDel;
    }

    public void setInternshipReleaseIsDel(Byte internshipReleaseIsDel) {
        this.internshipReleaseIsDel = internshipReleaseIsDel;
    }

    public String getTeacherDistributionStartTimeStr() {
        return teacherDistributionStartTimeStr;
    }

    public void setTeacherDistributionStartTimeStr(String teacherDistributionStartTimeStr) {
        this.teacherDistributionStartTimeStr = teacherDistributionStartTimeStr;
    }

    public String getTeacherDistributionEndTimeStr() {
        return teacherDistributionEndTimeStr;
    }

    public void setTeacherDistributionEndTimeStr(String teacherDistributionEndTimeStr) {
        this.teacherDistributionEndTimeStr = teacherDistributionEndTimeStr;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getReleaseTimeStr() {
        return releaseTimeStr;
    }

    public void setReleaseTimeStr(String releaseTimeStr) {
        this.releaseTimeStr = releaseTimeStr;
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getInternshipTypeName() {
        return internshipTypeName;
    }

    public void setInternshipTypeName(String internshipTypeName) {
        this.internshipTypeName = internshipTypeName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSchoolGuidanceTeacher() {
        return schoolGuidanceTeacher;
    }

    public void setSchoolGuidanceTeacher(String schoolGuidanceTeacher) {
        this.schoolGuidanceTeacher = schoolGuidanceTeacher;
    }

    public String getSchoolGuidanceTeacherTel() {
        return schoolGuidanceTeacherTel;
    }

    public void setSchoolGuidanceTeacherTel(String schoolGuidanceTeacherTel) {
        this.schoolGuidanceTeacherTel = schoolGuidanceTeacherTel;
    }
}
