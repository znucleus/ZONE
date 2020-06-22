package top.zbeboy.zone.web.bean.answer.release;

import top.zbeboy.zbase.domain.tables.pojos.AnswerRelease;

public class AnswerReleaseBean extends AnswerRelease {
    private String startTimeStr;
    private String endTimeStr;
    private String releaseTimeStr;

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getReleaseTimeStr() {
        return releaseTimeStr;
    }

    public void setReleaseTimeStr(String releaseTimeStr) {
        this.releaseTimeStr = releaseTimeStr;
    }
}
