/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;

import top.zbeboy.zone.domain.tables.LeaverRegisterDataOption;


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
public class LeaverRegisterDataOptionRecord extends TableRecordImpl<LeaverRegisterDataOptionRecord> implements Record2<String, String> {

    private static final long serialVersionUID = -214558099;

    /**
     * Setter for <code>zone.leaver_register_data_option.leaver_register_data_id</code>.
     */
    public void setLeaverRegisterDataId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.leaver_register_data_option.leaver_register_data_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getLeaverRegisterDataId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.leaver_register_data_option.leaver_register_option_id</code>.
     */
    public void setLeaverRegisterOptionId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.leaver_register_data_option.leaver_register_option_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getLeaverRegisterOptionId() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return LeaverRegisterDataOption.LEAVER_REGISTER_DATA_OPTION.LEAVER_REGISTER_DATA_ID;
    }

    @Override
    public Field<String> field2() {
        return LeaverRegisterDataOption.LEAVER_REGISTER_DATA_OPTION.LEAVER_REGISTER_OPTION_ID;
    }

    @Override
    public String component1() {
        return getLeaverRegisterDataId();
    }

    @Override
    public String component2() {
        return getLeaverRegisterOptionId();
    }

    @Override
    public String value1() {
        return getLeaverRegisterDataId();
    }

    @Override
    public String value2() {
        return getLeaverRegisterOptionId();
    }

    @Override
    public LeaverRegisterDataOptionRecord value1(String value) {
        setLeaverRegisterDataId(value);
        return this;
    }

    @Override
    public LeaverRegisterDataOptionRecord value2(String value) {
        setLeaverRegisterOptionId(value);
        return this;
    }

    @Override
    public LeaverRegisterDataOptionRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LeaverRegisterDataOptionRecord
     */
    public LeaverRegisterDataOptionRecord() {
        super(LeaverRegisterDataOption.LEAVER_REGISTER_DATA_OPTION);
    }

    /**
     * Create a detached, initialised LeaverRegisterDataOptionRecord
     */
    public LeaverRegisterDataOptionRecord(String leaverRegisterDataId, String leaverRegisterOptionId) {
        super(LeaverRegisterDataOption.LEAVER_REGISTER_DATA_OPTION);

        set(0, leaverRegisterDataId);
        set(1, leaverRegisterOptionId);
    }
}
