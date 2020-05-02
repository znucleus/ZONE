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
import top.zbeboy.zone.domain.tables.records.InternshipReviewAuthorizeRecord;


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
public class InternshipReviewAuthorize extends TableImpl<InternshipReviewAuthorizeRecord> {

    private static final long serialVersionUID = 33603769;

    /**
     * The reference instance of <code>zone.internship_review_authorize</code>
     */
    public static final InternshipReviewAuthorize INTERNSHIP_REVIEW_AUTHORIZE = new InternshipReviewAuthorize();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InternshipReviewAuthorizeRecord> getRecordType() {
        return InternshipReviewAuthorizeRecord.class;
    }

    /**
     * The column <code>zone.internship_review_authorize.internship_release_id</code>.
     */
    public final TableField<InternshipReviewAuthorizeRecord, String> INTERNSHIP_RELEASE_ID = createField(DSL.name("internship_release_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.internship_review_authorize.username</code>.
     */
    public final TableField<InternshipReviewAuthorizeRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * Create a <code>zone.internship_review_authorize</code> table reference
     */
    public InternshipReviewAuthorize() {
        this(DSL.name("internship_review_authorize"), null);
    }

    /**
     * Create an aliased <code>zone.internship_review_authorize</code> table reference
     */
    public InternshipReviewAuthorize(String alias) {
        this(DSL.name(alias), INTERNSHIP_REVIEW_AUTHORIZE);
    }

    /**
     * Create an aliased <code>zone.internship_review_authorize</code> table reference
     */
    public InternshipReviewAuthorize(Name alias) {
        this(alias, INTERNSHIP_REVIEW_AUTHORIZE);
    }

    private InternshipReviewAuthorize(Name alias, Table<InternshipReviewAuthorizeRecord> aliased) {
        this(alias, aliased, null);
    }

    private InternshipReviewAuthorize(Name alias, Table<InternshipReviewAuthorizeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> InternshipReviewAuthorize(Table<O> child, ForeignKey<O, InternshipReviewAuthorizeRecord> key) {
        super(child, key, INTERNSHIP_REVIEW_AUTHORIZE);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.INTERNSHIP_REVIEW_AUTHORIZE_PRIMARY, Indexes.INTERNSHIP_REVIEW_AUTHORIZE_USERNAME);
    }

    @Override
    public UniqueKey<InternshipReviewAuthorizeRecord> getPrimaryKey() {
        return Keys.KEY_INTERNSHIP_REVIEW_AUTHORIZE_PRIMARY;
    }

    @Override
    public List<UniqueKey<InternshipReviewAuthorizeRecord>> getKeys() {
        return Arrays.<UniqueKey<InternshipReviewAuthorizeRecord>>asList(Keys.KEY_INTERNSHIP_REVIEW_AUTHORIZE_PRIMARY);
    }

    @Override
    public List<ForeignKey<InternshipReviewAuthorizeRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<InternshipReviewAuthorizeRecord, ?>>asList(Keys.INTERNSHIP_REVIEW_AUTHORIZE_IBFK_1, Keys.INTERNSHIP_REVIEW_AUTHORIZE_IBFK_2);
    }

    public InternshipRelease internshipRelease() {
        return new InternshipRelease(this, Keys.INTERNSHIP_REVIEW_AUTHORIZE_IBFK_1);
    }

    public Users users() {
        return new Users(this, Keys.INTERNSHIP_REVIEW_AUTHORIZE_IBFK_2);
    }

    @Override
    public InternshipReviewAuthorize as(String alias) {
        return new InternshipReviewAuthorize(DSL.name(alias), this);
    }

    @Override
    public InternshipReviewAuthorize as(Name alias) {
        return new InternshipReviewAuthorize(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public InternshipReviewAuthorize rename(String name) {
        return new InternshipReviewAuthorize(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public InternshipReviewAuthorize rename(Name name) {
        return new InternshipReviewAuthorize(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
