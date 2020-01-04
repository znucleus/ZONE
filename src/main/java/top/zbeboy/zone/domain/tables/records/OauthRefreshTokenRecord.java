/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import javax.annotation.Generated;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;

import top.zbeboy.zone.domain.tables.OauthRefreshToken;


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
public class OauthRefreshTokenRecord extends TableRecordImpl<OauthRefreshTokenRecord> implements Record3<String, byte[], byte[]> {

    private static final long serialVersionUID = 1020618780;

    /**
     * Setter for <code>zone.oauth_refresh_token.token_id</code>.
     */
    public void setTokenId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.oauth_refresh_token.token_id</code>.
     */
    @Size(max = 255)
    public String getTokenId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.oauth_refresh_token.token</code>.
     */
    public void setToken(byte... value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.oauth_refresh_token.token</code>.
     */
    public byte[] getToken() {
        return (byte[]) get(1);
    }

    /**
     * Setter for <code>zone.oauth_refresh_token.authentication</code>.
     */
    public void setAuthentication(byte... value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.oauth_refresh_token.authentication</code>.
     */
    public byte[] getAuthentication() {
        return (byte[]) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, byte[], byte[]> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, byte[], byte[]> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return OauthRefreshToken.OAUTH_REFRESH_TOKEN.TOKEN_ID;
    }

    @Override
    public Field<byte[]> field2() {
        return OauthRefreshToken.OAUTH_REFRESH_TOKEN.TOKEN;
    }

    @Override
    public Field<byte[]> field3() {
        return OauthRefreshToken.OAUTH_REFRESH_TOKEN.AUTHENTICATION;
    }

    @Override
    public String component1() {
        return getTokenId();
    }

    @Override
    public byte[] component2() {
        return getToken();
    }

    @Override
    public byte[] component3() {
        return getAuthentication();
    }

    @Override
    public String value1() {
        return getTokenId();
    }

    @Override
    public byte[] value2() {
        return getToken();
    }

    @Override
    public byte[] value3() {
        return getAuthentication();
    }

    @Override
    public OauthRefreshTokenRecord value1(String value) {
        setTokenId(value);
        return this;
    }

    @Override
    public OauthRefreshTokenRecord value2(byte... value) {
        setToken(value);
        return this;
    }

    @Override
    public OauthRefreshTokenRecord value3(byte... value) {
        setAuthentication(value);
        return this;
    }

    @Override
    public OauthRefreshTokenRecord values(String value1, byte[] value2, byte[] value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OauthRefreshTokenRecord
     */
    public OauthRefreshTokenRecord() {
        super(OauthRefreshToken.OAUTH_REFRESH_TOKEN);
    }

    /**
     * Create a detached, initialised OauthRefreshTokenRecord
     */
    public OauthRefreshTokenRecord(String tokenId, byte[] token, byte[] authentication) {
        super(OauthRefreshToken.OAUTH_REFRESH_TOKEN);

        set(0, tokenId);
        set(1, token);
        set(2, authentication);
    }
}