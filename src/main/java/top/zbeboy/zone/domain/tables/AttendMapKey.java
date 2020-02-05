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
import top.zbeboy.zone.domain.tables.records.AttendMapKeyRecord;


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
public class AttendMapKey extends TableImpl<AttendMapKeyRecord> {

    private static final long serialVersionUID = 1534752163;

    /**
     * The reference instance of <code>zone.attend_map_key</code>
     */
    public static final AttendMapKey ATTEND_MAP_KEY = new AttendMapKey();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AttendMapKeyRecord> getRecordType() {
        return AttendMapKeyRecord.class;
    }

    /**
     * The column <code>zone.attend_map_key.id</code>.
     */
    public final TableField<AttendMapKeyRecord, Integer> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>zone.attend_map_key.map_key</code>.
     */
    public final TableField<AttendMapKeyRecord, String> MAP_KEY = createField(DSL.name("map_key"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * Create a <code>zone.attend_map_key</code> table reference
     */
    public AttendMapKey() {
        this(DSL.name("attend_map_key"), null);
    }

    /**
     * Create an aliased <code>zone.attend_map_key</code> table reference
     */
    public AttendMapKey(String alias) {
        this(DSL.name(alias), ATTEND_MAP_KEY);
    }

    /**
     * Create an aliased <code>zone.attend_map_key</code> table reference
     */
    public AttendMapKey(Name alias) {
        this(alias, ATTEND_MAP_KEY);
    }

    private AttendMapKey(Name alias, Table<AttendMapKeyRecord> aliased) {
        this(alias, aliased, null);
    }

    private AttendMapKey(Name alias, Table<AttendMapKeyRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> AttendMapKey(Table<O> child, ForeignKey<O, AttendMapKeyRecord> key) {
        super(child, key, ATTEND_MAP_KEY);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ATTEND_MAP_KEY_MAP_KEY, Indexes.ATTEND_MAP_KEY_PRIMARY);
    }

    @Override
    public UniqueKey<AttendMapKeyRecord> getPrimaryKey() {
        return Keys.KEY_ATTEND_MAP_KEY_PRIMARY;
    }

    @Override
    public List<UniqueKey<AttendMapKeyRecord>> getKeys() {
        return Arrays.<UniqueKey<AttendMapKeyRecord>>asList(Keys.KEY_ATTEND_MAP_KEY_PRIMARY, Keys.KEY_ATTEND_MAP_KEY_MAP_KEY);
    }

    @Override
    public AttendMapKey as(String alias) {
        return new AttendMapKey(DSL.name(alias), this);
    }

    @Override
    public AttendMapKey as(Name alias) {
        return new AttendMapKey(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AttendMapKey rename(String name) {
        return new AttendMapKey(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AttendMapKey rename(Name name) {
        return new AttendMapKey(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
