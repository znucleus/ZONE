package top.zbeboy.zone.web.vo.internship.apply;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InternshipApplyEditVo {
    @NotNull(message = "学生ID不能为空")
    @Min(value = 1, message = "学生ID不正确")
    private int studentId;
    @Size(max = 64, message = "学生账号不正确")
    private String username;
    @NotBlank(message = "实习发布ID不能为空")
    @Size(max = 64, message = "实习发布ID不正确")
    private String internshipReleaseId;
    @Size(max = 15, message = "学生姓名15个字符以内")
    private String realName;
    @Size(max = 50, message = "专业班级50个字符以内")
    private String organizeName;
    @Size(max = 2, message = "学生性别不正确")
    private String studentSex;
    @Size(max = 20, message = "学号20个字符以内")
    private String studentNumber;
    @Size(max = 15, message = "电话号码15个字符以内")
    private String mobile;
    @Size(max = 100, message = "qq邮箱100个字符以内")
    private String qqMailbox;
    @Size(max = 20, message = "父母联系电话20个字符以内")
    private String parentContactPhone;
    private int staffId;
    private String headmaster;
    private String headmasterTel;
    @Size(max = 200, message = "实习单位名称200个字符以内")
    private String companyName;
    @Size(max = 500, message = "实习单位地址500个字符以内")
    private String companyAddress;
    @Size(max = 10, message = "实习单位联系人10个字符以内")
    private String companyContact;
    @Size(max = 20, message = "实习单位联系人联系方式20个字符以内")
    private String companyMobile;
    private String schoolGuidanceTeacher;
    private String schoolGuidanceTeacherTel;
    private String startTime;
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
}
