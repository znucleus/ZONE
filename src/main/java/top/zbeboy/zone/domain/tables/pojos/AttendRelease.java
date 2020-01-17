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
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AttendRelease implements Serializable {

    private static final long serialVersionUID = 2094648704;

    private String    attendReleaseId;
    private String    title;
    private Timestamp attendStartTime;
    private Timestamp attendEndTime;
    private Byte      isAuto;
    private Timestamp expireDate;
    private Integer   organizeId;
    private String    username;
    private Timestamp releaseTime;

    public AttendRelease() {}

    public AttendRelease(AttendRelease value) {
        this.attendReleaseId = value.attendReleaseId;
        this.title = value.title;
        this.attendStartTime = value.attendStartTime;
        this.attendEndTime = value.attendEndTime;
        this.isAuto = value.isAuto;
        this.expireDate = value.expireDate;
        this.organizeId = value.organizeId;
        this.username = value.username;
        this.releaseTime = value.releaseTime;
    }

    public AttendRelease(
        String    attendReleaseId,
        String    title,
        Timestamp attendStartTime,
        Timestamp attendEndTime,
        Byte      isAuto,
        Timestamp expireDate,
        Integer   organizeId,
        String    username,
        Timestamp releaseTime
    ) {
        this.attendReleaseId = attendReleaseId;
        this.title = title;
        this.attendStartTime = attendStartTime;
        this.attendEndTime = attendEndTime;
        this.isAuto = isAuto;
        this.expireDate = expireDate;
        this.organizeId = organizeId;
        this.username = username;
        this.releaseTime = releaseTime;
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
    @Size(max = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public Timestamp getAttendStartTime() {
        return this.attendStartTime;
    }

    public void setAttendStartTime(Timestamp attendStartTime) {
        this.attendStartTime = attendStartTime;
    }

    @NotNull
    public Timestamp getAttendEndTime() {
        return this.attendEndTime;
    }

    public void setAttendEndTime(Timestamp attendEndTime) {
        this.attendEndTime = attendEndTime;
    }

    public Byte getIsAuto() {
        return this.isAuto;
    }

    public void setIsAuto(Byte isAuto) {
        this.isAuto = isAuto;
    }

    @NotNull
    public Timestamp getExpireDate() {
        return this.expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    @NotNull
    public Integer getOrganizeId() {
        return this.organizeId;
    }

    public void setOrganizeId(Integer organizeId) {
        this.organizeId = organizeId;
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
    public Timestamp getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AttendRelease (");

        sb.append(attendReleaseId);
        sb.append(", ").append(title);
        sb.append(", ").append(attendStartTime);
        sb.append(", ").append(attendEndTime);
        sb.append(", ").append(isAuto);
        sb.append(", ").append(expireDate);
        sb.append(", ").append(organizeId);
        sb.append(", ").append(username);
        sb.append(", ").append(releaseTime);

        sb.append(")");
        return sb.toString();
    }
}
