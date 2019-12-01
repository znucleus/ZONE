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
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.SystemMailboxLog;


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
public class SystemMailboxLogRecord extends UpdatableRecordImpl<SystemMailboxLogRecord> implements Record4<String, Timestamp, String, String> {

    private static final long serialVersionUID = 2074571430;

    /**
     * Setter for <code>zone.system_mailbox_log.log_id</code>.
     */
    public void setLogId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.system_mailbox_log.log_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getLogId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.system_mailbox_log.send_time</code>.
     */
    public void setSendTime(Timestamp value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.system_mailbox_log.send_time</code>.
     */
    public Timestamp getSendTime() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>zone.system_mailbox_log.accept_mail</code>.
     */
    public void setAcceptMail(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.system_mailbox_log.accept_mail</code>.
     */
    @Size(max = 200)
    public String getAcceptMail() {
        return (String) get(2);
    }

    /**
     * Setter for <code>zone.system_mailbox_log.send_condition</code>.
     */
    public void setSendCondition(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.system_mailbox_log.send_condition</code>.
     */
    @Size(max = 500)
    public String getSendCondition() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, Timestamp, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, Timestamp, String, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return SystemMailboxLog.SYSTEM_MAILBOX_LOG.LOG_ID;
    }

    @Override
    public Field<Timestamp> field2() {
        return SystemMailboxLog.SYSTEM_MAILBOX_LOG.SEND_TIME;
    }

    @Override
    public Field<String> field3() {
        return SystemMailboxLog.SYSTEM_MAILBOX_LOG.ACCEPT_MAIL;
    }

    @Override
    public Field<String> field4() {
        return SystemMailboxLog.SYSTEM_MAILBOX_LOG.SEND_CONDITION;
    }

    @Override
    public String component1() {
        return getLogId();
    }

    @Override
    public Timestamp component2() {
        return getSendTime();
    }

    @Override
    public String component3() {
        return getAcceptMail();
    }

    @Override
    public String component4() {
        return getSendCondition();
    }

    @Override
    public String value1() {
        return getLogId();
    }

    @Override
    public Timestamp value2() {
        return getSendTime();
    }

    @Override
    public String value3() {
        return getAcceptMail();
    }

    @Override
    public String value4() {
        return getSendCondition();
    }

    @Override
    public SystemMailboxLogRecord value1(String value) {
        setLogId(value);
        return this;
    }

    @Override
    public SystemMailboxLogRecord value2(Timestamp value) {
        setSendTime(value);
        return this;
    }

    @Override
    public SystemMailboxLogRecord value3(String value) {
        setAcceptMail(value);
        return this;
    }

    @Override
    public SystemMailboxLogRecord value4(String value) {
        setSendCondition(value);
        return this;
    }

    @Override
    public SystemMailboxLogRecord values(String value1, Timestamp value2, String value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SystemMailboxLogRecord
     */
    public SystemMailboxLogRecord() {
        super(SystemMailboxLog.SYSTEM_MAILBOX_LOG);
    }

    /**
     * Create a detached, initialised SystemMailboxLogRecord
     */
    public SystemMailboxLogRecord(String logId, Timestamp sendTime, String acceptMail, String sendCondition) {
        super(SystemMailboxLog.SYSTEM_MAILBOX_LOG);

        set(0, logId);
        set(1, sendTime);
        set(2, acceptMail);
        set(3, sendCondition);
    }
}
