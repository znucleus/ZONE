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
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.InternshipFile;


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
public class InternshipFileRecord extends UpdatableRecordImpl<InternshipFileRecord> implements Record2<String, String> {

    private static final long serialVersionUID = 622304353;

    /**
     * Setter for <code>zone.internship_file.internship_release_id</code>.
     */
    public void setInternshipReleaseId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.internship_file.internship_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getInternshipReleaseId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.internship_file.file_id</code>.
     */
    public void setFileId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.internship_file.file_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getFileId() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
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
        return InternshipFile.INTERNSHIP_FILE.INTERNSHIP_RELEASE_ID;
    }

    @Override
    public Field<String> field2() {
        return InternshipFile.INTERNSHIP_FILE.FILE_ID;
    }

    @Override
    public String component1() {
        return getInternshipReleaseId();
    }

    @Override
    public String component2() {
        return getFileId();
    }

    @Override
    public String value1() {
        return getInternshipReleaseId();
    }

    @Override
    public String value2() {
        return getFileId();
    }

    @Override
    public InternshipFileRecord value1(String value) {
        setInternshipReleaseId(value);
        return this;
    }

    @Override
    public InternshipFileRecord value2(String value) {
        setFileId(value);
        return this;
    }

    @Override
    public InternshipFileRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InternshipFileRecord
     */
    public InternshipFileRecord() {
        super(InternshipFile.INTERNSHIP_FILE);
    }

    /**
     * Create a detached, initialised InternshipFileRecord
     */
    public InternshipFileRecord(String internshipReleaseId, String fileId) {
        super(InternshipFile.INTERNSHIP_FILE);

        set(0, internshipReleaseId);
        set(1, fileId);
    }
}
