package top.zbeboy.zone.web.vo.internship.journal;

import javax.validation.constraints.NotBlank;

public class InternshipJournalAddVo {
    @NotBlank(message = "实习发布ID不能为空")
    private String  internshipReleaseId;
    @NotBlank(message = "内容与感想不能为空")
    private String internshipJournalContent;
    @NotBlank(message = "内容与感想HTML不能为空")
    private String  internshipJournalHtml;
    @NotBlank(message = "日志日期不能为空")
    private String internshipJournalDate;
    private Byte isSeeStaff;

    public String getInternshipReleaseId() {
        return internshipReleaseId;
    }

    public void setInternshipReleaseId(String internshipReleaseId) {
        this.internshipReleaseId = internshipReleaseId;
    }

    public String getInternshipJournalContent() {
        return internshipJournalContent;
    }

    public void setInternshipJournalContent(String internshipJournalContent) {
        this.internshipJournalContent = internshipJournalContent;
    }

    public String getInternshipJournalHtml() {
        return internshipJournalHtml;
    }

    public void setInternshipJournalHtml(String internshipJournalHtml) {
        this.internshipJournalHtml = internshipJournalHtml;
    }

    public String getInternshipJournalDate() {
        return internshipJournalDate;
    }

    public void setInternshipJournalDate(String internshipJournalDate) {
        this.internshipJournalDate = internshipJournalDate;
    }

    public Byte getIsSeeStaff() {
        return isSeeStaff;
    }

    public void setIsSeeStaff(Byte isSeeStaff) {
        this.isSeeStaff = isSeeStaff;
    }
}
