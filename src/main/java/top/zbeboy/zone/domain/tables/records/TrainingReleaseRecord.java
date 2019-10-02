/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.TrainingRelease;


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
public class TrainingReleaseRecord extends UpdatableRecordImpl<TrainingReleaseRecord> implements Record13<String, String, Date, Date, String, Time, Time, Integer, String, Integer, Integer, String, Timestamp> {

    private static final long serialVersionUID = -968499956;

    /**
     * Setter for <code>zhe.training_release.training_release_id</code>.
     */
    public void setTrainingReleaseId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zhe.training_release.training_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getTrainingReleaseId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zhe.training_release.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zhe.training_release.title</code>.
     */
    @NotNull
    @Size(max = 100)
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zhe.training_release.start_date</code>.
     */
    public void setStartDate(Date value) {
        set(2, value);
    }

    /**
     * Getter for <code>zhe.training_release.start_date</code>.
     */
    @NotNull
    public Date getStartDate() {
        return (Date) get(2);
    }

    /**
     * Setter for <code>zhe.training_release.end_date</code>.
     */
    public void setEndDate(Date value) {
        set(3, value);
    }

    /**
     * Getter for <code>zhe.training_release.end_date</code>.
     */
    @NotNull
    public Date getEndDate() {
        return (Date) get(3);
    }

    /**
     * Setter for <code>zhe.training_release.cycle</code>.
     */
    public void setCycle(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>zhe.training_release.cycle</code>.
     */
    @NotNull
    @Size(max = 20)
    public String getCycle() {
        return (String) get(4);
    }

    /**
     * Setter for <code>zhe.training_release.start_time</code>.
     */
    public void setStartTime(Time value) {
        set(5, value);
    }

    /**
     * Getter for <code>zhe.training_release.start_time</code>.
     */
    @NotNull
    public Time getStartTime() {
        return (Time) get(5);
    }

    /**
     * Setter for <code>zhe.training_release.end_time</code>.
     */
    public void setEndTime(Time value) {
        set(6, value);
    }

    /**
     * Getter for <code>zhe.training_release.end_time</code>.
     */
    @NotNull
    public Time getEndTime() {
        return (Time) get(6);
    }

    /**
     * Setter for <code>zhe.training_release.schoolroom_id</code>.
     */
    public void setSchoolroomId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>zhe.training_release.schoolroom_id</code>.
     */
    @NotNull
    public Integer getSchoolroomId() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>zhe.training_release.username</code>.
     */
    public void setUsername(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>zhe.training_release.username</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getUsername() {
        return (String) get(8);
    }

    /**
     * Setter for <code>zhe.training_release.course_id</code>.
     */
    public void setCourseId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>zhe.training_release.course_id</code>.
     */
    @NotNull
    public Integer getCourseId() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>zhe.training_release.organize_id</code>.
     */
    public void setOrganizeId(Integer value) {
        set(10, value);
    }

    /**
     * Getter for <code>zhe.training_release.organize_id</code>.
     */
    @NotNull
    public Integer getOrganizeId() {
        return (Integer) get(10);
    }

    /**
     * Setter for <code>zhe.training_release.publisher</code>.
     */
    public void setPublisher(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>zhe.training_release.publisher</code>.
     */
    @NotNull
    @Size(max = 30)
    public String getPublisher() {
        return (String) get(11);
    }

    /**
     * Setter for <code>zhe.training_release.release_time</code>.
     */
    public void setReleaseTime(Timestamp value) {
        set(12, value);
    }

    /**
     * Getter for <code>zhe.training_release.release_time</code>.
     */
    @NotNull
    public Timestamp getReleaseTime() {
        return (Timestamp) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<String, String, Date, Date, String, Time, Time, Integer, String, Integer, Integer, String, Timestamp> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row13<String, String, Date, Date, String, Time, Time, Integer, String, Integer, Integer, String, Timestamp> valuesRow() {
        return (Row13) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return TrainingRelease.TRAINING_RELEASE.TRAINING_RELEASE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return TrainingRelease.TRAINING_RELEASE.TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field3() {
        return TrainingRelease.TRAINING_RELEASE.START_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field4() {
        return TrainingRelease.TRAINING_RELEASE.END_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return TrainingRelease.TRAINING_RELEASE.CYCLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Time> field6() {
        return TrainingRelease.TRAINING_RELEASE.START_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Time> field7() {
        return TrainingRelease.TRAINING_RELEASE.END_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return TrainingRelease.TRAINING_RELEASE.SCHOOLROOM_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return TrainingRelease.TRAINING_RELEASE.USERNAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field10() {
        return TrainingRelease.TRAINING_RELEASE.COURSE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field11() {
        return TrainingRelease.TRAINING_RELEASE.ORGANIZE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return TrainingRelease.TRAINING_RELEASE.PUBLISHER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field13() {
        return TrainingRelease.TRAINING_RELEASE.RELEASE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getTrainingReleaseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date component3() {
        return getStartDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date component4() {
        return getEndDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getCycle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time component6() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time component7() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component8() {
        return getSchoolroomId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component10() {
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component11() {
        return getOrganizeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getPublisher();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component13() {
        return getReleaseTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getTrainingReleaseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value3() {
        return getStartDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value4() {
        return getEndDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getCycle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time value6() {
        return getStartTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time value7() {
        return getEndTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getSchoolroomId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value10() {
        return getCourseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value11() {
        return getOrganizeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getPublisher();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value13() {
        return getReleaseTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value1(String value) {
        setTrainingReleaseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value2(String value) {
        setTitle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value3(Date value) {
        setStartDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value4(Date value) {
        setEndDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value5(String value) {
        setCycle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value6(Time value) {
        setStartTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value7(Time value) {
        setEndTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value8(Integer value) {
        setSchoolroomId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value9(String value) {
        setUsername(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value10(Integer value) {
        setCourseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value11(Integer value) {
        setOrganizeId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value12(String value) {
        setPublisher(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord value13(Timestamp value) {
        setReleaseTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingReleaseRecord values(String value1, String value2, Date value3, Date value4, String value5, Time value6, Time value7, Integer value8, String value9, Integer value10, Integer value11, String value12, Timestamp value13) {
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
     * Create a detached TrainingReleaseRecord
     */
    public TrainingReleaseRecord() {
        super(TrainingRelease.TRAINING_RELEASE);
    }

    /**
     * Create a detached, initialised TrainingReleaseRecord
     */
    public TrainingReleaseRecord(String trainingReleaseId, String title, Date startDate, Date endDate, String cycle, Time startTime, Time endTime, Integer schoolroomId, String username, Integer courseId, Integer organizeId, String publisher, Timestamp releaseTime) {
        super(TrainingRelease.TRAINING_RELEASE);

        set(0, trainingReleaseId);
        set(1, title);
        set(2, startDate);
        set(3, endDate);
        set(4, cycle);
        set(5, startTime);
        set(6, endTime);
        set(7, schoolroomId);
        set(8, username);
        set(9, courseId);
        set(10, organizeId);
        set(11, publisher);
        set(12, releaseTime);
    }
}
