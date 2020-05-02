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
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.AcademicTitleRecord;


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
public class AcademicTitle extends TableImpl<AcademicTitleRecord> {

    private static final long serialVersionUID = -950516391;

    /**
     * The reference instance of <code>zone.academic_title</code>
     */
    public static final AcademicTitle ACADEMIC_TITLE = new AcademicTitle();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AcademicTitleRecord> getRecordType() {
        return AcademicTitleRecord.class;
    }

    /**
     * The column <code>zone.academic_title.academic_title_id</code>.
     */
    public final TableField<AcademicTitleRecord, Integer> ACADEMIC_TITLE_ID = createField(DSL.name("academic_title_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>zone.academic_title.academic_title_name</code>.
     */
    public final TableField<AcademicTitleRecord, String> ACADEMIC_TITLE_NAME = createField(DSL.name("academic_title_name"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * Create a <code>zone.academic_title</code> table reference
     */
    public AcademicTitle() {
        this(DSL.name("academic_title"), null);
    }

    /**
     * Create an aliased <code>zone.academic_title</code> table reference
     */
    public AcademicTitle(String alias) {
        this(DSL.name(alias), ACADEMIC_TITLE);
    }

    /**
     * Create an aliased <code>zone.academic_title</code> table reference
     */
    public AcademicTitle(Name alias) {
        this(alias, ACADEMIC_TITLE);
    }

    private AcademicTitle(Name alias, Table<AcademicTitleRecord> aliased) {
        this(alias, aliased, null);
    }

    private AcademicTitle(Name alias, Table<AcademicTitleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> AcademicTitle(Table<O> child, ForeignKey<O, AcademicTitleRecord> key) {
        super(child, key, ACADEMIC_TITLE);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ACADEMIC_TITLE_PRIMARY);
    }

    @Override
    public Identity<AcademicTitleRecord, Integer> getIdentity() {
        return Keys.IDENTITY_ACADEMIC_TITLE;
    }

    @Override
    public UniqueKey<AcademicTitleRecord> getPrimaryKey() {
        return Keys.KEY_ACADEMIC_TITLE_PRIMARY;
    }

    @Override
    public List<UniqueKey<AcademicTitleRecord>> getKeys() {
        return Arrays.<UniqueKey<AcademicTitleRecord>>asList(Keys.KEY_ACADEMIC_TITLE_PRIMARY);
    }

    @Override
    public AcademicTitle as(String alias) {
        return new AcademicTitle(DSL.name(alias), this);
    }

    @Override
    public AcademicTitle as(Name alias) {
        return new AcademicTitle(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AcademicTitle rename(String name) {
        return new AcademicTitle(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AcademicTitle rename(Name name) {
        return new AcademicTitle(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
