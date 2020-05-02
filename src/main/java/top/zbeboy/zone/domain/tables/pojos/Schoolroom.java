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
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Schoolroom implements Serializable {

    private static final long serialVersionUID = -1412740760;

    private Integer schoolroomId;
    private Integer buildingId;
    private String  buildingCode;
    private Byte    schoolroomIsDel;

    public Schoolroom() {}

    public Schoolroom(Schoolroom value) {
        this.schoolroomId = value.schoolroomId;
        this.buildingId = value.buildingId;
        this.buildingCode = value.buildingCode;
        this.schoolroomIsDel = value.schoolroomIsDel;
    }

    public Schoolroom(
        Integer schoolroomId,
        Integer buildingId,
        String  buildingCode,
        Byte    schoolroomIsDel
    ) {
        this.schoolroomId = schoolroomId;
        this.buildingId = buildingId;
        this.buildingCode = buildingCode;
        this.schoolroomIsDel = schoolroomIsDel;
    }

    public Integer getSchoolroomId() {
        return this.schoolroomId;
    }

    public void setSchoolroomId(Integer schoolroomId) {
        this.schoolroomId = schoolroomId;
    }

    @NotNull
    public Integer getBuildingId() {
        return this.buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    @NotNull
    @Size(max = 10)
    public String getBuildingCode() {
        return this.buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public Byte getSchoolroomIsDel() {
        return this.schoolroomIsDel;
    }

    public void setSchoolroomIsDel(Byte schoolroomIsDel) {
        this.schoolroomIsDel = schoolroomIsDel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Schoolroom (");

        sb.append(schoolroomId);
        sb.append(", ").append(buildingId);
        sb.append(", ").append(buildingCode);
        sb.append(", ").append(schoolroomIsDel);

        sb.append(")");
        return sb.toString();
    }
}
