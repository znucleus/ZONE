package top.zbeboy.zone.web.bean.training.special;

import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecialDocument;

public class TrainingSpecialDocumentBean extends TrainingSpecialDocument {
    private String createDateStr;
    private Byte canOperator;
    private String content;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
