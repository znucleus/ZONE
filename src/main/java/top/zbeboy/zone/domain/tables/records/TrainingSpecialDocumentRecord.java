/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.TrainingSpecialDocument;


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
public class TrainingSpecialDocumentRecord extends UpdatableRecordImpl<TrainingSpecialDocumentRecord> implements Record7<String, String, String, String, Timestamp, Integer, String> {

    private static final long serialVersionUID = 943319098;

    /**
     * Setter for <code>zone.training_special_document.training_special_document_id</code>.
     */
    public void setTrainingSpecialDocumentId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.training_special_document.training_special_document_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getTrainingSpecialDocumentId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.training_special_document.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.training_special_document.title</code>.
     */
    @NotNull
    @Size(max = 200)
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.training_special_document.username</code>.
     */
    public void setUsername(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.training_special_document.username</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return (String) get(2);
    }

    /**
     * Setter for <code>zone.training_special_document.creator</code>.
     */
    public void setCreator(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.training_special_document.creator</code>.
     */
    @NotNull
    @Size(max = 30)
    public String getCreator() {
        return (String) get(3);
    }

    /**
     * Setter for <code>zone.training_special_document.create_date</code>.
     */
    public void setCreateDate(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.training_special_document.create_date</code>.
     */
    @NotNull
    public Timestamp getCreateDate() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>zone.training_special_document.reading</code>.
     */
    public void setReading(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.training_special_document.reading</code>.
     */
    public Integer getReading() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>zone.training_special_document.training_special_id</code>.
     */
    public void setTrainingSpecialId(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>zone.training_special_document.training_special_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getTrainingSpecialId() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, String, String, Timestamp, Integer, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, String, String, String, Timestamp, Integer, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.TRAINING_SPECIAL_DOCUMENT_ID;
    }

    @Override
    public Field<String> field2() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.TITLE;
    }

    @Override
    public Field<String> field3() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.USERNAME;
    }

    @Override
    public Field<String> field4() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.CREATOR;
    }

    @Override
    public Field<Timestamp> field5() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.CREATE_DATE;
    }

    @Override
    public Field<Integer> field6() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.READING;
    }

    @Override
    public Field<String> field7() {
        return TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT.TRAINING_SPECIAL_ID;
    }

    @Override
    public String component1() {
        return getTrainingSpecialDocumentId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public String component3() {
        return getUsername();
    }

    @Override
    public String component4() {
        return getCreator();
    }

    @Override
    public Timestamp component5() {
        return getCreateDate();
    }

    @Override
    public Integer component6() {
        return getReading();
    }

    @Override
    public String component7() {
        return getTrainingSpecialId();
    }

    @Override
    public String value1() {
        return getTrainingSpecialDocumentId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public String value3() {
        return getUsername();
    }

    @Override
    public String value4() {
        return getCreator();
    }

    @Override
    public Timestamp value5() {
        return getCreateDate();
    }

    @Override
    public Integer value6() {
        return getReading();
    }

    @Override
    public String value7() {
        return getTrainingSpecialId();
    }

    @Override
    public TrainingSpecialDocumentRecord value1(String value) {
        setTrainingSpecialDocumentId(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord value3(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord value4(String value) {
        setCreator(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord value5(Timestamp value) {
        setCreateDate(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord value6(Integer value) {
        setReading(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord value7(String value) {
        setTrainingSpecialId(value);
        return this;
    }

    @Override
    public TrainingSpecialDocumentRecord values(String value1, String value2, String value3, String value4, Timestamp value5, Integer value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TrainingSpecialDocumentRecord
     */
    public TrainingSpecialDocumentRecord() {
        super(TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT);
    }

    /**
     * Create a detached, initialised TrainingSpecialDocumentRecord
     */
    public TrainingSpecialDocumentRecord(String trainingSpecialDocumentId, String title, String username, String creator, Timestamp createDate, Integer reading, String trainingSpecialId) {
        super(TrainingSpecialDocument.TRAINING_SPECIAL_DOCUMENT);

        set(0, trainingSpecialDocumentId);
        set(1, title);
        set(2, username);
        set(3, creator);
        set(4, createDate);
        set(5, reading);
        set(6, trainingSpecialId);
    }
}
