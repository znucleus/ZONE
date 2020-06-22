package top.zbeboy.zone.web.bean.internship.journal;

import top.zbeboy.zbase.domain.tables.pojos.InternshipJournal;

public class InternshipJournalBean extends InternshipJournal {
    private String createDateStr;
    private String internshipJournalContent;
    private String internshipJournalDateStr;

    // 小组内统计个人日志数量
    private int journalNum;
    private String studentRealName;
    public static final String JOURNAL_NUM = "journalNum";

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public int getJournalNum() {
        return journalNum;
    }

    public void setJournalNum(int journalNum) {
        this.journalNum = journalNum;
    }

    public String getStudentRealName() {
        return studentRealName;
    }

    public void setStudentRealName(String studentRealName) {
        this.studentRealName = studentRealName;
    }

    public String getInternshipJournalContent() {
        return internshipJournalContent;
    }

    public void setInternshipJournalContent(String internshipJournalContent) {
        this.internshipJournalContent = internshipJournalContent;
    }

    public String getInternshipJournalDateStr() {
        return internshipJournalDateStr;
    }

    public void setInternshipJournalDateStr(String internshipJournalDateStr) {
        this.internshipJournalDateStr = internshipJournalDateStr;
    }
}
