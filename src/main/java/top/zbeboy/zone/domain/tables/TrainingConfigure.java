/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables;


import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.TrainingConfigureRecord;


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
public class TrainingConfigure extends TableImpl<TrainingConfigureRecord> {

    private static final long serialVersionUID = -2143569533;

    /**
     * The reference instance of <code>zone.training_configure</code>
     */
    public static final TrainingConfigure TRAINING_CONFIGURE = new TrainingConfigure();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TrainingConfigureRecord> getRecordType() {
        return TrainingConfigureRecord.class;
    }

    /**
     * The column <code>zone.training_configure.training_configure_id</code>.
     */
    public final TableField<TrainingConfigureRecord, String> TRAINING_CONFIGURE_ID = createField(DSL.name("training_configure_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.training_configure.week_day</code>.
     */
    public final TableField<TrainingConfigureRecord, Byte> WEEK_DAY = createField(DSL.name("week_day"), org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "");

    /**
     * The column <code>zone.training_configure.start_time</code>.
     */
    public final TableField<TrainingConfigureRecord, Time> START_TIME = createField(DSL.name("start_time"), org.jooq.impl.SQLDataType.TIME.nullable(false), this, "");

    /**
     * The column <code>zone.training_configure.end_time</code>.
     */
    public final TableField<TrainingConfigureRecord, Time> END_TIME = createField(DSL.name("end_time"), org.jooq.impl.SQLDataType.TIME.nullable(false), this, "");

    /**
     * The column <code>zone.training_configure.schoolroom_id</code>.
     */
    public final TableField<TrainingConfigureRecord, Integer> SCHOOLROOM_ID = createField(DSL.name("schoolroom_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>zone.training_configure.training_release_id</code>.
     */
    public final TableField<TrainingConfigureRecord, String> TRAINING_RELEASE_ID = createField(DSL.name("training_release_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * Create a <code>zone.training_configure</code> table reference
     */
    public TrainingConfigure() {
        this(DSL.name("training_configure"), null);
    }

    /**
     * Create an aliased <code>zone.training_configure</code> table reference
     */
    public TrainingConfigure(String alias) {
        this(DSL.name(alias), TRAINING_CONFIGURE);
    }

    /**
     * Create an aliased <code>zone.training_configure</code> table reference
     */
    public TrainingConfigure(Name alias) {
        this(alias, TRAINING_CONFIGURE);
    }

    private TrainingConfigure(Name alias, Table<TrainingConfigureRecord> aliased) {
        this(alias, aliased, null);
    }

    private TrainingConfigure(Name alias, Table<TrainingConfigureRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TrainingConfigure(Table<O> child, ForeignKey<O, TrainingConfigureRecord> key) {
        super(child, key, TRAINING_CONFIGURE);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TRAINING_CONFIGURE_PRIMARY, Indexes.TRAINING_CONFIGURE_SCHOOLROOM_ID, Indexes.TRAINING_CONFIGURE_TRAINING_RELEASE_ID);
    }

    @Override
    public UniqueKey<TrainingConfigureRecord> getPrimaryKey() {
        return Keys.KEY_TRAINING_CONFIGURE_PRIMARY;
    }

    @Override
    public List<UniqueKey<TrainingConfigureRecord>> getKeys() {
        return Arrays.<UniqueKey<TrainingConfigureRecord>>asList(Keys.KEY_TRAINING_CONFIGURE_PRIMARY);
    }

    @Override
    public List<ForeignKey<TrainingConfigureRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TrainingConfigureRecord, ?>>asList(Keys.TRAINING_CONFIGURE_IBFK_1, Keys.TRAINING_CONFIGURE_IBFK_2);
    }

    public Schoolroom schoolroom() {
        return new Schoolroom(this, Keys.TRAINING_CONFIGURE_IBFK_1);
    }

    public TrainingRelease trainingRelease() {
        return new TrainingRelease(this, Keys.TRAINING_CONFIGURE_IBFK_2);
    }

    @Override
    public TrainingConfigure as(String alias) {
        return new TrainingConfigure(DSL.name(alias), this);
    }

    @Override
    public TrainingConfigure as(Name alias) {
        return new TrainingConfigure(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingConfigure rename(String name) {
        return new TrainingConfigure(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingConfigure rename(Name name) {
        return new TrainingConfigure(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, Byte, Time, Time, Integer, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}