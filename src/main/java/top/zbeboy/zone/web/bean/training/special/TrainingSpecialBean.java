package top.zbeboy.zone.web.bean.training.special;

import top.zbeboy.zbase.domain.tables.pojos.TrainingSpecial;

public class TrainingSpecialBean extends TrainingSpecial {
    private String relativePath;
    private String newName;
    private String ext;
    private String releaseTimeStr;
    private Byte canOperator;
    private String realCover;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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
