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
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.AttendSubscribeLogRecord;


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
public class AttendSubscribeLog extends TableImpl<AttendSubscribeLogRecord> {

    private static final long serialVersionUID = 1988807686;

    /**
     * The reference instance of <code>zone.attend_subscribe_log</code>
     */
    public static final AttendSubscribeLog ATTEND_SUBSCRIBE_LOG = new AttendSubscribeLog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AttendSubscribeLogRecord> getRecordType() {
        return AttendSubscribeLogRecord.class;
    }

    /**
     * The column <code>zone.attend_subscribe_log.log_id</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> LOG_ID = createField(DSL.name("log_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.username</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.attend_release_id</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> ATTEND_RELEASE_ID = createField(DSL.name("attend_release_id"), org.jooq.impl.SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.real_name</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> REAL_NAME = createField(DSL.name("real_name"), org.jooq.impl.SQLDataType.VARCHAR(15), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.open_id</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> OPEN_ID = createField(DSL.name("open_id"), org.jooq.impl.SQLDataType.VARCHAR(200), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.template_id</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> TEMPLATE_ID = createField(DSL.name("template_id"), org.jooq.impl.SQLDataType.VARCHAR(64), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.request</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> REQUEST = createField(DSL.name("request"), org.jooq.impl.SQLDataType.VARCHAR(500), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.result</code>.
     */
    public final TableField<AttendSubscribeLogRecord, String> RESULT = createField(DSL.name("result"), org.jooq.impl.SQLDataType.VARCHAR(500), this, "");

    /**
     * The column <code>zone.attend_subscribe_log.create_date</code>.
     */
    public final TableField<AttendSubscribeLogRecord, Timestamp> CREATE_DATE = createField(DSL.name("create_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>zone.attend_subscribe_log</code> table reference
     */
    public AttendSubscribeLog() {
        this(DSL.name("attend_subscribe_log"), null);
    }

    /**
     * Create an aliased <code>zone.attend_subscribe_log</code> table reference
     */
    public AttendSubscribeLog(String alias) {
        this(DSL.name(alias), ATTEND_SUBSCRIBE_LOG);
    }

    /**
     * Create an aliased <code>zone.attend_subscribe_log</code> table reference
     */
    public AttendSubscribeLog(Name alias) {
        this(alias, ATTEND_SUBSCRIBE_LOG);
    }

    private AttendSubscribeLog(Name alias, Table<AttendSubscribeLogRecord> aliased) {
        this(alias, aliased, null);
    }

    private AttendSubscribeLog(Name alias, Table<AttendSubscribeLogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> AttendSubscribeLog(Table<O> child, ForeignKey<O, AttendSubscribeLogRecord> key) {
        super(child, key, ATTEND_SUBSCRIBE_LOG);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ATTEND_SUBSCRIBE_LOG_PRIMARY);
    }

    @Override
    public UniqueKey<AttendSubscribeLogRecord> getPrimaryKey() {
        return Keys.KEY_ATTEND_SUBSCRIBE_LOG_PRIMARY;
    }

    @Override
    public List<UniqueKey<AttendSubscribeLogRecord>> getKeys() {
        return Arrays.<UniqueKey<AttendSubscribeLogRecord>>asList(Keys.KEY_ATTEND_SUBSCRIBE_LOG_PRIMARY);
    }

    @Override
    public AttendSubscribeLog as(String alias) {
        return new AttendSubscribeLog(DSL.name(alias), this);
    }

    @Override
    public AttendSubscribeLog as(Name alias) {
        return new AttendSubscribeLog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AttendSubscribeLog rename(String name) {
        return new AttendSubscribeLog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AttendSubscribeLog rename(Name name) {
        return new AttendSubscribeLog(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, String, String, String, String, String, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}