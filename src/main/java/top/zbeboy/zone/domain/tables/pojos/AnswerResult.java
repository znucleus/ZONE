/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AnswerResult implements Serializable {

    private static final long serialVersionUID = -495837523;

    private String answerResultId;
    private String userId;
    private String userName;
    private Double lastSocre;
    private Double totalScore;
    private String ipAddress;
    private String answerReleaseId;

    public AnswerResult() {}

    public AnswerResult(AnswerResult value) {
        this.answerResultId = value.answerResultId;
        this.userId = value.userId;
        this.userName = value.userName;
        this.lastSocre = value.lastSocre;
        this.totalScore = value.totalScore;
        this.ipAddress = value.ipAddress;
        this.answerReleaseId = value.answerReleaseId;
    }

    public AnswerResult(
        String answerResultId,
        String userId,
        String userName,
        Double lastSocre,
        Double totalScore,
        String ipAddress,
        String answerReleaseId
    ) {
        this.answerResultId = answerResultId;
        this.userId = userId;
        this.userName = userName;
        this.lastSocre = lastSocre;
        this.totalScore = totalScore;
        this.ipAddress = ipAddress;
        this.answerReleaseId = answerReleaseId;
    }

    @NotNull
    @Size(max = 64)
    public String getAnswerResultId() {
        return this.answerResultId;
    }

    public void setAnswerResultId(String answerResultId) {
        this.answerResultId = answerResultId;
    }

    @NotNull
    @Size(max = 64)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Size(max = 20)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getLastSocre() {
        return this.lastSocre;
    }

    public void setLastSocre(Double lastSocre) {
        this.lastSocre = lastSocre;
    }

    @NotNull
    public Double getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    @Size(max = 50)
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @NotNull
    @Size(max = 64)
    public String getAnswerReleaseId() {
        return this.answerReleaseId;
    }

    public void setAnswerReleaseId(String answerReleaseId) {
        this.answerReleaseId = answerReleaseId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AnswerResult (");

        sb.append(answerResultId);
        sb.append(", ").append(userId);
        sb.append(", ").append(userName);
        sb.append(", ").append(lastSocre);
        sb.append(", ").append(totalScore);
        sb.append(", ").append(ipAddress);
        sb.append(", ").append(answerReleaseId);

        sb.append(")");
        return sb.toString();
    }
}
