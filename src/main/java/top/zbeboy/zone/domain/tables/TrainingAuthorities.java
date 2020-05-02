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
import top.zbeboy.zone.domain.tables.records.TrainingAuthoritiesRecord;


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
public class TrainingAuthorities extends TableImpl<TrainingAuthoritiesRecord> {

    private static final long serialVersionUID = -1254116682;

    /**
     * The reference instance of <code>zone.training_authorities</code>
     */
    public static final TrainingAuthorities TRAINING_AUTHORITIES = new TrainingAuthorities();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TrainingAuthoritiesRecord> getRecordType() {
        return TrainingAuthoritiesRecord.class;
    }

    /**
     * The column <code>zone.training_authorities.authorities_id</code>.
     */
    public final TableField<TrainingAuthoritiesRecord, String> AUTHORITIES_ID = createField(DSL.name("authorities_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.training_authorities.training_release_id</code>.
     */
    public final TableField<TrainingAuthoritiesRecord, String> TRAINING_RELEASE_ID = createField(DSL.name("training_release_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.training_authorities.username</code>.
     */
    public final TableField<TrainingAuthoritiesRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.training_authorities.valid_date</code>.
     */
    public final TableField<TrainingAuthoritiesRecord, Timestamp> VALID_DATE = createField(DSL.name("valid_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>zone.training_authorities.expire_date</code>.
     */
    public final TableField<TrainingAuthoritiesRecord, Timestamp> EXPIRE_DATE = createField(DSL.name("expire_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>zone.training_authorities</code> table reference
     */
    public TrainingAuthorities() {
        this(DSL.name("training_authorities"), null);
    }

    /**
     * Create an aliased <code>zone.training_authorities</code> table reference
     */
    public TrainingAuthorities(String alias) {
        this(DSL.name(alias), TRAINING_AUTHORITIES);
    }

    /**
     * Create an aliased <code>zone.training_authorities</code> table reference
     */
    public TrainingAuthorities(Name alias) {
        this(alias, TRAINING_AUTHORITIES);
    }

    private TrainingAuthorities(Name alias, Table<TrainingAuthoritiesRecord> aliased) {
        this(alias, aliased, null);
    }

    private TrainingAuthorities(Name alias, Table<TrainingAuthoritiesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TrainingAuthorities(Table<O> child, ForeignKey<O, TrainingAuthoritiesRecord> key) {
        super(child, key, TRAINING_AUTHORITIES);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TRAINING_AUTHORITIES_PRIMARY, Indexes.TRAINING_AUTHORITIES_TRAINING_RELEASE_ID, Indexes.TRAINING_AUTHORITIES_USERNAME);
    }

    @Override
    public UniqueKey<TrainingAuthoritiesRecord> getPrimaryKey() {
        return Keys.KEY_TRAINING_AUTHORITIES_PRIMARY;
    }

    @Override
    public List<UniqueKey<TrainingAuthoritiesRecord>> getKeys() {
        return Arrays.<UniqueKey<TrainingAuthoritiesRecord>>asList(Keys.KEY_TRAINING_AUTHORITIES_PRIMARY);
    }

    @Override
    public List<ForeignKey<TrainingAuthoritiesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TrainingAuthoritiesRecord, ?>>asList(Keys.TRAINING_AUTHORITIES_IBFK_1, Keys.TRAINING_AUTHORITIES_IBFK_2);
    }

    public TrainingRelease trainingRelease() {
        return new TrainingRelease(this, Keys.TRAINING_AUTHORITIES_IBFK_1);
    }

    public Users users() {
        return new Users(this, Keys.TRAINING_AUTHORITIES_IBFK_2);
    }

    @Override
    public TrainingAuthorities as(String alias) {
        return new TrainingAuthorities(DSL.name(alias), this);
    }

    @Override
    public TrainingAuthorities as(Name alias) {
        return new TrainingAuthorities(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingAuthorities rename(String name) {
        return new TrainingAuthorities(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingAuthorities rename(Name name) {
        return new TrainingAuthorities(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, Timestamp, Timestamp> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
