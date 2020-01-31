package top.zbeboy.zone.web.bean.attend;

import top.zbeboy.zone.domain.tables.pojos.AttendUsers;

public class AttendUsersBean extends AttendUsers {
    private String realName;
    private String studentNumber;

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
}
