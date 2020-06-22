package top.zbeboy.zone.web.bean.training.special;

import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecialFileType;

public class TrainingSpecialFileTypeBean extends TrainingSpecialFileType {
    private Byte canOperator;

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }
}
