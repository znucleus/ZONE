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
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.WeiXin;


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
public class WeiXinRecord extends UpdatableRecordImpl<WeiXinRecord> implements Record9<Integer, String, String, String, String, String, String, String, Timestamp> {

    private static final long serialVersionUID = -282137710;

    /**
     * Setter for <code>zone.wei_xin.wei_xin_id</code>.
     */
    public void setWeiXinId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.wei_xin.wei_xin_id</code>.
     */
    public Integer getWeiXinId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>zone.wei_xin.open_id</code>.
     */
    public void setOpenId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.wei_xin.open_id</code>.
     */
    @NotNull
    @Size(max = 200)
    public String getOpenId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.wei_xin.session_key</code>.
     */
    public void setSessionKey(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.wei_xin.session_key</code>.
     */
    @Size(max = 300)
    public String getSessionKey() {
        return (String) get(2);
    }

    /**
     * Setter for <code>zone.wei_xin.union_id</code>.
     */
    public void setUnionId(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.wei_xin.union_id</code>.
     */
    @Size(max = 200)
    public String getUnionId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>zone.wei_xin.app_id</code>.
     */
    public void setAppId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.wei_xin.app_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getAppId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>zone.wei_xin.username</code>.
     */
    public void setUsername(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.wei_xin.username</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return (String) get(5);
    }

    /**
     * Setter for <code>zone.wei_xin.res_code</code>.
     */
    public void setResCode(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>zone.wei_xin.res_code</code>.
     */
    @Size(max = 200)
    public String getResCode() {
        return (String) get(6);
    }

    /**
     * Setter for <code>zone.wei_xin.result</code>.
     */
    public void setResult(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>zone.wei_xin.result</code>.
     */
    @Size(max = 500)
    public String getResult() {
        return (String) get(7);
    }

    /**
     * Setter for <code>zone.wei_xin.create_date</code>.
     */
    public void setCreateDate(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>zone.wei_xin.create_date</code>.
     */
    @NotNull
    public Timestamp getCreateDate() {
        return (Timestamp) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<Integer, String, String, String, String, String, String, String, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<Integer, String, String, String, String, String, String, String, Timestamp> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return WeiXin.WEI_XIN.WEI_XIN_ID;
    }

    @Override
    public Field<String> field2() {
        return WeiXin.WEI_XIN.OPEN_ID;
    }

    @Override
    public Field<String> field3() {
        return WeiXin.WEI_XIN.SESSION_KEY;
    }

    @Override
    public Field<String> field4() {
        return WeiXin.WEI_XIN.UNION_ID;
    }

    @Override
    public Field<String> field5() {
        return WeiXin.WEI_XIN.APP_ID;
    }

    @Override
    public Field<String> field6() {
        return WeiXin.WEI_XIN.USERNAME;
    }

    @Override
    public Field<String> field7() {
        return WeiXin.WEI_XIN.RES_CODE;
    }

    @Override
    public Field<String> field8() {
        return WeiXin.WEI_XIN.RESULT;
    }

    @Override
    public Field<Timestamp> field9() {
        return WeiXin.WEI_XIN.CREATE_DATE;
    }

    @Override
    public Integer component1() {
        return getWeiXinId();
    }

    @Override
    public String component2() {
        return getOpenId();
    }

    @Override
    public String component3() {
        return getSessionKey();
    }

    @Override
    public String component4() {
        return getUnionId();
    }

    @Override
    public String component5() {
        return getAppId();
    }

    @Override
    public String component6() {
        return getUsername();
    }

    @Override
    public String component7() {
        return getResCode();
    }

    @Override
    public String component8() {
        return getResult();
    }

    @Override
    public Timestamp component9() {
        return getCreateDate();
    }

    @Override
    public Integer value1() {
        return getWeiXinId();
    }

    @Override
    public String value2() {
        return getOpenId();
    }

    @Override
    public String value3() {
        return getSessionKey();
    }

    @Override
    public String value4() {
        return getUnionId();
    }

    @Override
    public String value5() {
        return getAppId();
    }

    @Override
    public String value6() {
        return getUsername();
    }

    @Override
    public String value7() {
        return getResCode();
    }

    @Override
    public String value8() {
        return getResult();
    }

    @Override
    public Timestamp value9() {
        return getCreateDate();
    }

    @Override
    public WeiXinRecord value1(Integer value) {
        setWeiXinId(value);
        return this;
    }

    @Override
    public WeiXinRecord value2(String value) {
        setOpenId(value);
        return this;
    }

    @Override
    public WeiXinRecord value3(String value) {
        setSessionKey(value);
        return this;
    }

    @Override
    public WeiXinRecord value4(String value) {
        setUnionId(value);
        return this;
    }

    @Override
    public WeiXinRecord value5(String value) {
        setAppId(value);
        return this;
    }

    @Override
    public WeiXinRecord value6(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public WeiXinRecord value7(String value) {
        setResCode(value);
        return this;
    }

    @Override
    public WeiXinRecord value8(String value) {
        setResult(value);
        return this;
    }

    @Override
    public WeiXinRecord value9(Timestamp value) {
        setCreateDate(value);
        return this;
    }

    @Override
    public WeiXinRecord values(Integer value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, Timestamp value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WeiXinRecord
     */
    public WeiXinRecord() {
        super(WeiXin.WEI_XIN);
    }

    /**
     * Create a detached, initialised WeiXinRecord
     */
    public WeiXinRecord(Integer weiXinId, String openId, String sessionKey, String unionId, String appId, String username, String resCode, String result, Timestamp createDate) {
        super(WeiXin.WEI_XIN);

        set(0, weiXinId);
        set(1, openId);
        set(2, sessionKey);
        set(3, unionId);
        set(4, appId);
        set(5, username);
        set(6, resCode);
        set(7, result);
        set(8, createDate);
    }
}