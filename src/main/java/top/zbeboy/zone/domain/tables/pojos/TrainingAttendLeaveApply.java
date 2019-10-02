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
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TrainingAttendLeaveApply implements Serializable {

    private static final long serialVersionUID = -1887319630;

    private String    leaveApplyId;
    private String    trainingAttendId;
    private String    trainingUsersId;
    private Timestamp applyDate;
    private String    applyReason;
    private Byte      applyState;
    private String    remark;

    public TrainingAttendLeaveApply() {}

    public TrainingAttendLeaveApply(TrainingAttendLeaveApply value) {
        this.leaveApplyId = value.leaveApplyId;
        this.trainingAttendId = value.trainingAttendId;
        this.trainingUsersId = value.trainingUsersId;
        this.applyDate = value.applyDate;
        this.applyReason = value.applyReason;
        this.applyState = value.applyState;
        this.remark = value.remark;
    }

    public TrainingAttendLeaveApply(
        String    leaveApplyId,
        String    trainingAttendId,
        String    trainingUsersId,
        Timestamp applyDate,
        String    applyReason,
        Byte      applyState,
        String    remark
    ) {
        this.leaveApplyId = leaveApplyId;
        this.trainingAttendId = trainingAttendId;
        this.trainingUsersId = trainingUsersId;
        this.applyDate = applyDate;
        this.applyReason = applyReason;
        this.applyState = applyState;
        this.remark = remark;
    }

    @NotNull
    @Size(max = 64)
    public String getLeaveApplyId() {
        return this.leaveApplyId;
    }

    public void setLeaveApplyId(String leaveApplyId) {
        this.leaveApplyId = leaveApplyId;
    }

    @NotNull
    @Size(max = 64)
    public String getTrainingAttendId() {
        return this.trainingAttendId;
    }

    public void setTrainingAttendId(String trainingAttendId) {
        this.trainingAttendId = trainingAttendId;
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
    public Timestamp getApplyDate() {
        return this.applyDate;
    }

    public void setApplyDate(Timestamp applyDate) {
        this.applyDate = applyDate;
    }

    @NotNull
    @Size(max = 500)
    public String getApplyReason() {
        return this.applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    @NotNull
    public Byte getApplyState() {
        return this.applyState;
    }

    public void setApplyState(Byte applyState) {
        this.applyState = applyState;
    }

    @Size(max = 500)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrainingAttendLeaveApply (");

        sb.append(leaveApplyId);
        sb.append(", ").append(trainingAttendId);
        sb.append(", ").append(trainingUsersId);
        sb.append(", ").append(applyDate);
        sb.append(", ").append(applyReason);
        sb.append(", ").append(applyState);
        sb.append(", ").append(remark);

        sb.append(")");
        return sb.toString();
    }
}
