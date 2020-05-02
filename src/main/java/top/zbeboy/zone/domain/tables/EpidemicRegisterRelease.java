/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.EpidemicRegisterReleaseRecord;


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
public class EpidemicRegisterRelease extends TableImpl<EpidemicRegisterReleaseRecord> {

    private static final long serialVersionUID = -1253559553;

    /**
     * The reference instance of <code>zone.epidemic_register_release</code>
     */
    public static final EpidemicRegisterRelease EPIDEMIC_REGISTER_RELEASE = new EpidemicRegisterRelease();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpidemicRegisterReleaseRecord> getRecordType() {
        return EpidemicRegisterReleaseRecord.class;
    }

    /**
     * The column <code>zone.epidemic_register_release.epidemic_register_release_id</code>.
     */
    public final TableField<EpidemicRegisterReleaseRecord, String> EPIDEMIC_REGISTER_RELEASE_ID = createField(DSL.name("epidemic_register_release_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_release.title</code>.
     */
    public final TableField<EpidemicRegisterReleaseRecord, String> TITLE = createField(DSL.name("title"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_release.username</code>.
     */
    public final TableField<EpidemicRegisterReleaseRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_release.publisher</code>.
     */
    public final TableField<EpidemicRegisterReleaseRecord, String> PUBLISHER = createField(DSL.name("publisher"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_release.release_time</code>.
     */
    public final TableField<EpidemicRegisterReleaseRecord, Timestamp> RELEASE_TIME = createField(DSL.name("release_time"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>zone.epidemic_register_release</code> table reference
     */
    public EpidemicRegisterRelease() {
        this(DSL.name("epidemic_register_release"), null);
    }

    /**
     * Create an aliased <code>zone.epidemic_register_release</code> table reference
     */
    public EpidemicRegisterRelease(String alias) {
        this(DSL.name(alias), EPIDEMIC_REGISTER_RELEASE);
    }

    /**
     * Create an aliased <code>zone.epidemic_register_release</code> table reference
     */
    public EpidemicRegisterRelease(Name alias) {
        this(alias, EPIDEMIC_REGISTER_RELEASE);
    }

    private EpidemicRegisterRelease(Name alias, Table<EpidemicRegisterReleaseRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpidemicRegisterRelease(Name alias, Table<EpidemicRegisterReleaseRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> EpidemicRegisterRelease(Table<O> child, ForeignKey<O, EpidemicRegisterReleaseRecord> key) {
        super(child, key, EPIDEMIC_REGISTER_RELEASE);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EPIDEMIC_REGISTER_RELEASE_PRIMARY, Indexes.EPIDEMIC_REGISTER_RELEASE_USERNAME);
    }

    @Override
    public UniqueKey<EpidemicRegisterReleaseRecord> getPrimaryKey() {
        return Keys.KEY_EPIDEMIC_REGISTER_RELEASE_PRIMARY;
    }

    @Override
    public List<UniqueKey<EpidemicRegisterReleaseRecord>> getKeys() {
        return Arrays.<UniqueKey<EpidemicRegisterReleaseRecord>>asList(Keys.KEY_EPIDEMIC_REGISTER_RELEASE_PRIMARY);
    }

    @Override
    public List<ForeignKey<EpidemicRegisterReleaseRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<EpidemicRegisterReleaseRecord, ?>>asList(Keys.EPIDEMIC_REGISTER_RELEASE_IBFK_1);
    }

    public Users users() {
        return new Users(this, Keys.EPIDEMIC_REGISTER_RELEASE_IBFK_1);
    }

    @Override
    public EpidemicRegisterRelease as(String alias) {
        return new EpidemicRegisterRelease(DSL.name(alias), this);
    }

    @Override
    public EpidemicRegisterRelease as(Name alias) {
        return new EpidemicRegisterRelease(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpidemicRegisterRelease rename(String name) {
        return new EpidemicRegisterRelease(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EpidemicRegisterRelease rename(Name name) {
        return new EpidemicRegisterRelease(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, Timestamp> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
