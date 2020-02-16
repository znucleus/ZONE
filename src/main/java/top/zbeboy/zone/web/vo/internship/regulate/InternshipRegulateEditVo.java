package top.zbeboy.zone.web.vo.internship.regulate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class InternshipRegulateEditVo {
    @NotBlank(message = "实习监管ID不能为空")
    @Size(max = 64, message = "实习监管ID不正确")
    private String internshipRegulateId;
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
    private String tliy = "无";

    public String getInternshipRegulateId() {
        return internshipRegulateId;
    }

    public void setInternshipRegulateId(String internshipRegulateId) {
        this.internshipRegulateId = internshipRegulateId;
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

    public String getTliy() {
        return tliy;
    }

    public void setTliy(String tliy) {
        this.tliy = tliy;
    }
}
