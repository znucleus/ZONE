package top.zbeboy.zone.web.bean.attend;

import top.zbeboy.zbase.domain.tables.pojos.AttendReleaseSub;

public class AttendReleaseSubBean extends AttendReleaseSub {
    private String realName;
    private String organizeName;
    private String attendStartTimeStr;
    private String attendEndTimeStr;
    private String releaseTimeStr;
    private String collegeName;
    private String schoolName;
    private String validDateStr;
    private String expireDateStr;
    private Byte isAttend;
    private Byte isSubscribe;

    // 微信模板数据
    private String templateId;
    private String page;
    private String data;
    private String miniProgramState;
    private String lang;
    private int studentId;

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

    public String getAttendStartTimeStr() {
        return attendStartTimeStr;
    }

    public void setAttendStartTimeStr(String attendStartTimeStr) {
        this.attendStartTimeStr = attendStartTimeStr;
    }

    public String getAttendEndTimeStr() {
        return attendEndTimeStr;
    }

    public void setAttendEndTimeStr(String attendEndTimeStr) {
        this.attendEndTimeStr = attendEndTimeStr;
    }

    public String getReleaseTimeStr() {
        return releaseTimeStr;
    }

    public void setReleaseTimeStr(String releaseTimeStr) {
        this.releaseTimeStr = releaseTimeStr;
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

    public String getValidDateStr() {
        return validDateStr;
    }

    public void setValidDateStr(String validDateStr) {
        this.validDateStr = validDateStr;
    }

    public String getExpireDateStr() {
        return expireDateStr;
    }

    public void setExpireDateStr(String expireDateStr) {
        this.expireDateStr = expireDateStr;
    }

    public Byte getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(Byte isAttend) {
        this.isAttend = isAttend;
    }

    public Byte getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(Byte isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMiniProgramState() {
        return miniProgramState;
    }

    public void setMiniProgramState(String miniProgramState) {
        this.miniProgramState = miniProgramState;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
