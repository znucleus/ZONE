package top.zbeboy.zone.web.vo.attend.users;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AttendUsersAddVo {
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号20个字符以内")
    private String studentNumber;
    @NotBlank(message = "签到发布主表ID不能为空")
    @Size(max = 64, message = "签到发布主表ID不正确")
    private String attendReleaseId;
    private String remark;

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getAttendReleaseId() {
        return attendReleaseId;
    }

    public void setAttendReleaseId(String attendReleaseId) {
        this.attendReleaseId = attendReleaseId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
