package top.zbeboy.zone.web.bean.data.staff;

import top.zbeboy.zbase.domain.tables.pojos.Staff;

import java.sql.Date;

public class StaffBean extends Staff {
    private Byte schoolIsDel;
    private Byte collegeIsDel;
    private Byte departmentIsDel;
    private String departmentName;
    private Integer collegeId;
    private String collegeName;
    private Integer schoolId;
    private String schoolName;
    private String nationName;
    private String academicTitleName;
    private String politicalLandscapeName;
    private String roleName;
    private Byte enabled;
    private Byte accountNonLocked;
    private String realName;
    private String email;
    private String mobile;
    private String idCard;
    private Byte verifyMailbox;
    private String langKey;
    private Date joinDate;

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getNationName() {
        return nationName;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getAcademicTitleName() {
        return academicTitleName;
    }

    public void setAcademicTitleName(String academicTitleName) {
        this.academicTitleName = academicTitleName;
    }

    public String getPoliticalLandscapeName() {
        return politicalLandscapeName;
    }

    public void setPoliticalLandscapeName(String politicalLandscapeName) {
        this.politicalLandscapeName = politicalLandscapeName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public Byte getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Byte accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Byte getVerifyMailbox() {
        return verifyMailbox;
    }

    public void setVerifyMailbox(Byte verifyMailbox) {
        this.verifyMailbox = verifyMailbox;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
