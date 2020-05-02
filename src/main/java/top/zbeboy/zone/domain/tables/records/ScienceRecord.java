/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.Science;


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
public class ScienceRecord extends UpdatableRecordImpl<ScienceRecord> implements Record5<Integer, String, String, Byte, Integer> {

    private static final long serialVersionUID = 701426889;

    /**
     * Setter for <code>zone.science.science_id</code>.
     */
    public void setScienceId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.science.science_id</code>.
     */
    public Integer getScienceId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>zone.science.science_name</code>.
     */
    public void setScienceName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.science.science_name</code>.
     */
    @NotNull
    @Size(max = 200)
    public String getScienceName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.science.science_code</code>.
     */
    public void setScienceCode(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.science.science_code</code>.
     */
    @NotNull
    @Size(max = 20)
    public String getScienceCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>zone.science.science_is_del</code>.
     */
    public void setScienceIsDel(Byte value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.science.science_is_del</code>.
     */
    public Byte getScienceIsDel() {
        return (Byte) get(3);
    }

    /**
     * Setter for <code>zone.science.department_id</code>.
     */
    public void setDepartmentId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.science.department_id</code>.
     */
    @NotNull
    public Integer getDepartmentId() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, String, String, Byte, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Integer, String, String, Byte, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Science.SCIENCE.SCIENCE_ID;
    }

    @Override
    public Field<String> field2() {
        return Science.SCIENCE.SCIENCE_NAME;
    }

    @Override
    public Field<String> field3() {
        return Science.SCIENCE.SCIENCE_CODE;
    }

    @Override
    public Field<Byte> field4() {
        return Science.SCIENCE.SCIENCE_IS_DEL;
    }

    @Override
    public Field<Integer> field5() {
        return Science.SCIENCE.DEPARTMENT_ID;
    }

    @Override
    public Integer component1() {
        return getScienceId();
    }

    @Override
    public String component2() {
        return getScienceName();
    }

    @Override
    public String component3() {
        return getScienceCode();
    }

    @Override
    public Byte component4() {
        return getScienceIsDel();
    }

    @Override
    public Integer component5() {
        return getDepartmentId();
    }

    @Override
    public Integer value1() {
        return getScienceId();
    }

    @Override
    public String value2() {
        return getScienceName();
    }

    @Override
    public String value3() {
        return getScienceCode();
    }

    @Override
    public Byte value4() {
        return getScienceIsDel();
    }

    @Override
    public Integer value5() {
        return getDepartmentId();
    }

    @Override
    public ScienceRecord value1(Integer value) {
        setScienceId(value);
        return this;
    }

    @Override
    public ScienceRecord value2(String value) {
        setScienceName(value);
        return this;
    }

    @Override
    public ScienceRecord value3(String value) {
        setScienceCode(value);
        return this;
    }

    @Override
    public ScienceRecord value4(Byte value) {
        setScienceIsDel(value);
        return this;
    }

    @Override
    public ScienceRecord value5(Integer value) {
        setDepartmentId(value);
        return this;
    }

    @Override
    public ScienceRecord values(Integer value1, String value2, String value3, Byte value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScienceRecord
     */
    public ScienceRecord() {
        super(Science.SCIENCE);
    }

    /**
     * Create a detached, initialised ScienceRecord
     */
    public ScienceRecord(Integer scienceId, String scienceName, String scienceCode, Byte scienceIsDel, Integer departmentId) {
        super(Science.SCIENCE);

        set(0, scienceId);
        set(1, scienceName);
        set(2, scienceCode);
        set(3, scienceIsDel);
        set(4, departmentId);
    }
}
