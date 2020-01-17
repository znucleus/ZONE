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

import top.zbeboy.zone.domain.tables.AttendRelease;


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
public class AttendReleaseRecord extends UpdatableRecordImpl<AttendReleaseRecord> implements Record9<String, String, Timestamp, Timestamp, Byte, Timestamp, Integer, String, Timestamp> {

    private static final long serialVersionUID = -593368615;

    /**
     * Setter for <code>zone.attend_release.attend_release_id</code>.
     */
    public void setAttendReleaseId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.attend_release.attend_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getAttendReleaseId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.attend_release.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.attend_release.title</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.attend_release.attend_start_time</code>.
     */
    public void setAttendStartTime(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.attend_release.attend_start_time</code>.
     */
    @NotNull
    public Timestamp getAttendStartTime() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>zone.attend_release.attend_end_time</code>.
     */
    public void setAttendEndTime(Timestamp value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.attend_release.attend_end_time</code>.
     */
    @NotNull
    public Timestamp getAttendEndTime() {
        return (Timestamp) get(3);
    }

    /**
     * Setter for <code>zone.attend_release.is_auto</code>.
     */
    public void setIsAuto(Byte value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.attend_release.is_auto</code>.
     */
    public Byte getIsAuto() {
        return (Byte) get(4);
    }

    /**
     * Setter for <code>zone.attend_release.expire_date</code>.
     */
    public void setExpireDate(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.attend_release.expire_date</code>.
     */
    @NotNull
    public Timestamp getExpireDate() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>zone.attend_release.organize_id</code>.
     */
    public void setOrganizeId(Integer value) {
        set(6, value);
    }

    /**
     * Getter for <code>zone.attend_release.organize_id</code>.
     */
    @NotNull
    public Integer getOrganizeId() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>zone.attend_release.username</code>.
     */
    public void setUsername(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>zone.attend_release.username</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return (String) get(7);
    }

    /**
     * Setter for <code>zone.attend_release.release_time</code>.
     */
    public void setReleaseTime(Timestamp value) {
        set(8, value);
    }

    /**
     * Getter for <code>zone.attend_release.release_time</code>.
     */
    @NotNull
    public Timestamp getReleaseTime() {
        return (Timestamp) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, Timestamp, Timestamp, Byte, Timestamp, Integer, String, Timestamp> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<String, String, Timestamp, Timestamp, Byte, Timestamp, Integer, String, Timestamp> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return AttendRelease.ATTEND_RELEASE.ATTEND_RELEASE_ID;
    }

    @Override
    public Field<String> field2() {
        return AttendRelease.ATTEND_RELEASE.TITLE;
    }

    @Override
    public Field<Timestamp> field3() {
        return AttendRelease.ATTEND_RELEASE.ATTEND_START_TIME;
    }

    @Override
    public Field<Timestamp> field4() {
        return AttendRelease.ATTEND_RELEASE.ATTEND_END_TIME;
    }

    @Override
    public Field<Byte> field5() {
        return AttendRelease.ATTEND_RELEASE.IS_AUTO;
    }

    @Override
    public Field<Timestamp> field6() {
        return AttendRelease.ATTEND_RELEASE.EXPIRE_DATE;
    }

    @Override
    public Field<Integer> field7() {
        return AttendRelease.ATTEND_RELEASE.ORGANIZE_ID;
    }

    @Override
    public Field<String> field8() {
        return AttendRelease.ATTEND_RELEASE.USERNAME;
    }

    @Override
    public Field<Timestamp> field9() {
        return AttendRelease.ATTEND_RELEASE.RELEASE_TIME;
    }

    @Override
    public String component1() {
        return getAttendReleaseId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public Timestamp component3() {
        return getAttendStartTime();
    }

    @Override
    public Timestamp component4() {
        return getAttendEndTime();
    }

    @Override
    public Byte component5() {
        return getIsAuto();
    }

    @Override
    public Timestamp component6() {
        return getExpireDate();
    }

    @Override
    public Integer component7() {
        return getOrganizeId();
    }

    @Override
    public String component8() {
        return getUsername();
    }

    @Override
    public Timestamp component9() {
        return getReleaseTime();
    }

    @Override
    public String value1() {
        return getAttendReleaseId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public Timestamp value3() {
        return getAttendStartTime();
    }

    @Override
    public Timestamp value4() {
        return getAttendEndTime();
    }

    @Override
    public Byte value5() {
        return getIsAuto();
    }

    @Override
    public Timestamp value6() {
        return getExpireDate();
    }

    @Override
    public Integer value7() {
        return getOrganizeId();
    }

    @Override
    public String value8() {
        return getUsername();
    }

    @Override
    public Timestamp value9() {
        return getReleaseTime();
    }

    @Override
    public AttendReleaseRecord value1(String value) {
        setAttendReleaseId(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value3(Timestamp value) {
        setAttendStartTime(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value4(Timestamp value) {
        setAttendEndTime(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value5(Byte value) {
        setIsAuto(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value6(Timestamp value) {
        setExpireDate(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value7(Integer value) {
        setOrganizeId(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value8(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public AttendReleaseRecord value9(Timestamp value) {
        setReleaseTime(value);
        return this;
    }

    @Override
    public AttendReleaseRecord values(String value1, String value2, Timestamp value3, Timestamp value4, Byte value5, Timestamp value6, Integer value7, String value8, Timestamp value9) {
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
     * Create a detached AttendReleaseRecord
     */
    public AttendReleaseRecord() {
        super(AttendRelease.ATTEND_RELEASE);
    }

    /**
     * Create a detached, initialised AttendReleaseRecord
     */
    public AttendReleaseRecord(String attendReleaseId, String title, Timestamp attendStartTime, Timestamp attendEndTime, Byte isAuto, Timestamp expireDate, Integer organizeId, String username, Timestamp releaseTime) {
        super(AttendRelease.ATTEND_RELEASE);

        set(0, attendReleaseId);
        set(1, title);
        set(2, attendStartTime);
        set(3, attendEndTime);
        set(4, isAuto);
        set(5, expireDate);
        set(6, organizeId);
        set(7, username);
        set(8, releaseTime);
    }
}