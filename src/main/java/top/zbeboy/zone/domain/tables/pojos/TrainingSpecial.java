/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TrainingSpecial implements Serializable {

    private static final long serialVersionUID = 523094082;

    private String    trainingSpecialId;
    private String    title;
    private String    cover;
    private String    username;
    private String    publisher;
    private Timestamp releaseTime;

    public TrainingSpecial() {}

    public TrainingSpecial(TrainingSpecial value) {
        this.trainingSpecialId = value.trainingSpecialId;
        this.title = value.title;
        this.cover = value.cover;
        this.username = value.username;
        this.publisher = value.publisher;
        this.releaseTime = value.releaseTime;
    }

    public TrainingSpecial(
        String    trainingSpecialId,
        String    title,
        String    cover,
        String    username,
        String    publisher,
        Timestamp releaseTime
    ) {
        this.trainingSpecialId = trainingSpecialId;
        this.title = title;
        this.cover = cover;
        this.username = username;
        this.publisher = publisher;
        this.releaseTime = releaseTime;
    }

    @NotNull
    @Size(max = 64)
    public String getTrainingSpecialId() {
        return this.trainingSpecialId;
    }

    public void setTrainingSpecialId(String trainingSpecialId) {
        this.trainingSpecialId = trainingSpecialId;
    }

    @NotNull
    @Size(max = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    @Size(max = 64)
    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Size(max = 30)
    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @NotNull
    public Timestamp getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrainingSpecial (");

        sb.append(trainingSpecialId);
        sb.append(", ").append(title);
        sb.append(", ").append(cover);
        sb.append(", ").append(username);
        sb.append(", ").append(publisher);
        sb.append(", ").append(releaseTime);

        sb.append(")");
        return sb.toString();
    }
}
