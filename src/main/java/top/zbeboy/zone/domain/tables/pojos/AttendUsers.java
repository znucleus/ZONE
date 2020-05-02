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
public class AttendUsers implements Serializable {

    private static final long serialVersionUID = 1321197022;

    private String    attendUsersId;
    private Integer   studentId;
    private String    attendReleaseId;
    private Timestamp createDate;
    private String    remark;

    public AttendUsers() {}

    public AttendUsers(AttendUsers value) {
        this.attendUsersId = value.attendUsersId;
        this.studentId = value.studentId;
        this.attendReleaseId = value.attendReleaseId;
        this.createDate = value.createDate;
        this.remark = value.remark;
    }

    public AttendUsers(
        String    attendUsersId,
        Integer   studentId,
        String    attendReleaseId,
        Timestamp createDate,
        String    remark
    ) {
        this.attendUsersId = attendUsersId;
        this.studentId = studentId;
        this.attendReleaseId = attendReleaseId;
        this.createDate = createDate;
        this.remark = remark;
    }

    @NotNull
    @Size(max = 64)
    public String getAttendUsersId() {
        return this.attendUsersId;
    }

    public void setAttendUsersId(String attendUsersId) {
        this.attendUsersId = attendUsersId;
    }

    @NotNull
    public Integer getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    @NotNull
    @Size(max = 64)
    public String getAttendReleaseId() {
        return this.attendReleaseId;
    }

    public void setAttendReleaseId(String attendReleaseId) {
        this.attendReleaseId = attendReleaseId;
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
        StringBuilder sb = new StringBuilder("AttendUsers (");

        sb.append(attendUsersId);
        sb.append(", ").append(studentId);
        sb.append(", ").append(attendReleaseId);
        sb.append(", ").append(createDate);
        sb.append(", ").append(remark);

        sb.append(")");
        return sb.toString();
    }
}
