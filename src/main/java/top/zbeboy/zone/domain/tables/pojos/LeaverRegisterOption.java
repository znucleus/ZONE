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
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LeaverRegisterOption implements Serializable {

    private static final long serialVersionUID = 1796766851;

    private String leaverRegisterOptionId;
    private String leaverRegisterReleaseId;
    private String optionContent;
    private Byte   sort;

    public LeaverRegisterOption() {}

    public LeaverRegisterOption(LeaverRegisterOption value) {
        this.leaverRegisterOptionId = value.leaverRegisterOptionId;
        this.leaverRegisterReleaseId = value.leaverRegisterReleaseId;
        this.optionContent = value.optionContent;
        this.sort = value.sort;
    }

    public LeaverRegisterOption(
        String leaverRegisterOptionId,
        String leaverRegisterReleaseId,
        String optionContent,
        Byte   sort
    ) {
        this.leaverRegisterOptionId = leaverRegisterOptionId;
        this.leaverRegisterReleaseId = leaverRegisterReleaseId;
        this.optionContent = optionContent;
        this.sort = sort;
    }

    @NotNull
    @Size(max = 64)
    public String getLeaverRegisterOptionId() {
        return this.leaverRegisterOptionId;
    }

    public void setLeaverRegisterOptionId(String leaverRegisterOptionId) {
        this.leaverRegisterOptionId = leaverRegisterOptionId;
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
    @Size(max = 300)
    public String getOptionContent() {
        return this.optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    @NotNull
    public Byte getSort() {
        return this.sort;
    }

    public void setSort(Byte sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LeaverRegisterOption (");

        sb.append(leaverRegisterOptionId);
        sb.append(", ").append(leaverRegisterReleaseId);
        sb.append(", ").append(optionContent);
        sb.append(", ").append(sort);

        sb.append(")");
        return sb.toString();
    }
}