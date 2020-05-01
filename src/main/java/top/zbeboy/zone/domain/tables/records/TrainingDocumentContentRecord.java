/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;

import top.zbeboy.zone.domain.tables.TrainingDocumentContent;


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
public class TrainingDocumentContentRecord extends TableRecordImpl<TrainingDocumentContentRecord> implements Record2<String, String> {

    private static final long serialVersionUID = -504642427;

    /**
     * Setter for <code>zone.training_document_content.training_document_id</code>.
     */
    public void setTrainingDocumentId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.training_document_content.training_document_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getTrainingDocumentId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.training_document_content.training_document_content</code>.
     */
    public void setTrainingDocumentContent(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.training_document_content.training_document_content</code>.
     */
    @NotNull
    public String getTrainingDocumentContent() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TrainingDocumentContent.TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_ID;
    }

    @Override
    public Field<String> field2() {
        return TrainingDocumentContent.TRAINING_DOCUMENT_CONTENT.TRAINING_DOCUMENT_CONTENT_;
    }

    @Override
    public String component1() {
        return getTrainingDocumentId();
    }

    @Override
    public String component2() {
        return getTrainingDocumentContent();
    }

    @Override
    public String value1() {
        return getTrainingDocumentId();
    }

    @Override
    public String value2() {
        return getTrainingDocumentContent();
    }

    @Override
    public TrainingDocumentContentRecord value1(String value) {
        setTrainingDocumentId(value);
        return this;
    }

    @Override
    public TrainingDocumentContentRecord value2(String value) {
        setTrainingDocumentContent(value);
        return this;
    }

    @Override
    public TrainingDocumentContentRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TrainingDocumentContentRecord
     */
    public TrainingDocumentContentRecord() {
        super(TrainingDocumentContent.TRAINING_DOCUMENT_CONTENT);
    }

    /**
     * Create a detached, initialised TrainingDocumentContentRecord
     */
    public TrainingDocumentContentRecord(String trainingDocumentId, String trainingDocumentContent) {
        super(TrainingDocumentContent.TRAINING_DOCUMENT_CONTENT);

        set(0, trainingDocumentId);
        set(1, trainingDocumentContent);
    }
}