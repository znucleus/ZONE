package top.zbeboy.zone.web.bean.training.attend;

import top.zbeboy.zone.domain.tables.pojos.TrainingAttendUsers;

public class TrainingAttendUsersBean extends TrainingAttendUsers {
    private String realName;
    private String studentNumber;
    private String organizeName;
    private String mobile;
    private String email;
    private String sex;
    private String trainingReleaseId;

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

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTrainingReleaseId() {
        return trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }
}
