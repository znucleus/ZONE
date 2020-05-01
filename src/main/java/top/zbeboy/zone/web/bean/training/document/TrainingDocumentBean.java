package top.zbeboy.zone.web.bean.training.document;

import top.zbeboy.zone.domain.tables.pojos.TrainingDocument;

public class TrainingDocumentBean extends TrainingDocument {
    private String createDateStr;

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
