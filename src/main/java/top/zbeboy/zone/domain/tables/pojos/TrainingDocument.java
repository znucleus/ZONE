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
public class TrainingDocument implements Serializable {

    private static final long serialVersionUID = 857559542;

    private String    trainingDocumentId;
    private String    trainingReleaseId;
    private String    documentTitle;
    private String    username;
    private Integer   courseId;
    private String    creator;
    private Timestamp createDate;
    private Integer   reading;
    private Byte      isOriginal;
    private String    origin;

    public TrainingDocument() {}

    public TrainingDocument(TrainingDocument value) {
        this.trainingDocumentId = value.trainingDocumentId;
        this.trainingReleaseId = value.trainingReleaseId;
        this.documentTitle = value.documentTitle;
        this.username = value.username;
        this.courseId = value.courseId;
        this.creator = value.creator;
        this.createDate = value.createDate;
        this.reading = value.reading;
        this.isOriginal = value.isOriginal;
        this.origin = value.origin;
    }

    public TrainingDocument(
        String    trainingDocumentId,
        String    trainingReleaseId,
        String    documentTitle,
        String    username,
        Integer   courseId,
        String    creator,
        Timestamp createDate,
        Integer   reading,
        Byte      isOriginal,
        String    origin
    ) {
        this.trainingDocumentId = trainingDocumentId;
        this.trainingReleaseId = trainingReleaseId;
        this.documentTitle = documentTitle;
        this.username = username;
        this.courseId = courseId;
        this.creator = creator;
        this.createDate = createDate;
        this.reading = reading;
        this.isOriginal = isOriginal;
        this.origin = origin;
    }

    @NotNull
    @Size(max = 64)
    public String getTrainingDocumentId() {
        return this.trainingDocumentId;
    }

    public void setTrainingDocumentId(String trainingDocumentId) {
        this.trainingDocumentId = trainingDocumentId;
    }

    @NotNull
    @Size(max = 64)
    public String getTrainingReleaseId() {
        return this.trainingReleaseId;
    }

    public void setTrainingReleaseId(String trainingReleaseId) {
        this.trainingReleaseId = trainingReleaseId;
    }

    @NotNull
    @Size(max = 200)
    public String getDocumentTitle() {
        return this.documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
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
    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @NotNull
    @Size(max = 30)
    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @NotNull
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getReading() {
        return this.reading;
    }

    public void setReading(Integer reading) {
        this.reading = reading;
    }

    public Byte getIsOriginal() {
        return this.isOriginal;
    }

    public void setIsOriginal(Byte isOriginal) {
        this.isOriginal = isOriginal;
    }

    @Size(max = 500)
    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrainingDocument (");

        sb.append(trainingDocumentId);
        sb.append(", ").append(trainingReleaseId);
        sb.append(", ").append(documentTitle);
        sb.append(", ").append(username);
        sb.append(", ").append(courseId);
        sb.append(", ").append(creator);
        sb.append(", ").append(createDate);
        sb.append(", ").append(reading);
        sb.append(", ").append(isOriginal);
        sb.append(", ").append(origin);

        sb.append(")");
        return sb.toString();
    }
}
