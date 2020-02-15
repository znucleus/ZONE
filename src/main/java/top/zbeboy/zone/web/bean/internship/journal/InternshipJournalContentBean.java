package top.zbeboy.zone.web.bean.internship.journal;

import top.zbeboy.zone.domain.tables.pojos.InternshipJournalContent;

public class InternshipJournalContentBean extends InternshipJournalContent {
    private String internshipJournalDateStr;

    public String getInternshipJournalDateStr() {
        return internshipJournalDateStr;
    }

    public void setInternshipJournalDateStr(String internshipJournalDateStr) {
        this.internshipJournalDateStr = internshipJournalDateStr;
    }
}
