/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.SchoolRecord;


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
public class School extends TableImpl<SchoolRecord> {

    private static final long serialVersionUID = -2053141988;

    /**
     * The reference instance of <code>zone.school</code>
     */
    public static final School SCHOOL = new School();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SchoolRecord> getRecordType() {
        return SchoolRecord.class;
    }

    /**
     * The column <code>zone.school.school_id</code>.
     */
    public final TableField<SchoolRecord, Integer> SCHOOL_ID = createField(DSL.name("school_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>zone.school.school_name</code>.
     */
    public final TableField<SchoolRecord, String> SCHOOL_NAME = createField(DSL.name("school_name"), org.jooq.impl.SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>zone.school.school_is_del</code>.
     */
    public final TableField<SchoolRecord, Byte> SCHOOL_IS_DEL = createField(DSL.name("school_is_del"), org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * Create a <code>zone.school</code> table reference
     */
    public School() {
        this(DSL.name("school"), null);
    }

    /**
     * Create an aliased <code>zone.school</code> table reference
     */
    public School(String alias) {
        this(DSL.name(alias), SCHOOL);
    }

    /**
     * Create an aliased <code>zone.school</code> table reference
     */
    public School(Name alias) {
        this(alias, SCHOOL);
    }

    private School(Name alias, Table<SchoolRecord> aliased) {
        this(alias, aliased, null);
    }

    private School(Name alias, Table<SchoolRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> School(Table<O> child, ForeignKey<O, SchoolRecord> key) {
        super(child, key, SCHOOL);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SCHOOL_PRIMARY);
    }

    @Override
    public Identity<SchoolRecord, Integer> getIdentity() {
        return Keys.IDENTITY_SCHOOL;
    }

    @Override
    public UniqueKey<SchoolRecord> getPrimaryKey() {
        return Keys.KEY_SCHOOL_PRIMARY;
    }

    @Override
    public List<UniqueKey<SchoolRecord>> getKeys() {
        return Arrays.<UniqueKey<SchoolRecord>>asList(Keys.KEY_SCHOOL_PRIMARY);
    }

    @Override
    public School as(String alias) {
        return new School(DSL.name(alias), this);
    }

    @Override
    public School as(Name alias) {
        return new School(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public School rename(String name) {
        return new School(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public School rename(Name name) {
        return new School(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, Byte> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
