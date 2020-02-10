/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;
import java.sql.Date;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InternshipInfo implements Serializable {

    private static final long serialVersionUID = -2061241850;

    private String  internshipInfoId;
    private Integer studentId;
    private String  studentUsername;
    private String  internshipReleaseId;
    private String  studentName;
    private String  organizeName;
    private String  studentSex;
    private String  studentNumber;
    private String  mobile;
    private String  qqMailbox;
    private String  parentContactPhone;
    private String  headmaster;
    private String  headmasterTel;
    private String  companyName;
    private String  companyAddress;
    private String  companyContact;
    private String  companyMobile;
    private String  schoolGuidanceTeacher;
    private String  schoolGuidanceTeacherTel;
    private Date    startTime;
    private Date    endTime;
    private Byte    commitmentBook;
    private Byte    safetyResponsibilityBook;
    private Byte    practiceAgreement;
    private Byte    internshipApplication;
    private Byte    practiceReceiving;
    private Byte    securityEducationAgreement;
    private Byte    parentalConsent;

    public InternshipInfo() {}

    public InternshipInfo(InternshipInfo value) {
        this.internshipInfoId = value.internshipInfoId;
        this.studentId = value.studentId;
        this.studentUsername = value.studentUsername;
        this.internshipReleaseId = value.internshipReleaseId;
        this.studentName = value.studentName;
        this.organizeName = value.organizeName;
        this.studentSex = value.studentSex;
        this.studentNumber = value.studentNumber;
        this.mobile = value.mobile;
        this.qqMailbox = value.qqMailbox;
        this.parentContactPhone = value.parentContactPhone;
        this.headmaster = value.headmaster;
        this.headmasterTel = value.headmasterTel;
        this.companyName = value.companyName;
        this.companyAddress = value.companyAddress;
        this.companyContact = value.companyContact;
        this.companyMobile = value.companyMobile;
        this.schoolGuidanceTeacher = value.schoolGuidanceTeacher;
        this.schoolGuidanceTeacherTel = value.schoolGuidanceTeacherTel;
        this.startTime = value.startTime;
        this.endTime = value.endTime;
        this.commitmentBook = value.commitmentBook;
        this.safetyResponsibilityBook = value.safetyResponsibilityBook;
        this.practiceAgreement = value.practiceAgreement;
        this.internshipApplication = value.internshipApplication;
        this.practiceReceiving = value.practiceReceiving;
        this.securityEducationAgreement = value.securityEducationAgreement;
        this.parentalConsent = value.parentalConsent;
    }

    public InternshipInfo(
        String  internshipInfoId,
        Integer studentId,
        String  studentUsername,
        String  internshipReleaseId,
        String  studentName,
        String  organizeName,
        String  studentSex,
        String  studentNumber,
        String  mobile,
        String  qqMailbox,
        String  parentContactPhone,
        String  headmaster,
        String  headmasterTel,
        String  companyName,
        String  companyAddress,
        String  companyContact,
        String  companyMobile,
        String  schoolGuidanceTeacher,
        String  schoolGuidanceTeacherTel,
        Date    startTime,
        Date    endTime,
        Byte    commitmentBook,
        Byte    safetyResponsibilityBook,
        Byte    practiceAgreement,
        Byte    internshipApplication,
        Byte    practiceReceiving,
        Byte    securityEducationAgreement,
        Byte    parentalConsent
    ) {
        this.internshipInfoId = internshipInfoId;
        this.studentId = studentId;
        this.studentUsername = studentUsername;
        this.internshipReleaseId = internshipReleaseId;
        this.studentName = studentName;
        this.organizeName = organizeName;
        this.studentSex = studentSex;
        this.studentNumber = studentNumber;
        this.mobile = mobile;
        this.qqMailbox = qqMailbox;
        this.parentContactPhone = parentContactPhone;
        this.headmaster = headmaster;
        this.headmasterTel = headmasterTel;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyContact = companyContact;
        this.companyMobile = companyMobile;
        this.schoolGuidanceTeacher = schoolGuidanceTeacher;
        this.schoolGuidanceTeacherTel = schoolGuidanceTeacherTel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.commitmentBook = commitmentBook;
        this.safetyResponsibilityBook = safetyResponsibilityBook;
        this.practiceAgreement = practiceAgreement;
        this.internshipApplication = internshipApplication;
        this.practiceReceiving = practiceReceiving;
        this.securityEducationAgreement = securityEducationAgreement;
        this.parentalConsent = parentalConsent;
    }

    @NotNull
    @Size(max = 64)
    public String getInternshipInfoId() {
        return this.internshipInfoId;
    }

    public void setInternshipInfoId(String internshipInfoId) {
        this.internshipInfoId = internshipInfoId;
    }

    @NotNull
    public Integer getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    @NotNull
    @Size(max = 64)
    public String getStudentUsername() {
        return this.studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    @NotNull
    @Size(max = 64)
    public String getInternshipReleaseId() {
        return this.internshipReleaseId;
    }

    public void setInternshipReleaseId(String internshipReleaseId) {
        this.internshipReleaseId = internshipReleaseId;
    }

    @NotNull
    @Size(max = 15)
    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @NotNull
    @Size(max = 50)
    public String getOrganizeName() {
        return this.organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    @NotNull
    @Size(max = 24)
    public String getStudentSex() {
        return this.studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    @NotNull
    @Size(max = 20)
    public String getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @NotNull
    @Size(max = 15)
    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @NotNull
    @Size(max = 100)
    public String getQqMailbox() {
        return this.qqMailbox;
    }

    public void setQqMailbox(String qqMailbox) {
        this.qqMailbox = qqMailbox;
    }

    @NotNull
    @Size(max = 48)
    public String getParentContactPhone() {
        return this.parentContactPhone;
    }

    public void setParentContactPhone(String parentContactPhone) {
        this.parentContactPhone = parentContactPhone;
    }

    @NotNull
    @Size(max = 10)
    public String getHeadmaster() {
        return this.headmaster;
    }

    public void setHeadmaster(String headmaster) {
        this.headmaster = headmaster;
    }

    @NotNull
    @Size(max = 20)
    public String getHeadmasterTel() {
        return this.headmasterTel;
    }

    public void setHeadmasterTel(String headmasterTel) {
        this.headmasterTel = headmasterTel;
    }

    @NotNull
    @Size(max = 200)
    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @NotNull
    @Size(max = 500)
    public String getCompanyAddress() {
        return this.companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @NotNull
    @Size(max = 10)
    public String getCompanyContact() {
        return this.companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    @NotNull
    @Size(max = 20)
    public String getCompanyMobile() {
        return this.companyMobile;
    }

    public void setCompanyMobile(String companyMobile) {
        this.companyMobile = companyMobile;
    }

    @NotNull
    @Size(max = 10)
    public String getSchoolGuidanceTeacher() {
        return this.schoolGuidanceTeacher;
    }

    public void setSchoolGuidanceTeacher(String schoolGuidanceTeacher) {
        this.schoolGuidanceTeacher = schoolGuidanceTeacher;
    }

    @NotNull
    @Size(max = 20)
    public String getSchoolGuidanceTeacherTel() {
        return this.schoolGuidanceTeacherTel;
    }

    public void setSchoolGuidanceTeacherTel(String schoolGuidanceTeacherTel) {
        this.schoolGuidanceTeacherTel = schoolGuidanceTeacherTel;
    }

    @NotNull
    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @NotNull
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Byte getCommitmentBook() {
        return this.commitmentBook;
    }

    public void setCommitmentBook(Byte commitmentBook) {
        this.commitmentBook = commitmentBook;
    }

    public Byte getSafetyResponsibilityBook() {
        return this.safetyResponsibilityBook;
    }

    public void setSafetyResponsibilityBook(Byte safetyResponsibilityBook) {
        this.safetyResponsibilityBook = safetyResponsibilityBook;
    }

    public Byte getPracticeAgreement() {
        return this.practiceAgreement;
    }

    public void setPracticeAgreement(Byte practiceAgreement) {
        this.practiceAgreement = practiceAgreement;
    }

    public Byte getInternshipApplication() {
        return this.internshipApplication;
    }

    public void setInternshipApplication(Byte internshipApplication) {
        this.internshipApplication = internshipApplication;
    }

    public Byte getPracticeReceiving() {
        return this.practiceReceiving;
    }

    public void setPracticeReceiving(Byte practiceReceiving) {
        this.practiceReceiving = practiceReceiving;
    }

    public Byte getSecurityEducationAgreement() {
        return this.securityEducationAgreement;
    }

    public void setSecurityEducationAgreement(Byte securityEducationAgreement) {
        this.securityEducationAgreement = securityEducationAgreement;
    }

    public Byte getParentalConsent() {
        return this.parentalConsent;
    }

    public void setParentalConsent(Byte parentalConsent) {
        this.parentalConsent = parentalConsent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InternshipInfo (");

        sb.append(internshipInfoId);
        sb.append(", ").append(studentId);
        sb.append(", ").append(studentUsername);
        sb.append(", ").append(internshipReleaseId);
        sb.append(", ").append(studentName);
        sb.append(", ").append(organizeName);
        sb.append(", ").append(studentSex);
        sb.append(", ").append(studentNumber);
        sb.append(", ").append(mobile);
        sb.append(", ").append(qqMailbox);
        sb.append(", ").append(parentContactPhone);
        sb.append(", ").append(headmaster);
        sb.append(", ").append(headmasterTel);
        sb.append(", ").append(companyName);
        sb.append(", ").append(companyAddress);
        sb.append(", ").append(companyContact);
        sb.append(", ").append(companyMobile);
        sb.append(", ").append(schoolGuidanceTeacher);
        sb.append(", ").append(schoolGuidanceTeacherTel);
        sb.append(", ").append(startTime);
        sb.append(", ").append(endTime);
        sb.append(", ").append(commitmentBook);
        sb.append(", ").append(safetyResponsibilityBook);
        sb.append(", ").append(practiceAgreement);
        sb.append(", ").append(internshipApplication);
        sb.append(", ").append(practiceReceiving);
        sb.append(", ").append(securityEducationAgreement);
        sb.append(", ").append(parentalConsent);

        sb.append(")");
        return sb.toString();
    }
}