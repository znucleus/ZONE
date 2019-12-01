/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
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
import top.zbeboy.zone.domain.tables.records.RoleApplicationRecord;


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
public class RoleApplication extends TableImpl<RoleApplicationRecord> {

    private static final long serialVersionUID = -1836028434;

    /**
     * The reference instance of <code>zone.role_application</code>
     */
    public static final RoleApplication ROLE_APPLICATION = new RoleApplication();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoleApplicationRecord> getRecordType() {
        return RoleApplicationRecord.class;
    }

    /**
     * The column <code>zone.role_application.role_id</code>.
     */
    public final TableField<RoleApplicationRecord, String> ROLE_ID = createField(DSL.name("role_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.role_application.application_id</code>.
     */
    public final TableField<RoleApplicationRecord, String> APPLICATION_ID = createField(DSL.name("application_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * Create a <code>zone.role_application</code> table reference
     */
    public RoleApplication() {
        this(DSL.name("role_application"), null);
    }

    /**
     * Create an aliased <code>zone.role_application</code> table reference
     */
    public RoleApplication(String alias) {
        this(DSL.name(alias), ROLE_APPLICATION);
    }

    /**
     * Create an aliased <code>zone.role_application</code> table reference
     */
    public RoleApplication(Name alias) {
        this(alias, ROLE_APPLICATION);
    }

    private RoleApplication(Name alias, Table<RoleApplicationRecord> aliased) {
        this(alias, aliased, null);
    }

    private RoleApplication(Name alias, Table<RoleApplicationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> RoleApplication(Table<O> child, ForeignKey<O, RoleApplicationRecord> key) {
        super(child, key, ROLE_APPLICATION);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ROLE_APPLICATION_APPLICATION_ID, Indexes.ROLE_APPLICATION_PRIMARY);
    }

    @Override
    public UniqueKey<RoleApplicationRecord> getPrimaryKey() {
        return Keys.KEY_ROLE_APPLICATION_PRIMARY;
    }

    @Override
    public List<UniqueKey<RoleApplicationRecord>> getKeys() {
        return Arrays.<UniqueKey<RoleApplicationRecord>>asList(Keys.KEY_ROLE_APPLICATION_PRIMARY);
    }

    @Override
    public List<ForeignKey<RoleApplicationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoleApplicationRecord, ?>>asList(Keys.ROLE_APPLICATION_IBFK_1, Keys.ROLE_APPLICATION_IBFK_2);
    }

    public Role role() {
        return new Role(this, Keys.ROLE_APPLICATION_IBFK_1);
    }

    public Application application() {
        return new Application(this, Keys.ROLE_APPLICATION_IBFK_2);
    }

    @Override
    public RoleApplication as(String alias) {
        return new RoleApplication(DSL.name(alias), this);
    }

    @Override
    public RoleApplication as(Name alias) {
        return new RoleApplication(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RoleApplication rename(String name) {
        return new RoleApplication(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RoleApplication rename(Name name) {
        return new RoleApplication(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
