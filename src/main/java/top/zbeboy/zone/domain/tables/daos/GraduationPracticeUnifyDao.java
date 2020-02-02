/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.sql.Date;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.GraduationPracticeUnify;
import top.zbeboy.zone.domain.tables.records.GraduationPracticeUnifyRecord;


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
@Repository
public class GraduationPracticeUnifyDao extends DAOImpl<GraduationPracticeUnifyRecord, top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify, String> {

    /**
     * Create a new GraduationPracticeUnifyDao without any configuration
     */
    public GraduationPracticeUnifyDao() {
        super(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY, top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify.class);
    }

    /**
     * Create a new GraduationPracticeUnifyDao with an attached configuration
     */
    @Autowired
    public GraduationPracticeUnifyDao(Configuration configuration) {
        super(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY, top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify object) {
        return object.getGraduationPracticeUnifyId();
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfGraduationPracticeUnifyId(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByGraduationPracticeUnifyId(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_ID, values);
    }

    /**
     * Fetch a unique record that has <code>graduation_practice_unify_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify fetchOneByGraduationPracticeUnifyId(String value) {
        return fetchOne(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_ID, value);
    }

    /**
     * Fetch records that have <code>student_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfStudentName(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByStudentName(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_NAME, values);
    }

    /**
     * Fetch records that have <code>college_class BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfCollegeClass(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.COLLEGE_CLASS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>college_class IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByCollegeClass(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.COLLEGE_CLASS, values);
    }

    /**
     * Fetch records that have <code>student_sex BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfStudentSex(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_SEX, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_sex IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByStudentSex(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_SEX, values);
    }

    /**
     * Fetch records that have <code>student_number BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfStudentNumber(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_NUMBER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_number IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByStudentNumber(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_NUMBER, values);
    }

    /**
     * Fetch records that have <code>phone_number BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfPhoneNumber(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PHONE_NUMBER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>phone_number IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByPhoneNumber(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PHONE_NUMBER, values);
    }

    /**
     * Fetch records that have <code>qq_mailbox BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfQqMailbox(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.QQ_MAILBOX, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>qq_mailbox IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByQqMailbox(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.QQ_MAILBOX, values);
    }

    /**
     * Fetch records that have <code>parental_contact BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfParentalContact(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PARENTAL_CONTACT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>parental_contact IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByParentalContact(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PARENTAL_CONTACT, values);
    }

    /**
     * Fetch records that have <code>headmaster BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfHeadmaster(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.HEADMASTER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>headmaster IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByHeadmaster(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.HEADMASTER, values);
    }

    /**
     * Fetch records that have <code>headmaster_contact BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfHeadmasterContact(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.HEADMASTER_CONTACT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>headmaster_contact IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByHeadmasterContact(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.HEADMASTER_CONTACT, values);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfGraduationPracticeUnifyName(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByGraduationPracticeUnifyName(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_NAME, values);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_address BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfGraduationPracticeUnifyAddress(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_ADDRESS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_address IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByGraduationPracticeUnifyAddress(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_ADDRESS, values);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_contacts BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfGraduationPracticeUnifyContacts(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_CONTACTS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_contacts IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByGraduationPracticeUnifyContacts(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_CONTACTS, values);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_tel BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfGraduationPracticeUnifyTel(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_TEL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>graduation_practice_unify_tel IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByGraduationPracticeUnifyTel(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.GRADUATION_PRACTICE_UNIFY_TEL, values);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfSchoolGuidanceTeacher(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SCHOOL_GUIDANCE_TEACHER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchBySchoolGuidanceTeacher(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SCHOOL_GUIDANCE_TEACHER, values);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher_tel BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfSchoolGuidanceTeacherTel(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SCHOOL_GUIDANCE_TEACHER_TEL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher_tel IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchBySchoolGuidanceTeacherTel(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SCHOOL_GUIDANCE_TEACHER_TEL, values);
    }

    /**
     * Fetch records that have <code>start_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfStartTime(Date lowerInclusive, Date upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.START_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>start_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByStartTime(Date... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.START_TIME, values);
    }

    /**
     * Fetch records that have <code>end_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfEndTime(Date lowerInclusive, Date upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.END_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>end_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByEndTime(Date... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.END_TIME, values);
    }

    /**
     * Fetch records that have <code>commitment_book BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfCommitmentBook(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.COMMITMENT_BOOK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>commitment_book IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByCommitmentBook(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.COMMITMENT_BOOK, values);
    }

    /**
     * Fetch records that have <code>safety_responsibility_book BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfSafetyResponsibilityBook(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SAFETY_RESPONSIBILITY_BOOK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>safety_responsibility_book IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchBySafetyResponsibilityBook(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SAFETY_RESPONSIBILITY_BOOK, values);
    }

    /**
     * Fetch records that have <code>practice_agreement BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfPracticeAgreement(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PRACTICE_AGREEMENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>practice_agreement IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByPracticeAgreement(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PRACTICE_AGREEMENT, values);
    }

    /**
     * Fetch records that have <code>internship_application BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfInternshipApplication(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.INTERNSHIP_APPLICATION, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_application IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByInternshipApplication(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.INTERNSHIP_APPLICATION, values);
    }

    /**
     * Fetch records that have <code>practice_receiving BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfPracticeReceiving(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PRACTICE_RECEIVING, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>practice_receiving IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByPracticeReceiving(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PRACTICE_RECEIVING, values);
    }

    /**
     * Fetch records that have <code>security_education_agreement BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfSecurityEducationAgreement(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SECURITY_EDUCATION_AGREEMENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>security_education_agreement IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchBySecurityEducationAgreement(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.SECURITY_EDUCATION_AGREEMENT, values);
    }

    /**
     * Fetch records that have <code>parental_consent BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfParentalConsent(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PARENTAL_CONSENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>parental_consent IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByParentalConsent(Byte... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.PARENTAL_CONSENT, values);
    }

    /**
     * Fetch records that have <code>student_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfStudentId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByStudentId(Integer... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_ID, values);
    }

    /**
     * Fetch records that have <code>student_username BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfStudentUsername(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_USERNAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByStudentUsername(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.STUDENT_USERNAME, values);
    }

    /**
     * Fetch records that have <code>internship_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchRangeOfInternshipReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.INTERNSHIP_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.GraduationPracticeUnify> fetchByInternshipReleaseId(String... values) {
        return fetch(GraduationPracticeUnify.GRADUATION_PRACTICE_UNIFY.INTERNSHIP_RELEASE_ID, values);
    }
}
