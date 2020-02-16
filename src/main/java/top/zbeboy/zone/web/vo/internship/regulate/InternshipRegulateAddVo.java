package top.zbeboy.zone.web.vo.internship.regulate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InternshipRegulateAddVo {
    @NotBlank(message = "学生姓名不能为空")
    @Size(max = 30, message = "学生姓名30个字符以内")
    private String studentName;
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号20个字符以内")
    private String studentNumber;
    @NotBlank(message = "学生联系方式不能为空")
    @Size(max = 15, message = "学生联系方式15个字符以内")
    private String studentTel;
    @NotBlank(message = "实习内容不能为空")
    @Size(max = 200, message = "实习内容200个字符以内")
    private String internshipContent;
    @NotBlank(message = "实习进展不能为空")
    @Size(max = 200, message = "实习进展200个字符以内")
    private String internshipProgress;
    @NotBlank(message = "汇报途径不能为空")
    @Size(max = 20, message = "汇报途径20个字符以内")
    private String reportWay;
    @NotBlank(message = "汇报日期不能为空")
    private String reportDate;
    @NotNull(message = "学生ID不能为空")
    @Min(value = 1, message = "学生ID不正确")
    private int studentId;
    @NotBlank(message = "实习发布ID不能为空")
    @Size(max = 64, message = "实习发布ID不正确")
    private String internshipReleaseId;
    private String tliy = "无";

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentTel() {
        return studentTel;
    }

    public void setStudentTel(String studentTel) {
        this.studentTel = studentTel;
    }

    public String getInternshipContent() {
        return internshipContent;
    }

    public void setInternshipContent(String internshipContent) {
        this.internshipContent = internshipContent;
    }

    public String getInternshipProgress() {
        return internshipProgress;
    }

    public void setInternshipProgress(String internshipProgress) {
        this.internshipProgress = internshipProgress;
    }

    public String getReportWay() {
        return reportWay;
    }

    public void setReportWay(String reportWay) {
        this.reportWay = reportWay;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

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

    public String getTliy() {
        return tliy;
    }

    public void setTliy(String tliy) {
        this.tliy = tliy;
    }
}
