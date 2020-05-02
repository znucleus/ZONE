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
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.EpidemicRegisterDataRecord;


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
public class EpidemicRegisterData extends TableImpl<EpidemicRegisterDataRecord> {

    private static final long serialVersionUID = 712635733;

    /**
     * The reference instance of <code>zone.epidemic_register_data</code>
     */
    public static final EpidemicRegisterData EPIDEMIC_REGISTER_DATA = new EpidemicRegisterData();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EpidemicRegisterDataRecord> getRecordType() {
        return EpidemicRegisterDataRecord.class;
    }

    /**
     * The column <code>zone.epidemic_register_data.epidemic_register_data_id</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> EPIDEMIC_REGISTER_DATA_ID = createField(DSL.name("epidemic_register_data_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.location</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> LOCATION = createField(DSL.name("location"), org.jooq.impl.SQLDataType.VARCHAR(200), this, "");

    /**
     * The column <code>zone.epidemic_register_data.address</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> ADDRESS = createField(DSL.name("address"), org.jooq.impl.SQLDataType.VARCHAR(300), this, "");

    /**
     * The column <code>zone.epidemic_register_data.epidemic_status</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> EPIDEMIC_STATUS = createField(DSL.name("epidemic_status"), org.jooq.impl.SQLDataType.VARCHAR(500).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.register_real_name</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> REGISTER_REAL_NAME = createField(DSL.name("register_real_name"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.register_type</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> REGISTER_TYPE = createField(DSL.name("register_type"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.institute</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> INSTITUTE = createField(DSL.name("institute"), org.jooq.impl.SQLDataType.VARCHAR(500).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.register_date</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, Timestamp> REGISTER_DATE = createField(DSL.name("register_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.epidemic_register_release_id</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> EPIDEMIC_REGISTER_RELEASE_ID = createField(DSL.name("epidemic_register_release_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.register_username</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> REGISTER_USERNAME = createField(DSL.name("register_username"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.channel_id</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, Integer> CHANNEL_ID = createField(DSL.name("channel_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>zone.epidemic_register_data.remark</code>.
     */
    public final TableField<EpidemicRegisterDataRecord, String> REMARK = createField(DSL.name("remark"), org.jooq.impl.SQLDataType.VARCHAR(200), this, "");

    /**
     * Create a <code>zone.epidemic_register_data</code> table reference
     */
    public EpidemicRegisterData() {
        this(DSL.name("epidemic_register_data"), null);
    }

    /**
     * Create an aliased <code>zone.epidemic_register_data</code> table reference
     */
    public EpidemicRegisterData(String alias) {
        this(DSL.name(alias), EPIDEMIC_REGISTER_DATA);
    }

    /**
     * Create an aliased <code>zone.epidemic_register_data</code> table reference
     */
    public EpidemicRegisterData(Name alias) {
        this(alias, EPIDEMIC_REGISTER_DATA);
    }

    private EpidemicRegisterData(Name alias, Table<EpidemicRegisterDataRecord> aliased) {
        this(alias, aliased, null);
    }

    private EpidemicRegisterData(Name alias, Table<EpidemicRegisterDataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> EpidemicRegisterData(Table<O> child, ForeignKey<O, EpidemicRegisterDataRecord> key) {
        super(child, key, EPIDEMIC_REGISTER_DATA);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.EPIDEMIC_REGISTER_DATA_CHANNEL_ID, Indexes.EPIDEMIC_REGISTER_DATA_EPIDEMIC_REGISTER_RELEASE_ID, Indexes.EPIDEMIC_REGISTER_DATA_PRIMARY, Indexes.EPIDEMIC_REGISTER_DATA_REGISTER_USERNAME);
    }

    @Override
    public UniqueKey<EpidemicRegisterDataRecord> getPrimaryKey() {
        return Keys.KEY_EPIDEMIC_REGISTER_DATA_PRIMARY;
    }

    @Override
    public List<UniqueKey<EpidemicRegisterDataRecord>> getKeys() {
        return Arrays.<UniqueKey<EpidemicRegisterDataRecord>>asList(Keys.KEY_EPIDEMIC_REGISTER_DATA_PRIMARY);
    }

    @Override
    public List<ForeignKey<EpidemicRegisterDataRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<EpidemicRegisterDataRecord, ?>>asList(Keys.EPIDEMIC_REGISTER_DATA_IBFK_1, Keys.EPIDEMIC_REGISTER_DATA_IBFK_2, Keys.EPIDEMIC_REGISTER_DATA_IBFK_3);
    }

    public EpidemicRegisterRelease epidemicRegisterRelease() {
        return new EpidemicRegisterRelease(this, Keys.EPIDEMIC_REGISTER_DATA_IBFK_1);
    }

    public Users users() {
        return new Users(this, Keys.EPIDEMIC_REGISTER_DATA_IBFK_2);
    }

    public Channel channel() {
        return new Channel(this, Keys.EPIDEMIC_REGISTER_DATA_IBFK_3);
    }

    @Override
    public EpidemicRegisterData as(String alias) {
        return new EpidemicRegisterData(DSL.name(alias), this);
    }

    @Override
    public EpidemicRegisterData as(Name alias) {
        return new EpidemicRegisterData(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EpidemicRegisterData rename(String name) {
        return new EpidemicRegisterData(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public EpidemicRegisterData rename(Name name) {
        return new EpidemicRegisterData(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<String, String, String, String, String, String, String, Timestamp, String, String, Integer, String> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}
