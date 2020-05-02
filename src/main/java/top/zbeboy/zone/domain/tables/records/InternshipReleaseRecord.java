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
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.InternshipRelease;


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
public class InternshipReleaseRecord extends UpdatableRecordImpl<InternshipReleaseRecord> implements Record13<String, String, Timestamp, String, Timestamp, Timestamp, Timestamp, Timestamp, Byte, Integer, String, Integer, Byte> {

    private static final long serialVersionUID = 1687272769;

    /**
     * Setter for <code>zone.internship_release.internship_release_id</code>.
     */
    public void setInternshipReleaseId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.internship_release.internship_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getInternshipReleaseId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.internship_release.internship_title</code>.
     */
    public void setInternshipTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.internship_release.internship_title</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getInternshipTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.internship_release.release_time</code>.
     */
    public void setReleaseTime(Timestamp value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.internship_release.release_time</code>.
     */
    @NotNull
    public Timestamp getReleaseTime() {
        return (Timestamp) get(2);
    }

    /**
     * Setter for <code>zone.internship_release.username</code>.
     */
    public void setUsername(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.internship_release.username</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return (String) get(3);
    }

    /**
     * Setter for <code>zone.internship_release.teacher_distribution_start_time</code>.
     */
    public void setTeacherDistributionStartTime(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.internship_release.teacher_distribution_start_time</code>.
     */
    public Timestamp getTeacherDistributionStartTime() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>zone.internship_release.teacher_distribution_end_time</code>.
     */
    public void setTeacherDistributionEndTime(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.internship_release.teacher_distribution_end_time</code>.
     */
    public Timestamp getTeacherDistributionEndTime() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>zone.internship_release.start_time</code>.
     */
    public void setStartTime(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>zone.internship_release.start_time</code>.
     */
    public Timestamp getStartTime() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>zone.internship_release.end_time</code>.
     */
    public void setEndTime(Timestamp value) {
        set(7, value);
    }

    /**
     * Getter for <code>zone.internship_release.end_time</code>.
     */
    public Timestamp getEndTime() {
        return (Timestamp) get(7);
    }

    /**
     * Setter for <code>zone.internship_release.internship_release_is_del</code>.
     */
    public void setInternshipReleaseIsDel(Byte value) {
        set(8, value);
    }

    /**
     * Getter for <code>zone.internship_release.internship_release_is_del</code>.
     */
    @NotNull
    public Byte getInternshipReleaseIsDel() {
        return (Byte) get(8);
    }

    /**
     * Setter for <code>zone.internship_release.internship_type_id</code>.
     */
    public void setInternshipTypeId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>zone.internship_release.internship_type_id</code>.
     */
    @NotNull
    public Integer getInternshipTypeId() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>zone.internship_release.publisher</code>.
     */
    public void setPublisher(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>zone.internship_release.publisher</code>.
     */
    @NotNull
    @Size(max = 30)
    public String getPublisher() {
        return (String) get(10);
    }

    /**
     * Setter for <code>zone.internship_release.science_id</code>.
     */
    public void setScienceId(Integer value) {
        set(11, value);
    }

    /**
     * Getter for <code>zone.internship_release.science_id</code>.
     */
    @NotNull
    public Integer getScienceId() {
        return (Integer) get(11);
    }

    /**
     * Setter for <code>zone.internship_release.is_time_limit</code>.
     */
    public void setIsTimeLimit(Byte value) {
        set(12, value);
    }

    /**
     * Getter for <code>zone.internship_release.is_time_limit</code>.
     */
    public Byte getIsTimeLimit() {
        return (Byte) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<String, String, Timestamp, String, Timestamp, Timestamp, Timestamp, Timestamp, Byte, Integer, String, Integer, Byte> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<String, String, Timestamp, String, Timestamp, Timestamp, Timestamp, Timestamp, Byte, Integer, String, Integer, Byte> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return InternshipRelease.INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_ID;
    }

    @Override
    public Field<String> field2() {
        return InternshipRelease.INTERNSHIP_RELEASE.INTERNSHIP_TITLE;
    }

    @Override
    public Field<Timestamp> field3() {
        return InternshipRelease.INTERNSHIP_RELEASE.RELEASE_TIME;
    }

    @Override
    public Field<String> field4() {
        return InternshipRelease.INTERNSHIP_RELEASE.USERNAME;
    }

    @Override
    public Field<Timestamp> field5() {
        return InternshipRelease.INTERNSHIP_RELEASE.TEACHER_DISTRIBUTION_START_TIME;
    }

    @Override
    public Field<Timestamp> field6() {
        return InternshipRelease.INTERNSHIP_RELEASE.TEACHER_DISTRIBUTION_END_TIME;
    }

    @Override
    public Field<Timestamp> field7() {
        return InternshipRelease.INTERNSHIP_RELEASE.START_TIME;
    }

    @Override
    public Field<Timestamp> field8() {
        return InternshipRelease.INTERNSHIP_RELEASE.END_TIME;
    }

    @Override
    public Field<Byte> field9() {
        return InternshipRelease.INTERNSHIP_RELEASE.INTERNSHIP_RELEASE_IS_DEL;
    }

    @Override
    public Field<Integer> field10() {
        return InternshipRelease.INTERNSHIP_RELEASE.INTERNSHIP_TYPE_ID;
    }

    @Override
    public Field<String> field11() {
        return InternshipRelease.INTERNSHIP_RELEASE.PUBLISHER;
    }

    @Override
    public Field<Integer> field12() {
        return InternshipRelease.INTERNSHIP_RELEASE.SCIENCE_ID;
    }

    @Override
    public Field<Byte> field13() {
        return InternshipRelease.INTERNSHIP_RELEASE.IS_TIME_LIMIT;
    }

    @Override
    public String component1() {
        return getInternshipReleaseId();
    }

    @Override
    public String component2() {
        return getInternshipTitle();
    }

    @Override
    public Timestamp component3() {
        return getReleaseTime();
    }

    @Override
    public String component4() {
        return getUsername();
    }

    @Override
    public Timestamp component5() {
        return getTeacherDistributionStartTime();
    }

    @Override
    public Timestamp component6() {
        return getTeacherDistributionEndTime();
    }

    @Override
    public Timestamp component7() {
        return getStartTime();
    }

    @Override
    public Timestamp component8() {
        return getEndTime();
    }

    @Override
    public Byte component9() {
        return getInternshipReleaseIsDel();
    }

    @Override
    public Integer component10() {
        return getInternshipTypeId();
    }

    @Override
    public String component11() {
        return getPublisher();
    }

    @Override
    public Integer component12() {
        return getScienceId();
    }

    @Override
    public Byte component13() {
        return getIsTimeLimit();
    }

    @Override
    public String value1() {
        return getInternshipReleaseId();
    }

    @Override
    public String value2() {
        return getInternshipTitle();
    }

    @Override
    public Timestamp value3() {
        return getReleaseTime();
    }

    @Override
    public String value4() {
        return getUsername();
    }

    @Override
    public Timestamp value5() {
        return getTeacherDistributionStartTime();
    }

    @Override
    public Timestamp value6() {
        return getTeacherDistributionEndTime();
    }

    @Override
    public Timestamp value7() {
        return getStartTime();
    }

    @Override
    public Timestamp value8() {
        return getEndTime();
    }

    @Override
    public Byte value9() {
        return getInternshipReleaseIsDel();
    }

    @Override
    public Integer value10() {
        return getInternshipTypeId();
    }

    @Override
    public String value11() {
        return getPublisher();
    }

    @Override
    public Integer value12() {
        return getScienceId();
    }

    @Override
    public Byte value13() {
        return getIsTimeLimit();
    }

    @Override
    public InternshipReleaseRecord value1(String value) {
        setInternshipReleaseId(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value2(String value) {
        setInternshipTitle(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value3(Timestamp value) {
        setReleaseTime(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value4(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value5(Timestamp value) {
        setTeacherDistributionStartTime(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value6(Timestamp value) {
        setTeacherDistributionEndTime(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value7(Timestamp value) {
        setStartTime(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value8(Timestamp value) {
        setEndTime(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value9(Byte value) {
        setInternshipReleaseIsDel(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value10(Integer value) {
        setInternshipTypeId(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value11(String value) {
        setPublisher(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value12(Integer value) {
        setScienceId(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord value13(Byte value) {
        setIsTimeLimit(value);
        return this;
    }

    @Override
    public InternshipReleaseRecord values(String value1, String value2, Timestamp value3, String value4, Timestamp value5, Timestamp value6, Timestamp value7, Timestamp value8, Byte value9, Integer value10, String value11, Integer value12, Byte value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InternshipReleaseRecord
     */
    public InternshipReleaseRecord() {
        super(InternshipRelease.INTERNSHIP_RELEASE);
    }

    /**
     * Create a detached, initialised InternshipReleaseRecord
     */
    public InternshipReleaseRecord(String internshipReleaseId, String internshipTitle, Timestamp releaseTime, String username, Timestamp teacherDistributionStartTime, Timestamp teacherDistributionEndTime, Timestamp startTime, Timestamp endTime, Byte internshipReleaseIsDel, Integer internshipTypeId, String publisher, Integer scienceId, Byte isTimeLimit) {
        super(InternshipRelease.INTERNSHIP_RELEASE);

        set(0, internshipReleaseId);
        set(1, internshipTitle);
        set(2, releaseTime);
        set(3, username);
        set(4, teacherDistributionStartTime);
        set(5, teacherDistributionEndTime);
        set(6, startTime);
        set(7, endTime);
        set(8, internshipReleaseIsDel);
        set(9, internshipTypeId);
        set(10, publisher);
        set(11, scienceId);
        set(12, isTimeLimit);
    }
}
