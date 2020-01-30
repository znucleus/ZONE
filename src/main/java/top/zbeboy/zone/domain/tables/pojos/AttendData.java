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
public class AttendData implements Serializable {

    private static final long serialVersionUID = 2107955869;

    private String    attendUsersId;
    private Integer   attendReleaseSubId;
    private String    location;
    private String    address;
    private Timestamp createDate;
    private String    remark;

    public AttendData() {}

    public AttendData(AttendData value) {
        this.attendUsersId = value.attendUsersId;
        this.attendReleaseSubId = value.attendReleaseSubId;
        this.location = value.location;
        this.address = value.address;
        this.createDate = value.createDate;
        this.remark = value.remark;
    }

    public AttendData(
        String    attendUsersId,
        Integer   attendReleaseSubId,
        String    location,
        String    address,
        Timestamp createDate,
        String    remark
    ) {
        this.attendUsersId = attendUsersId;
        this.attendReleaseSubId = attendReleaseSubId;
        this.location = location;
        this.address = address;
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
    public Integer getAttendReleaseSubId() {
        return this.attendReleaseSubId;
    }

    public void setAttendReleaseSubId(Integer attendReleaseSubId) {
        this.attendReleaseSubId = attendReleaseSubId;
    }

    @Size(max = 30)
    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Size(max = 300)
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        StringBuilder sb = new StringBuilder("AttendData (");

        sb.append(attendUsersId);
        sb.append(", ").append(attendReleaseSubId);
        sb.append(", ").append(location);
        sb.append(", ").append(address);
        sb.append(", ").append(createDate);
        sb.append(", ").append(remark);

        sb.append(")");
        return sb.toString();
    }
}
