package top.zbeboy.zone.web.vo.internship.apply;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InternshipApplyAddVo {
    @NotNull(message = "学生ID不能为空")
    @Min(value = 1, message = "学生ID不正确")
    private int studentId;
    @NotNull(message = "学生账号不能为空")
    @Size(max = 64, message = "学生账号不正确")
    private String username;
    @NotBlank(message = "实习发布ID不能为空")
    @Size(max = 64, message = "实习发布ID不正确")
    private String internshipReleaseId;
    @NotBlank(message = "学生姓名不能为空")
    @Size(max = 15, message = "学生姓名15个字符以内")
    private String realName;
    @NotBlank(message = "专业班级不能为空")
    @Size(max = 50, message = "专业班级50个字符以内")
    private String organizeName;
    @NotBlank(message = "学生性别不能为空")
    @Size(max = 2, message = "学生性别不正确")
    private String studentSex;
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号20个字符以内")
    private String studentNumber;
    @NotBlank(message = "电话号码不能为空")
    @Size(max = 15, message = "电话号码15个字符以内")
    private String mobile;
    @NotBlank(message = "qq邮箱不能为空")
    @Size(max = 100, message = "qq邮箱100个字符以内")
    private String qqMailbox;
    @NotBlank(message = "父母联系电话不能为空")
    @Size(max = 20, message = "父母联系电话20个字符以内")
    private String parentContactPhone;
    @NotNull(message = "班主任不能为空")
    private int staffId;
    private String headmaster;
    private String headmasterTel;
    @NotBlank(message = "实习单位名称不能为空")
    @Size(max = 200, message = "实习单位名称200个字符以内")
    private String companyName;
    @NotBlank(message = "实习单位地址不能为空")
    @Size(max = 500, message = "实习单位地址500个字符以内")
    private String companyAddress;
    @NotBlank(message = "实习单位联系人不能为空")
    @Size(max = 10, message = "实习单位联系人10个字符以内")
    private String companyContact;
    @NotBlank(message = "实习单位联系人联系方式不能为空")
    @Size(max = 20, message = "实习单位联系人联系方式20个字符以内")
    private String companyMobile;
    @NotBlank(message = "校内指导教师不能为空")
    private String schoolGuidanceTeacher;
    private String schoolGuidanceTeacherTel;
    @NotBlank(message = "实习开始时间不能为空")
    private String startTime;
    @NotBlank(message = "实习结束时间不能为空")
    private String endTime;
    private Byte commitmentBook;
    private Byte safetyResponsibilityBook;
    private Byte practiceAgreement;
    private Byte internshipApplication;
    private Byte practiceReceiving;
    private Byte securityEducationAgreement;
    private Byte parentalConsent;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInternshipReleaseId() {
        return internshipReleaseId;
    }

    public void setInternshipReleaseId(String internshipReleaseId) {
        this.internshipReleaseId = internshipReleaseId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQqMailbox() {
        return qqMailbox;
    }

    public void setQqMailbox(String qqMailbox) {
        this.qqMailbox = qqMailbox;
    }

    public String getParentContactPhone() {
        return parentContactPhone;
    }

    public void setParentContactPhone(String parentContactPhone) {
        this.parentContactPhone = parentContactPhone;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getHeadmaster() {
        return headmaster;
    }

    public void setHeadmaster(String headmaster) {
        this.headmaster = headmaster;
    }

    public String getHeadmasterTel() {
        return headmasterTel;
    }

    public void setHeadmasterTel(String headmasterTel) {
        this.headmasterTel = headmasterTel;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyMobile() {
        return companyMobile;
    }

    public void setCompanyMobile(String companyMobile) {
        this.companyMobile = companyMobile;
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

    public Byte getCommitmentBook() {
        return commitmentBook;
    }

    public void setCommitmentBook(Byte commitmentBook) {
        this.commitmentBook = commitmentBook;
    }

    public Byte getSafetyResponsibilityBook() {
        return safetyResponsibilityBook;
    }

    public void setSafetyResponsibilityBook(Byte safetyResponsibilityBook) {
        this.safetyResponsibilityBook = safetyResponsibilityBook;
    }

    public Byte getPracticeAgreement() {
        return practiceAgreement;
    }

    public void setPracticeAgreement(Byte practiceAgreement) {
        this.practiceAgreement = practiceAgreement;
    }

    public Byte getInternshipApplication() {
        return internshipApplication;
    }

    public void setInternshipApplication(Byte internshipApplication) {
        this.internshipApplication = internshipApplication;
    }

    public Byte getPracticeReceiving() {
        return practiceReceiving;
    }

    public void setPracticeReceiving(Byte practiceReceiving) {
        this.practiceReceiving = practiceReceiving;
    }

    public Byte getSecurityEducationAgreement() {
        return securityEducationAgreement;
    }

    public void setSecurityEducationAgreement(Byte securityEducationAgreement) {
        this.securityEducationAgreement = securityEducationAgreement;
    }

    public Byte getParentalConsent() {
        return parentalConsent;
    }

    public void setParentalConsent(Byte parentalConsent) {
        this.parentalConsent = parentalConsent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("studentId", studentId)
                .append("username", username)
                .append("internshipReleaseId", internshipReleaseId)
                .append("realName", realName)
                .append("organizeName", organizeName)
                .append("studentSex", studentSex)
                .append("studentNumber", studentNumber)
                .append("mobile", mobile)
                .append("qqMailbox", qqMailbox)
                .append("parentContactPhone", parentContactPhone)
                .append("staffId", staffId)
                .append("headmaster", headmaster)
                .append("headmasterTel", headmasterTel)
                .append("companyName", companyName)
                .append("companyAddress", companyAddress)
                .append("companyContact", companyContact)
                .append("companyMobile", companyMobile)
                .append("schoolGuidanceTeacher", schoolGuidanceTeacher)
                .append("schoolGuidanceTeacherTel", schoolGuidanceTeacherTel)
                .append("startTime", startTime)
                .append("endTime", endTime)
                .append("commitmentBook", commitmentBook)
                .append("safetyResponsibilityBook", safetyResponsibilityBook)
                .append("practiceAgreement", practiceAgreement)
                .append("internshipApplication", internshipApplication)
                .append("practiceReceiving", practiceReceiving)
                .append("securityEducationAgreement", securityEducationAgreement)
                .append("parentalConsent", parentalConsent)
                .toString();
    }
}
