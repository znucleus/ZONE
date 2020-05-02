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
import top.zbeboy.zone.domain.tables.records.OauthClientUsersRecord;


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
public class OauthClientUsers extends TableImpl<OauthClientUsersRecord> {

    private static final long serialVersionUID = 1354664560;

    /**
     * The reference instance of <code>zone.oauth_client_users</code>
     */
    public static final OauthClientUsers OAUTH_CLIENT_USERS = new OauthClientUsers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OauthClientUsersRecord> getRecordType() {
        return OauthClientUsersRecord.class;
    }

    /**
     * The column <code>zone.oauth_client_users.client_id</code>.
     */
    public final TableField<OauthClientUsersRecord, String> CLIENT_ID = createField(DSL.name("client_id"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>zone.oauth_client_users.username</code>.
     */
    public final TableField<OauthClientUsersRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>zone.oauth_client_users.app_name</code>.
     */
    public final TableField<OauthClientUsersRecord, String> APP_NAME = createField(DSL.name("app_name"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>zone.oauth_client_users.secret</code>.
     */
    public final TableField<OauthClientUsersRecord, String> SECRET = createField(DSL.name("secret"), org.jooq.impl.SQLDataType.VARCHAR(300).nullable(false), this, "");

    /**
     * The column <code>zone.oauth_client_users.remark</code>.
     */
    public final TableField<OauthClientUsersRecord, String> REMARK = createField(DSL.name("remark"), org.jooq.impl.SQLDataType.VARCHAR(300), this, "");

    /**
     * The column <code>zone.oauth_client_users.create_date</code>.
     */
    public final TableField<OauthClientUsersRecord, Timestamp> CREATE_DATE = createField(DSL.name("create_date"), org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * Create a <code>zone.oauth_client_users</code> table reference
     */
    public OauthClientUsers() {
        this(DSL.name("oauth_client_users"), null);
    }

    /**
     * Create an aliased <code>zone.oauth_client_users</code> table reference
     */
    public OauthClientUsers(String alias) {
        this(DSL.name(alias), OAUTH_CLIENT_USERS);
    }

    /**
     * Create an aliased <code>zone.oauth_client_users</code> table reference
     */
    public OauthClientUsers(Name alias) {
        this(alias, OAUTH_CLIENT_USERS);
    }

    private OauthClientUsers(Name alias, Table<OauthClientUsersRecord> aliased) {
        this(alias, aliased, null);
    }

    private OauthClientUsers(Name alias, Table<OauthClientUsersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> OauthClientUsers(Table<O> child, ForeignKey<O, OauthClientUsersRecord> key) {
        super(child, key, OAUTH_CLIENT_USERS);
    }

    @Override
    public Schema getSchema() {
        return Zone.ZONE;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.OAUTH_CLIENT_USERS_PRIMARY);
    }

    @Override
    public UniqueKey<OauthClientUsersRecord> getPrimaryKey() {
        return Keys.KEY_OAUTH_CLIENT_USERS_PRIMARY;
    }

    @Override
    public List<UniqueKey<OauthClientUsersRecord>> getKeys() {
        return Arrays.<UniqueKey<OauthClientUsersRecord>>asList(Keys.KEY_OAUTH_CLIENT_USERS_PRIMARY);
    }

    @Override
    public OauthClientUsers as(String alias) {
        return new OauthClientUsers(DSL.name(alias), this);
    }

    @Override
    public OauthClientUsers as(Name alias) {
        return new OauthClientUsers(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public OauthClientUsers rename(String name) {
        return new OauthClientUsers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public OauthClientUsers rename(Name name) {
        return new OauthClientUsers(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, String, String, String, String, Timestamp> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}
