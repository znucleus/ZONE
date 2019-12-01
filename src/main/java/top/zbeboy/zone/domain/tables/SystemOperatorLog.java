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
import top.zbeboy.zone.domain.tables.records.SystemOperatorLogRecord;


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
public class SystemOperatorLog extends TableImpl<SystemOperatorLogRecord> {

    private static final long serialVersionUID = -604755725;

    /**
     * The reference instance of <code>zone.system_operator_log</code>
     */
    public static final SystemOperatorLog SYSTEM_OPERATOR_LOG = new SystemOperatorLog();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SystemOperatorLogRecord> getRecordType() {
        return SystemOperatorLogRecord.class;
    }

    /**
     * The column <code>zone.system_operator_log.log_id</code>.
     */
    public final TableField<SystemOperatorLogRecord, String> LOG_ID = createField(DSL.name("log_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.system_operator_log.behavior</code>.
     */
    public final TableField<SystemOperatorLogRecord, String> BEHAVIOR = createField(DSL.name("behavior"), org.jooq.impl.SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>zone.system_operator_log.operating_time</code>.
     */
    public final TableField<SystemOperatorLogRecord, Timestamp> OPERATING_TIME = createField(DSL.name("operating_time"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>zone.system_operator_log.username</code>.
     */
    public final TableField<SystemOperatorLogRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>zone.system_operator_log.ip_address</code>.
     */
    public final TableField<SystemOperatorLogRecord, String> IP_ADDRESS = createField(DSL.name("ip_address"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * Create a <code>zone.system_operator_log</code> table reference
     */
    public SystemOperatorLog() {
        this(DSL.name("system_operator_log"), null);
    }

    /**
     * Create an aliased <code>zone.system_operator_log</code> table reference
     */
    public SystemOperatorLog(String alias) {
        this(DSL.name(alias), SYSTEM_OPERATOR_LOG);
    }

    /**
     * Create an aliased <code>zone.system_operator_log</code> table reference
     */
    public SystemOperatorLog(Name alias) {
        this(alias, SYSTEM_OPERATOR_LOG);
    }

    private SystemOperatorLog(Name alias, Table<SystemOperatorLogRecord> aliased) {
        this(alias, aliased, null);
    }

    private SystemOperatorLog(Name alias, Table<SystemOperatorLogRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> SystemOperatorLog(Table<O> child, ForeignKey<O, SystemOperatorLogRecord> key) {
        super(child, key, SYSTEM_OPERATOR_LOG);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SYSTEM_OPERATOR_LOG_PRIMARY);
    }

    @Override
    public UniqueKey<SystemOperatorLogRecord> getPrimaryKey() {
        return Keys.KEY_SYSTEM_OPERATOR_LOG_PRIMARY;
    }

    @Override
    public List<UniqueKey<SystemOperatorLogRecord>> getKeys() {
        return Arrays.<UniqueKey<SystemOperatorLogRecord>>asList(Keys.KEY_SYSTEM_OPERATOR_LOG_PRIMARY);
    }

    @Override
    public SystemOperatorLog as(String alias) {
        return new SystemOperatorLog(DSL.name(alias), this);
    }

    @Override
    public SystemOperatorLog as(Name alias) {
        return new SystemOperatorLog(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemOperatorLog rename(String name) {
        return new SystemOperatorLog(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemOperatorLog rename(Name name) {
        return new SystemOperatorLog(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, Timestamp, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
