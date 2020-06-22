package top.zbeboy.zone.web.bean.data.organize;

import top.zbeboy.zbase.domain.tables.pojos.Organize;

public class OrganizeBean extends Organize {
    private Byte schoolIsDel;
    private Byte collegeIsDel;
    private Byte departmentIsDel;
    private Byte scienceIsDel;
    private Byte gradeIsDel;
    private int grade;
    private String scienceName;
    private String departmentName;
    private String collegeName;
    private String schoolName;
    private int collegeId;
    private int schoolId;
    private int departmentId;
    private int scienceId;
    private String realName;

    public Byte getSchoolIsDel() {
        return schoolIsDel;
    }

    public void setSchoolIsDel(Byte schoolIsDel) {
        this.schoolIsDel = schoolIsDel;
    }

    public Byte getCollegeIsDel() {
        return collegeIsDel;
    }

    public void setCollegeIsDel(Byte collegeIsDel) {
        this.collegeIsDel = collegeIsDel;
    }

    public Byte getDepartmentIsDel() {
        return departmentIsDel;
    }

    public void setDepartmentIsDel(Byte departmentIsDel) {
        this.departmentIsDel = departmentIsDel;
    }

    public Byte getScienceIsDel() {
        return scienceIsDel;
    }

    public void setScienceIsDel(Byte scienceIsDel) {
        this.scienceIsDel = scienceIsDel;
    }

    public Byte getGradeIsDel() {
        return gradeIsDel;
    }

    public void setGradeIsDel(Byte gradeIsDel) {
        this.gradeIsDel = gradeIsDel;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
