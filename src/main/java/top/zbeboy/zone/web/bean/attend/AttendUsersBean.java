package top.zbeboy.zone.web.bean.attend;

import top.zbeboy.zbase.domain.tables.pojos.AttendUsers;

import java.sql.Timestamp;

public class AttendUsersBean extends AttendUsers {
    private String realName;
    private String studentNumber;
    private String location;
    private String address;
    private Timestamp attendDate;
    private String attendRemark;
    private Byte deviceSame;
    private String attendDateStr;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getAttendDate() {
        return attendDate;
    }

    public void setAttendDate(Timestamp attendDate) {
        this.attendDate = attendDate;
    }

    public String getAttendRemark() {
        return attendRemark;
    }

    public void setAttendRemark(String attendRemark) {
        this.attendRemark = attendRemark;
    }

    public Byte getDeviceSame() {
        return deviceSame;
    }

    public void setDeviceSame(Byte deviceSame) {
        this.deviceSame = deviceSame;
    }

    public String getAttendDateStr() {
        return attendDateStr;
    }

    public void setAttendDateStr(String attendDateStr) {
        this.attendDateStr = attendDateStr;
    }
}
