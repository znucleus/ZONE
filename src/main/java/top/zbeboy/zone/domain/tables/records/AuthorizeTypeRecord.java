/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.AuthorizeType;


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
public class AuthorizeTypeRecord extends UpdatableRecordImpl<AuthorizeTypeRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = -1956878479;

    /**
     * Setter for <code>zone.authorize_type.authorize_type_id</code>.
     */
    public void setAuthorizeTypeId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.authorize_type.authorize_type_id</code>.
     */
    public Integer getAuthorizeTypeId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>zone.authorize_type.authorize_type_name</code>.
     */
    public void setAuthorizeTypeName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.authorize_type.authorize_type_name</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getAuthorizeTypeName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return AuthorizeType.AUTHORIZE_TYPE.AUTHORIZE_TYPE_ID;
    }

    @Override
    public Field<String> field2() {
        return AuthorizeType.AUTHORIZE_TYPE.AUTHORIZE_TYPE_NAME;
    }

    @Override
    public Integer component1() {
        return getAuthorizeTypeId();
    }

    @Override
    public String component2() {
        return getAuthorizeTypeName();
    }

    @Override
    public Integer value1() {
        return getAuthorizeTypeId();
    }

    @Override
    public String value2() {
        return getAuthorizeTypeName();
    }

    @Override
    public AuthorizeTypeRecord value1(Integer value) {
        setAuthorizeTypeId(value);
        return this;
    }

    @Override
    public AuthorizeTypeRecord value2(String value) {
        setAuthorizeTypeName(value);
        return this;
    }

    @Override
    public AuthorizeTypeRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AuthorizeTypeRecord
     */
    public AuthorizeTypeRecord() {
        super(AuthorizeType.AUTHORIZE_TYPE);
    }

    /**
     * Create a detached, initialised AuthorizeTypeRecord
     */
    public AuthorizeTypeRecord(Integer authorizeTypeId, String authorizeTypeName) {
        super(AuthorizeType.AUTHORIZE_TYPE);

        set(0, authorizeTypeId);
        set(1, authorizeTypeName);
    }
}