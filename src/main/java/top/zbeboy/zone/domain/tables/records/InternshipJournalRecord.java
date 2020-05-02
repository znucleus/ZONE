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
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;

import top.zbeboy.zone.domain.tables.InternshipJournal;


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
public class InternshipJournalRecord extends UpdatableRecordImpl<InternshipJournalRecord> implements Record12<String, String, String, String, String, String, Timestamp, Integer, String, Integer, String, Byte> {

    private static final long serialVersionUID = -1486497149;

    /**
     * Setter for <code>zone.internship_journal.internship_journal_id</code>.
     */
    public void setInternshipJournalId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>zone.internship_journal.internship_journal_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getInternshipJournalId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>zone.internship_journal.student_name</code>.
     */
    public void setStudentName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>zone.internship_journal.student_name</code>.
     */
    @NotNull
    @Size(max = 30)
    public String getStudentName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>zone.internship_journal.student_number</code>.
     */
    public void setStudentNumber(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>zone.internship_journal.student_number</code>.
     */
    @NotNull
    @Size(max = 20)
    public String getStudentNumber() {
        return (String) get(2);
    }

    /**
     * Setter for <code>zone.internship_journal.organize</code>.
     */
    public void setOrganize(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>zone.internship_journal.organize</code>.
     */
    @NotNull
    @Size(max = 200)
    public String getOrganize() {
        return (String) get(3);
    }

    /**
     * Setter for <code>zone.internship_journal.school_guidance_teacher</code>.
     */
    public void setSchoolGuidanceTeacher(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>zone.internship_journal.school_guidance_teacher</code>.
     */
    @NotNull
    @Size(max = 30)
    public String getSchoolGuidanceTeacher() {
        return (String) get(4);
    }

    /**
     * Setter for <code>zone.internship_journal.graduation_practice_company_name</code>.
     */
    public void setGraduationPracticeCompanyName(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>zone.internship_journal.graduation_practice_company_name</code>.
     */
    @NotNull
    @Size(max = 200)
    public String getGraduationPracticeCompanyName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>zone.internship_journal.create_date</code>.
     */
    public void setCreateDate(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>zone.internship_journal.create_date</code>.
     */
    @NotNull
    public Timestamp getCreateDate() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>zone.internship_journal.student_id</code>.
     */
    public void setStudentId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>zone.internship_journal.student_id</code>.
     */
    @NotNull
    public Integer getStudentId() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>zone.internship_journal.internship_release_id</code>.
     */
    public void setInternshipReleaseId(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>zone.internship_journal.internship_release_id</code>.
     */
    @NotNull
    @Size(max = 64)
    public String getInternshipReleaseId() {
        return (String) get(8);
    }

    /**
     * Setter for <code>zone.internship_journal.staff_id</code>.
     */
    public void setStaffId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>zone.internship_journal.staff_id</code>.
     */
    @NotNull
    public Integer getStaffId() {
        return (Integer) get(9);
    }

    /**
     * Setter for <code>zone.internship_journal.internship_journal_word</code>.
     */
    public void setInternshipJournalWord(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>zone.internship_journal.internship_journal_word</code>.
     */
    @Size(max = 500)
    public String getInternshipJournalWord() {
        return (String) get(10);
    }

    /**
     * Setter for <code>zone.internship_journal.is_see_staff</code>.
     */
    public void setIsSeeStaff(Byte value) {
        set(11, value);
    }

    /**
     * Getter for <code>zone.internship_journal.is_see_staff</code>.
     */
    public Byte getIsSeeStaff() {
        return (Byte) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row12<String, String, String, String, String, String, Timestamp, Integer, String, Integer, String, Byte> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    @Override
    public Row12<String, String, String, String, String, String, Timestamp, Integer, String, Integer, String, Byte> valuesRow() {
        return (Row12) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return InternshipJournal.INTERNSHIP_JOURNAL.INTERNSHIP_JOURNAL_ID;
    }

    @Override
    public Field<String> field2() {
        return InternshipJournal.INTERNSHIP_JOURNAL.STUDENT_NAME;
    }

    @Override
    public Field<String> field3() {
        return InternshipJournal.INTERNSHIP_JOURNAL.STUDENT_NUMBER;
    }

    @Override
    public Field<String> field4() {
        return InternshipJournal.INTERNSHIP_JOURNAL.ORGANIZE;
    }

    @Override
    public Field<String> field5() {
        return InternshipJournal.INTERNSHIP_JOURNAL.SCHOOL_GUIDANCE_TEACHER;
    }

    @Override
    public Field<String> field6() {
        return InternshipJournal.INTERNSHIP_JOURNAL.GRADUATION_PRACTICE_COMPANY_NAME;
    }

    @Override
    public Field<Timestamp> field7() {
        return InternshipJournal.INTERNSHIP_JOURNAL.CREATE_DATE;
    }

    @Override
    public Field<Integer> field8() {
        return InternshipJournal.INTERNSHIP_JOURNAL.STUDENT_ID;
    }

    @Override
    public Field<String> field9() {
        return InternshipJournal.INTERNSHIP_JOURNAL.INTERNSHIP_RELEASE_ID;
    }

    @Override
    public Field<Integer> field10() {
        return InternshipJournal.INTERNSHIP_JOURNAL.STAFF_ID;
    }

    @Override
    public Field<String> field11() {
        return InternshipJournal.INTERNSHIP_JOURNAL.INTERNSHIP_JOURNAL_WORD;
    }

    @Override
    public Field<Byte> field12() {
        return InternshipJournal.INTERNSHIP_JOURNAL.IS_SEE_STAFF;
    }

    @Override
    public String component1() {
        return getInternshipJournalId();
    }

    @Override
    public String component2() {
        return getStudentName();
    }

    @Override
    public String component3() {
        return getStudentNumber();
    }

    @Override
    public String component4() {
        return getOrganize();
    }

    @Override
    public String component5() {
        return getSchoolGuidanceTeacher();
    }

    @Override
    public String component6() {
        return getGraduationPracticeCompanyName();
    }

    @Override
    public Timestamp component7() {
        return getCreateDate();
    }

    @Override
    public Integer component8() {
        return getStudentId();
    }

    @Override
    public String component9() {
        return getInternshipReleaseId();
    }

    @Override
    public Integer component10() {
        return getStaffId();
    }

    @Override
    public String component11() {
        return getInternshipJournalWord();
    }

    @Override
    public Byte component12() {
        return getIsSeeStaff();
    }

    @Override
    public String value1() {
        return getInternshipJournalId();
    }

    @Override
    public String value2() {
        return getStudentName();
    }

    @Override
    public String value3() {
        return getStudentNumber();
    }

    @Override
    public String value4() {
        return getOrganize();
    }

    @Override
    public String value5() {
        return getSchoolGuidanceTeacher();
    }

    @Override
    public String value6() {
        return getGraduationPracticeCompanyName();
    }

    @Override
    public Timestamp value7() {
        return getCreateDate();
    }

    @Override
    public Integer value8() {
        return getStudentId();
    }

    @Override
    public String value9() {
        return getInternshipReleaseId();
    }

    @Override
    public Integer value10() {
        return getStaffId();
    }

    @Override
    public String value11() {
        return getInternshipJournalWord();
    }

    @Override
    public Byte value12() {
        return getIsSeeStaff();
    }

    @Override
    public InternshipJournalRecord value1(String value) {
        setInternshipJournalId(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value2(String value) {
        setStudentName(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value3(String value) {
        setStudentNumber(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value4(String value) {
        setOrganize(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value5(String value) {
        setSchoolGuidanceTeacher(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value6(String value) {
        setGraduationPracticeCompanyName(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value7(Timestamp value) {
        setCreateDate(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value8(Integer value) {
        setStudentId(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value9(String value) {
        setInternshipReleaseId(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value10(Integer value) {
        setStaffId(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value11(String value) {
        setInternshipJournalWord(value);
        return this;
    }

    @Override
    public InternshipJournalRecord value12(Byte value) {
        setIsSeeStaff(value);
        return this;
    }

    @Override
    public InternshipJournalRecord values(String value1, String value2, String value3, String value4, String value5, String value6, Timestamp value7, Integer value8, String value9, Integer value10, String value11, Byte value12) {
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
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InternshipJournalRecord
     */
    public InternshipJournalRecord() {
        super(InternshipJournal.INTERNSHIP_JOURNAL);
    }

    /**
     * Create a detached, initialised InternshipJournalRecord
     */
    public InternshipJournalRecord(String internshipJournalId, String studentName, String studentNumber, String organize, String schoolGuidanceTeacher, String graduationPracticeCompanyName, Timestamp createDate, Integer studentId, String internshipReleaseId, Integer staffId, String internshipJournalWord, Byte isSeeStaff) {
        super(InternshipJournal.INTERNSHIP_JOURNAL);

        set(0, internshipJournalId);
        set(1, studentName);
        set(2, studentNumber);
        set(3, organize);
        set(4, schoolGuidanceTeacher);
        set(5, graduationPracticeCompanyName);
        set(6, createDate);
        set(7, studentId);
        set(8, internshipReleaseId);
        set(9, staffId);
        set(10, internshipJournalWord);
        set(11, isSeeStaff);
    }
}
