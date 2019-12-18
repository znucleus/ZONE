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
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import top.zbeboy.zone.domain.Indexes;
import top.zbeboy.zone.domain.Keys;
import top.zbeboy.zone.domain.Zone;
import top.zbeboy.zone.domain.tables.records.RoleUsersRecord;


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
public class RoleUsers extends TableImpl<RoleUsersRecord> {

    private static final long serialVersionUID = -805398152;

    /**
     * The reference instance of <code>zone.role_users</code>
     */
    public static final RoleUsers ROLE_USERS = new RoleUsers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoleUsersRecord> getRecordType() {
        return RoleUsersRecord.class;
    }

    /**
     * The column <code>zone.role_users.role_users_id</code>.
     */
    public final TableField<RoleUsersRecord, String> ROLE_USERS_ID = createField(DSL.name("role_users_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.username</code>.
     */
    public final TableField<RoleUsersRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.authorize_type_id</code>.
     */
    public final TableField<RoleUsersRecord, String> AUTHORIZE_TYPE_ID = createField(DSL.name("authorize_type_id"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.data_scope</code>.
     */
    public final TableField<RoleUsersRecord, Byte> DATA_SCOPE = createField(DSL.name("data_scope"), org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "");

    /**
     * The column <code>zone.role_users.data_id</code>.
     */
    public final TableField<RoleUsersRecord, String> DATA_ID = createField(DSL.name("data_id"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.role_id</code>.
     */
    public final TableField<RoleUsersRecord, String> ROLE_ID = createField(DSL.name("role_id"), org.jooq.impl.SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.duration</code>.
     */
    public final TableField<RoleUsersRecord, String> DURATION = createField(DSL.name("duration"), org.jooq.impl.SQLDataType.VARCHAR(5).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.reason</code>.
     */
    public final TableField<RoleUsersRecord, String> REASON = createField(DSL.name("reason"), org.jooq.impl.SQLDataType.VARCHAR(200).nullable(false), this, "");

    /**
     * The column <code>zone.role_users.valid_date</code>.
     */
    public final TableField<RoleUsersRecord, Timestamp> VALID_DATE = createField(DSL.name("valid_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>zone.role_users.expire_date</code>.
     */
    public final TableField<RoleUsersRecord, Timestamp> EXPIRE_DATE = createField(DSL.name("expire_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>zone.role_users.create_date</code>.
     */
    public final TableField<RoleUsersRecord, Timestamp> CREATE_DATE = createField(DSL.name("create_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>zone.role_users.apply_status</code>.
     */
    public final TableField<RoleUsersRecord, Byte> APPLY_STATUS = createField(DSL.name("apply_status"), org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "");

    /**
     * Create a <code>zone.role_users</code> table reference
     */
    public RoleUsers() {
        this(DSL.name("role_users"), null);
    }

    /**
     * Create an aliased <code>zone.role_users</code> table reference
     */
    public RoleUsers(String alias) {
        this(DSL.name(alias), ROLE_USERS);
    }

    /**
     * Create an aliased <code>zone.role_users</code> table reference
     */
    public RoleUsers(Name alias) {
        this(alias, ROLE_USERS);
    }

    private RoleUsers(Name alias, Table<RoleUsersRecord> aliased) {
        this(alias, aliased, null);
    }

    private RoleUsers(Name alias, Table<RoleUsersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> RoleUsers(Table<O> child, ForeignKey<O, RoleUsersRecord> key) {
        super(child, key, ROLE_USERS);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.ROLE_USERS_AUTHORIZE_TYPE_ID, Indexes.ROLE_USERS_PRIMARY, Indexes.ROLE_USERS_USERNAME);
    }

    @Override
    public UniqueKey<RoleUsersRecord> getPrimaryKey() {
        return Keys.KEY_ROLE_USERS_PRIMARY;
    }

    @Override
    public List<UniqueKey<RoleUsersRecord>> getKeys() {
        return Arrays.<UniqueKey<RoleUsersRecord>>asList(Keys.KEY_ROLE_USERS_PRIMARY);
    }

    @Override
    public List<ForeignKey<RoleUsersRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RoleUsersRecord, ?>>asList(Keys.ROLE_USERS_IBFK_1, Keys.ROLE_USERS_IBFK_2);
    }

    public Users users() {
        return new Users(this, Keys.ROLE_USERS_IBFK_1);
    }

    public AuthorizeType authorizeType() {
        return new AuthorizeType(this, Keys.ROLE_USERS_IBFK_2);
    }

    @Override
    public RoleUsers as(String alias) {
        return new RoleUsers(DSL.name(alias), this);
    }

    @Override
    public RoleUsers as(Name alias) {
        return new RoleUsers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RoleUsers rename(String name) {
        return new RoleUsers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RoleUsers rename(Name name) {
        return new RoleUsers(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<String, String, String, Byte, String, String, String, String, Timestamp, Timestamp, Timestamp, Byte> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}
