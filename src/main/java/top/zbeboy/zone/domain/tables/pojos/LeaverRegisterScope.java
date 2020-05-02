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
public class LeaverRegisterScope implements Serializable {

    private static final long serialVersionUID = 171207092;

    private String  leaverRegisterReleaseId;
    private Integer dataId;

    public LeaverRegisterScope() {}

    public LeaverRegisterScope(LeaverRegisterScope value) {
        this.leaverRegisterReleaseId = value.leaverRegisterReleaseId;
        this.dataId = value.dataId;
    }

    public LeaverRegisterScope(
        String  leaverRegisterReleaseId,
        Integer dataId
    ) {
        this.leaverRegisterReleaseId = leaverRegisterReleaseId;
        this.dataId = dataId;
    }

    @NotNull
    @Size(max = 64)
    public String getLeaverRegisterReleaseId() {
        return this.leaverRegisterReleaseId;
    }

    public void setLeaverRegisterReleaseId(String leaverRegisterReleaseId) {
        this.leaverRegisterReleaseId = leaverRegisterReleaseId;
    }

    @NotNull
    public Integer getDataId() {
        return this.dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LeaverRegisterScope (");

        sb.append(leaverRegisterReleaseId);
        sb.append(", ").append(dataId);

        sb.append(")");
        return sb.toString();
    }
}
