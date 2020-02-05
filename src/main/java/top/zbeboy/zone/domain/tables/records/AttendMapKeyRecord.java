/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.AttendMapKey;


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
public class AttendMapKeyRecord extends UpdatableRecordImpl<AttendMapKeyRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1849147516;

    /**
     * Setter for <code>zone.attend_map_key.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.attend_map_key.id</code>.
     */
    @NotNull
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>zone.attend_map_key.map_key</code>.
     */
    public void setMapKey(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.attend_map_key.map_key</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getMapKey() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return AttendMapKey.ATTEND_MAP_KEY.ID;
    }

    @Override
    public Field<String> field2() {
        return AttendMapKey.ATTEND_MAP_KEY.MAP_KEY;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getMapKey();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getMapKey();
    }

    @Override
    public AttendMapKeyRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public AttendMapKeyRecord value2(String value) {
        setMapKey(value);
        return this;
    }

    @Override
    public AttendMapKeyRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AttendMapKeyRecord
     */
    public AttendMapKeyRecord() {
        super(AttendMapKey.ATTEND_MAP_KEY);
    }

    /**
     * Create a detached, initialised AttendMapKeyRecord
     */
    public AttendMapKeyRecord(Integer id, String mapKey) {
        super(AttendMapKey.ATTEND_MAP_KEY);

        set(0, id);
        set(1, mapKey);
    }
}
