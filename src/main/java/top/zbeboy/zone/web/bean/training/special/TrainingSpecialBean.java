package top.zbeboy.zone.web.bean.training.special;

import top.zbeboy.zone.domain.tables.pojos.TrainingSpecial;

public class TrainingSpecialBean extends TrainingSpecial {
    private String relativePath;
    private String releaseTimeStr;
    private Byte canOperator;
    private String realCover;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getReleaseTimeStr() {
        return releaseTimeStr;
    }

    public void setReleaseTimeStr(String releaseTimeStr) {
        this.releaseTimeStr = releaseTimeStr;
    }

    public Byte getCanOperator() {
        return canOperator;
    }

    public void setCanOperator(Byte canOperator) {
        this.canOperator = canOperator;
    }

    public String getRealCover() {
        return realCover;
    }

    public void setRealCover(String realCover) {
        this.realCover = realCover;
    }
}
