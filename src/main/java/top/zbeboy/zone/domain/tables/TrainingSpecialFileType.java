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
import top.zbeboy.zone.domain.tables.records.TrainingSpecialFileTypeRecord;


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
public class TrainingSpecialFileType extends TableImpl<TrainingSpecialFileTypeRecord> {

    private static final long serialVersionUID = 139295642;

    /**
     * The reference instance of <code>zone.training_special_file_type</code>
     */
    public static final TrainingSpecialFileType TRAINING_SPECIAL_FILE_TYPE = new TrainingSpecialFileType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TrainingSpecialFileTypeRecord> getRecordType() {
        return TrainingSpecialFileTypeRecord.class;
    }

    /**
     * The column <code>zone.training_special_file_type.file_type_id</code>.
     */
    public final TableField<TrainingSpecialFileTypeRecord, String> FILE_TYPE_ID = createField(DSL.name("file_type_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.training_special_file_type.file_type_name</code>.
     */
    public final TableField<TrainingSpecialFileTypeRecord, String> FILE_TYPE_NAME = createField(DSL.name("file_type_name"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>zone.training_special_file_type.training_special_id</code>.
     */
    public final TableField<TrainingSpecialFileTypeRecord, String> TRAINING_SPECIAL_ID = createField(DSL.name("training_special_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * Create a <code>zone.training_special_file_type</code> table reference
     */
    public TrainingSpecialFileType() {
        this(DSL.name("training_special_file_type"), null);
    }

    /**
     * Create an aliased <code>zone.training_special_file_type</code> table reference
     */
    public TrainingSpecialFileType(String alias) {
        this(DSL.name(alias), TRAINING_SPECIAL_FILE_TYPE);
    }

    /**
     * Create an aliased <code>zone.training_special_file_type</code> table reference
     */
    public TrainingSpecialFileType(Name alias) {
        this(alias, TRAINING_SPECIAL_FILE_TYPE);
    }

    private TrainingSpecialFileType(Name alias, Table<TrainingSpecialFileTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private TrainingSpecialFileType(Name alias, Table<TrainingSpecialFileTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> TrainingSpecialFileType(Table<O> child, ForeignKey<O, TrainingSpecialFileTypeRecord> key) {
        super(child, key, TRAINING_SPECIAL_FILE_TYPE);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.TRAINING_SPECIAL_FILE_TYPE_PRIMARY, Indexes.TRAINING_SPECIAL_FILE_TYPE_TRAINING_SPECIAL_ID);
    }

    @Override
    public UniqueKey<TrainingSpecialFileTypeRecord> getPrimaryKey() {
        return Keys.KEY_TRAINING_SPECIAL_FILE_TYPE_PRIMARY;
    }

    @Override
    public List<UniqueKey<TrainingSpecialFileTypeRecord>> getKeys() {
        return Arrays.<UniqueKey<TrainingSpecialFileTypeRecord>>asList(Keys.KEY_TRAINING_SPECIAL_FILE_TYPE_PRIMARY);
    }

    @Override
    public List<ForeignKey<TrainingSpecialFileTypeRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<TrainingSpecialFileTypeRecord, ?>>asList(Keys.TRAINING_SPECIAL_FILE_TYPE_IBFK_1);
    }

    public TrainingSpecial trainingSpecial() {
        return new TrainingSpecial(this, Keys.TRAINING_SPECIAL_FILE_TYPE_IBFK_1);
    }

    @Override
    public TrainingSpecialFileType as(String alias) {
        return new TrainingSpecialFileType(DSL.name(alias), this);
    }

    @Override
    public TrainingSpecialFileType as(Name alias) {
        return new TrainingSpecialFileType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingSpecialFileType rename(String name) {
        return new TrainingSpecialFileType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TrainingSpecialFileType rename(Name name) {
        return new TrainingSpecialFileType(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
