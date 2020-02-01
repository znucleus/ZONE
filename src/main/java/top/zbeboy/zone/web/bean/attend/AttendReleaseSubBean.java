package top.zbeboy.zone.web.bean.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendReleaseSub;

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
}
