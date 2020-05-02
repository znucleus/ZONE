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
public class TrainingUsers implements Serializable {

    private static final long serialVersionUID = -46710354;

    private String    trainingUsersId;
    private String    trainingReleaseId;
    private Integer   studentId;
    private Timestamp createDate;
    private String    remark;

    public TrainingUsers() {}

    public TrainingUsers(TrainingUsers value) {
        this.trainingUsersId = value.trainingUsersId;
        this.trainingReleaseId = value.trainingReleaseId;
        this.studentId = value.studentId;
        this.createDate = value.createDate;
        this.remark = value.remark;
    }

    public TrainingUsers(
        String    trainingUsersId,
        String    trainingReleaseId,
        Integer   studentId,
        Timestamp createDate,
        String    remark
    ) {
        this.trainingUsersId = trainingUsersId;
        this.trainingReleaseId = trainingReleaseId;
        this.studentId = studentId;
        this.createDate = createDate;
        this.remark = remark;
    }

    @NotNull
    @Size(max = 64)
    public String getTrainingUsersId() {
        return this.trainingUsersId;
    }

    public void setTrainingUsersId(String trainingUsersId) {
        this.trainingUsersId = trainingUsersId;
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
    public Integer getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    @NotNull
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Size(max = 200)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrainingUsers (");

        sb.append(trainingUsersId);
        sb.append(", ").append(trainingReleaseId);
        sb.append(", ").append(studentId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(remark);

        sb.append(")");
        return sb.toString();
    }
}
