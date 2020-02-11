package top.zbeboy.zone.web.bean.internship.release;

import top.zbeboy.zone.domain.tables.pojos.InternshipRelease;

public class InternshipReleaseBean extends InternshipRelease {
    private String scienceName;
    private String departmentName;
    private String schoolName;
    private String collegeName;
    private String internshipTypeName;
    private int schoolId;
    private int collegeId;
    private String teacherDistributionStartTimeStr;
    private String teacherDistributionEndTimeStr;
    private String startTimeStr;
    private String endTimeStr;
    private String releaseTimeStr;
    private Byte canOperator;

    // 实习审核 是否可进入审核权限页面
    private Byte canAuthorize;
    // 实习审核 统计总数
    private int waitTotalData;
    private int passTotalData;
    private int failTotalData;
    private int basicApplyTotalData;
    private int companyApplyTotalData;
    private int basicFillTotalData;
    private int companyFillTotalData;

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

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }

    public Byte getCanAuthorize() {
        return canAuthorize;
    }

    public void setCanAuthorize(Byte canAuthorize) {
        this.canAuthorize = canAuthorize;
    }

    public int getWaitTotalData() {
        return waitTotalData;
    }

    public void setWaitTotalData(int waitTotalData) {
        this.waitTotalData = waitTotalData;
    }

    public int getPassTotalData() {
        return passTotalData;
    }

    public void setPassTotalData(int passTotalData) {
        this.passTotalData = passTotalData;
    }

    public int getFailTotalData() {
        return failTotalData;
    }

    public void setFailTotalData(int failTotalData) {
        this.failTotalData = failTotalData;
    }

    public int getBasicApplyTotalData() {
        return basicApplyTotalData;
    }

    public void setBasicApplyTotalData(int basicApplyTotalData) {
        this.basicApplyTotalData = basicApplyTotalData;
    }

    public int getCompanyApplyTotalData() {
        return companyApplyTotalData;
    }

    public void setCompanyApplyTotalData(int companyApplyTotalData) {
        this.companyApplyTotalData = companyApplyTotalData;
    }

    public int getBasicFillTotalData() {
        return basicFillTotalData;
    }

    public void setBasicFillTotalData(int basicFillTotalData) {
        this.basicFillTotalData = basicFillTotalData;
    }

    public int getCompanyFillTotalData() {
        return companyFillTotalData;
    }

    public void setCompanyFillTotalData(int companyFillTotalData) {
        this.companyFillTotalData = companyFillTotalData;
    }
}
