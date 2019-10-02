/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zhe;
import top.zbeboy.zone.domain.tables.records.UsersTypeRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersType extends TableImpl<UsersTypeRecord> {

    private static final long serialVersionUID = -1724108974;

    /**
     * The reference instance of <code>zhe.users_type</code>
     */
    public static final UsersType USERS_TYPE = new UsersType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UsersTypeRecord> getRecordType() {
        return UsersTypeRecord.class;
    }

    /**
     * The column <code>zhe.users_type.users_type_id</code>.
     */
    public final TableField<UsersTypeRecord, Integer> USERS_TYPE_ID = createField("users_type_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>zhe.users_type.users_type_name</code>.
     */
    public final TableField<UsersTypeRecord, String> USERS_TYPE_NAME = createField("users_type_name", org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * Create a <code>zhe.users_type</code> table reference
     */
    public UsersType() {
        this(DSL.name("users_type"), null);
    }

    /**
     * Create an aliased <code>zhe.users_type</code> table reference
     */
    public UsersType(String alias) {
        this(DSL.name(alias), USERS_TYPE);
    }

    /**
     * Create an aliased <code>zhe.users_type</code> table reference
     */
    public UsersType(Name alias) {
        this(alias, USERS_TYPE);
    }

    private UsersType(Name alias, Table<UsersTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private UsersType(Name alias, Table<UsersTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> UsersType(Table<O> child, ForeignKey<O, UsersTypeRecord> key) {
        super(child, key, USERS_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Zhe.ZHE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.USERS_TYPE_PRIMARY, Indexes.USERS_TYPE_USERS_TYPE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<UsersTypeRecord, Integer> getIdentity() {
        return Keys.IDENTITY_USERS_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UsersTypeRecord> getPrimaryKey() {
        return Keys.KEY_USERS_TYPE_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UsersTypeRecord>> getKeys() {
        return Arrays.<UniqueKey<UsersTypeRecord>>asList(Keys.KEY_USERS_TYPE_PRIMARY, Keys.KEY_USERS_TYPE_USERS_TYPE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersType as(String alias) {
        return new UsersType(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsersType as(Name alias) {
        return new UsersType(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UsersType rename(String name) {
        return new UsersType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UsersType rename(Name name) {
        return new UsersType(name, null);
    }
}
