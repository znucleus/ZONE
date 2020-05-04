package top.zbeboy.zone.web.bean.training.special;

import top.zbeboy.zone.domain.tables.pojos.TrainingSpecial;

public class TrainingSpecialBean extends TrainingSpecial {
    private String relativePath;
    private String releaseTimeStr;

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
}
