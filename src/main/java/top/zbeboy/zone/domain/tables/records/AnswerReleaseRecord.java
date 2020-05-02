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
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.AnswerRelease;


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
public class AnswerReleaseRecord extends UpdatableRecordImpl<AnswerReleaseRecord> implements Record7<String, String, Timestamp, Timestamp, String, String, Timestamp> {

    private static final long serialVersionUID = 1570269840;

    /**
     * Setter for <code>zone.answer_release.answer_release_id</code>.
     */
    public void setAnswerReleaseId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.answer_release.answer_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getAnswerReleaseId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.answer_release.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.answer_release.title</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.answer_release.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.answer_release.start_time</code>.
     */
    @NotNull
    public Timestamp getStartTime() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>zone.answer_release.end_time</code>.
     */
    public void setEndTime(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.answer_release.end_time</code>.
     */
    @NotNull
    public Timestamp getEndTime() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>zone.answer_release.answer_bank_id</code>.
     */
    public void setAnswerBankId(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.answer_release.answer_bank_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getAnswerBankId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>zone.answer_release.username</code>.
     */
    public void setUsername(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.answer_release.username</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return (String) get(5);
    }

    /**
     * Setter for <code>zone.answer_release.release_time</code>.
     */
    public void setReleaseTime(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>zone.answer_release.release_time</code>.
     */
    @NotNull
    public Timestamp getReleaseTime() {
        return (Timestamp) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, Timestamp, Timestamp, String, String, Timestamp> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, String, Timestamp, Timestamp, String, String, Timestamp> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return AnswerRelease.ANSWER_RELEASE.ANSWER_RELEASE_ID;
    }

    @Override
    public Field<String> field2() {
        return AnswerRelease.ANSWER_RELEASE.TITLE;
    }

    @Override
    public Field<Timestamp> field3() {
        return AnswerRelease.ANSWER_RELEASE.START_TIME;
    }

    @Override
    public Field<Timestamp> field4() {
        return AnswerRelease.ANSWER_RELEASE.END_TIME;
    }

    @Override
    public Field<String> field5() {
        return AnswerRelease.ANSWER_RELEASE.ANSWER_BANK_ID;
    }

    @Override
    public Field<String> field6() {
        return AnswerRelease.ANSWER_RELEASE.USERNAME;
    }

    @Override
    public Field<Timestamp> field7() {
        return AnswerRelease.ANSWER_RELEASE.RELEASE_TIME;
    }

    @Override
    public String component1() {
        return getAnswerReleaseId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public Timestamp component3() {
        return getStartTime();
    }

    @Override
    public Timestamp component4() {
        return getEndTime();
    }

    @Override
    public String component5() {
        return getAnswerBankId();
    }

    @Override
    public String component6() {
        return getUsername();
    }

    @Override
    public Timestamp component7() {
        return getReleaseTime();
    }

    @Override
    public String value1() {
        return getAnswerReleaseId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public Timestamp value3() {
        return getStartTime();
    }

    @Override
    public Timestamp value4() {
        return getEndTime();
    }

    @Override
    public String value5() {
        return getAnswerBankId();
    }

    @Override
    public String value6() {
        return getUsername();
    }

    @Override
    public Timestamp value7() {
        return getReleaseTime();
    }

    @Override
    public AnswerReleaseRecord value1(String value) {
        setAnswerReleaseId(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord value3(Timestamp value) {
        setStartTime(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord value4(Timestamp value) {
        setEndTime(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord value5(String value) {
        setAnswerBankId(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord value6(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord value7(Timestamp value) {
        setReleaseTime(value);
        return this;
    }

    @Override
    public AnswerReleaseRecord values(String value1, String value2, Timestamp value3, Timestamp value4, String value5, String value6, Timestamp value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AnswerReleaseRecord
     */
    public AnswerReleaseRecord() {
        super(AnswerRelease.ANSWER_RELEASE);
    }

    /**
     * Create a detached, initialised AnswerReleaseRecord
     */
    public AnswerReleaseRecord(String answerReleaseId, String title, Timestamp startTime, Timestamp endTime, String answerBankId, String username, Timestamp releaseTime) {
        super(AnswerRelease.ANSWER_RELEASE);

        set(0, answerReleaseId);
        set(1, title);
        set(2, startTime);
        set(3, endTime);
        set(4, answerBankId);
        set(5, username);
        set(6, releaseTime);
    }
}
