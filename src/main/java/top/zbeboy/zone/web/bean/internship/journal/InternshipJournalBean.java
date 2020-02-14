package top.zbeboy.zone.web.bean.internship.journal;

import top.zbeboy.zone.domain.tables.pojos.InternshipJournal;

public class InternshipJournalBean extends InternshipJournal {
    private String createDateStr;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
