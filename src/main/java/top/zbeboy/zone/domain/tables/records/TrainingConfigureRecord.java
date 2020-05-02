/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.records;


import java.sql.Time;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.TrainingConfigure;


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
public class TrainingConfigureRecord extends UpdatableRecordImpl<TrainingConfigureRecord> implements Record6<String, Byte, Time, Time, Integer, String> {

    private static final long serialVersionUID = -142212766;

    /**
     * Setter for <code>zone.training_configure.training_configure_id</code>.
     */
    public void setTrainingConfigureId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.training_configure.training_configure_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getTrainingConfigureId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.training_configure.week_day</code>.
     */
    public void setWeekDay(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.training_configure.week_day</code>.
     */
    @NotNull
    public Byte getWeekDay() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>zone.training_configure.start_time</code>.
     */
    public void setStartTime(Time value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.training_configure.start_time</code>.
     */
    @NotNull
    public Time getStartTime() {
        return (Time) get(2);
    }

    /**
     * Setter for <code>zone.training_configure.end_time</code>.
     */
    public void setEndTime(Time value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.training_configure.end_time</code>.
     */
    @NotNull
    public Time getEndTime() {
        return (Time) get(3);
    }

    /**
     * Setter for <code>zone.training_configure.schoolroom_id</code>.
     */
    public void setSchoolroomId(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.training_configure.schoolroom_id</code>.
     */
    @NotNull
    public Integer getSchoolroomId() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>zone.training_configure.training_release_id</code>.
     */
    public void setTrainingReleaseId(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.training_configure.training_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getTrainingReleaseId() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, Byte, Time, Time, Integer, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<String, Byte, Time, Time, Integer, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TrainingConfigure.TRAINING_CONFIGURE.TRAINING_CONFIGURE_ID;
    }

    @Override
    public Field<Byte> field2() {
        return TrainingConfigure.TRAINING_CONFIGURE.WEEK_DAY;
    }

    @Override
    public Field<Time> field3() {
        return TrainingConfigure.TRAINING_CONFIGURE.START_TIME;
    }

    @Override
    public Field<Time> field4() {
        return TrainingConfigure.TRAINING_CONFIGURE.END_TIME;
    }

    @Override
    public Field<Integer> field5() {
        return TrainingConfigure.TRAINING_CONFIGURE.SCHOOLROOM_ID;
    }

    @Override
    public Field<String> field6() {
        return TrainingConfigure.TRAINING_CONFIGURE.TRAINING_RELEASE_ID;
    }

    @Override
    public String component1() {
        return getTrainingConfigureId();
    }

    @Override
    public Byte component2() {
        return getWeekDay();
    }

    @Override
    public Time component3() {
        return getStartTime();
    }

    @Override
    public Time component4() {
        return getEndTime();
    }

    @Override
    public Integer component5() {
        return getSchoolroomId();
    }

    @Override
    public String component6() {
        return getTrainingReleaseId();
    }

    @Override
    public String value1() {
        return getTrainingConfigureId();
    }

    @Override
    public Byte value2() {
        return getWeekDay();
    }

    @Override
    public Time value3() {
        return getStartTime();
    }

    @Override
    public Time value4() {
        return getEndTime();
    }

    @Override
    public Integer value5() {
        return getSchoolroomId();
    }

    @Override
    public String value6() {
        return getTrainingReleaseId();
    }

    @Override
    public TrainingConfigureRecord value1(String value) {
        setTrainingConfigureId(value);
        return this;
    }

    @Override
    public TrainingConfigureRecord value2(Byte value) {
        setWeekDay(value);
        return this;
    }

    @Override
    public TrainingConfigureRecord value3(Time value) {
        setStartTime(value);
        return this;
    }

    @Override
    public TrainingConfigureRecord value4(Time value) {
        setEndTime(value);
        return this;
    }

    @Override
    public TrainingConfigureRecord value5(Integer value) {
        setSchoolroomId(value);
        return this;
    }

    @Override
    public TrainingConfigureRecord value6(String value) {
        setTrainingReleaseId(value);
        return this;
    }

    @Override
    public TrainingConfigureRecord values(String value1, Byte value2, Time value3, Time value4, Integer value5, String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TrainingConfigureRecord
     */
    public TrainingConfigureRecord() {
        super(TrainingConfigure.TRAINING_CONFIGURE);
    }

    /**
     * Create a detached, initialised TrainingConfigureRecord
     */
    public TrainingConfigureRecord(String trainingConfigureId, Byte weekDay, Time startTime, Time endTime, Integer schoolroomId, String trainingReleaseId) {
        super(TrainingConfigure.TRAINING_CONFIGURE);

        set(0, trainingConfigureId);
        set(1, weekDay);
        set(2, startTime);
        set(3, endTime);
        set(4, schoolroomId);
        set(5, trainingReleaseId);
    }
}
