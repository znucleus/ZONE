package top.zbeboy.zone.web.bean.internship.statistical;

import top.zbeboy.zone.domain.tables.pojos.InternshipChangeHistory;

public class InternshipChangeHistoryBean extends InternshipChangeHistory {
    private String internshipTitle;
    private String realName;
    private String studentNumber;
    private String organizeName;
    private String applyTimeStr;
    private String changeFillStartTimeStr;
    private String changeFillEndTimeStr;

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
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

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
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
}
