package top.zbeboy.zone.web.bean.training.document;

import top.zbeboy.zone.domain.tables.pojos.TrainingDocument;

public class TrainingDocumentBean extends TrainingDocument {
    private String createDateStr;
    private Byte canOperator;
    private String trainingDocumentContent;

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

    public String getTrainingDocumentContent() {
        return trainingDocumentContent;
    }

    public void setTrainingDocumentContent(String trainingDocumentContent) {
        this.trainingDocumentContent = trainingDocumentContent;
    }
}
