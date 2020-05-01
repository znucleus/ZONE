package top.zbeboy.zone.web.bean.training.document;

import top.zbeboy.zone.domain.tables.pojos.TrainingDocumentFile;

public class TrainingDocumentFileBean extends TrainingDocumentFile {
    private String createDateStr;
    private Byte canOperator;
    private String trainingReleaseId;

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

    public String getTrainingReleaseId() {
        return trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }
}
