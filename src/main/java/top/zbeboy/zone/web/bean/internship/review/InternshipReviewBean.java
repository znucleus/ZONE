package top.zbeboy.zone.web.bean.internship.review;

import java.sql.Date;
import java.sql.Timestamp;

public class InternshipReviewBean {
    private int studentId;
    private String internshipReleaseId;
    private String realName;
    private String studentNumber;
    private String scienceName;
    private String organizeName;
    private String reason;
    private int internshipApplyState;
    private Timestamp changeFillStartTime;
    private Timestamp changeFillEndTime;
    private String changeFillStartTimeStr;
    private String changeFillEndTimeStr;
    private Date startTime;
    private Date endTime;
    private Timestamp applyTime;
    private String applyTimeStr;
    private String companyName;
    private String companyAddress;
    private String companyContact;
    private Byte commitmentBook;
    private Byte safetyResponsibilityBook;
    private Byte practiceAgreement;
    private Byte internshipApplication;
    private Byte practiceReceiving;
    private Byte securityEducationAgreement;
    private Byte parentalConsent;
    private String fileId;
    private String originalFileName;
    private String ext;

    // 实习审核填写时间段
    private String fillTime;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getInternshipApplyState() {
        return internshipApplyState;
    }

    public void setInternshipApplyState(int internshipApplyState) {
        this.internshipApplyState = internshipApplyState;
    }

    public Timestamp getChangeFillStartTime() {
        return changeFillStartTime;
    }

    public void setChangeFillStartTime(Timestamp changeFillStartTime) {
        this.changeFillStartTime = changeFillStartTime;
    }

    public Timestamp getChangeFillEndTime() {
        return changeFillEndTime;
    }

    public void setChangeFillEndTime(Timestamp changeFillEndTime) {
        this.changeFillEndTime = changeFillEndTime;
    }

    public String getChangeFillStartTimeStr() {
        return changeFillStartTimeStr;
    }

    public void setChangeFillStartTimeStr(String changeFillStartTimeStr) {
        this.changeFillStartTimeStr = changeFillStartTimeStr;
    }

    public String getChangeFillEndTimeStr() {
        return changeFillEndTimeStr;
    }

    public void setChangeFillEndTimeStr(String changeFillEndTimeStr) {
        this.changeFillEndTimeStr = changeFillEndTimeStr;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Timestamp getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Timestamp applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public String getFillTime() {
        return fillTime;
    }

    public void setFillTime(String fillTime) {
        this.fillTime = fillTime;
    }
}
