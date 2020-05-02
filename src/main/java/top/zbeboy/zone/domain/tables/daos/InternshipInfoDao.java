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

import top.zbeboy.zone.domain.tables.InternshipInfo;
import top.zbeboy.zone.domain.tables.records.InternshipInfoRecord;


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
@Repository
public class InternshipInfoDao extends DAOImpl<InternshipInfoRecord, top.zbeboy.zone.domain.tables.pojos.InternshipInfo, String> {

    /**
     * Create a new InternshipInfoDao without any configuration
     */
    public InternshipInfoDao() {
        super(InternshipInfo.INTERNSHIP_INFO, top.zbeboy.zone.domain.tables.pojos.InternshipInfo.class);
    }

    /**
     * Create a new InternshipInfoDao with an attached configuration
     */
    @Autowired
    public InternshipInfoDao(Configuration configuration) {
        super(InternshipInfo.INTERNSHIP_INFO, top.zbeboy.zone.domain.tables.pojos.InternshipInfo.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.InternshipInfo object) {
        return object.getInternshipInfoId();
    }

    /**
     * Fetch records that have <code>internship_info_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfInternshipInfoId(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_INFO_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_info_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByInternshipInfoId(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_INFO_ID, values);
    }

    /**
     * Fetch a unique record that has <code>internship_info_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.InternshipInfo fetchOneByInternshipInfoId(String value) {
        return fetchOne(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_INFO_ID, value);
    }

    /**
     * Fetch records that have <code>student_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfStudentId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.STUDENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByStudentId(Integer... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.STUDENT_ID, values);
    }

    /**
     * Fetch records that have <code>student_username BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfStudentUsername(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.STUDENT_USERNAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByStudentUsername(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.STUDENT_USERNAME, values);
    }

    /**
     * Fetch records that have <code>internship_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfInternshipReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByInternshipReleaseId(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID, values);
    }

    /**
     * Fetch records that have <code>student_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfStudentName(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.STUDENT_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByStudentName(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.STUDENT_NAME, values);
    }

    /**
     * Fetch records that have <code>organize_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfOrganizeName(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.ORGANIZE_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>organize_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByOrganizeName(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.ORGANIZE_NAME, values);
    }

    /**
     * Fetch records that have <code>student_sex BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfStudentSex(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.STUDENT_SEX, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_sex IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByStudentSex(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.STUDENT_SEX, values);
    }

    /**
     * Fetch records that have <code>student_number BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfStudentNumber(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.STUDENT_NUMBER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_number IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByStudentNumber(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.STUDENT_NUMBER, values);
    }

    /**
     * Fetch records that have <code>mobile BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfMobile(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.MOBILE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>mobile IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByMobile(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.MOBILE, values);
    }

    /**
     * Fetch records that have <code>qq_mailbox BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfQqMailbox(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.QQ_MAILBOX, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>qq_mailbox IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByQqMailbox(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.QQ_MAILBOX, values);
    }

    /**
     * Fetch records that have <code>parent_contact_phone BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfParentContactPhone(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.PARENT_CONTACT_PHONE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>parent_contact_phone IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByParentContactPhone(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.PARENT_CONTACT_PHONE, values);
    }

    /**
     * Fetch records that have <code>headmaster BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfHeadmaster(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.HEADMASTER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>headmaster IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByHeadmaster(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.HEADMASTER, values);
    }

    /**
     * Fetch records that have <code>headmaster_tel BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfHeadmasterTel(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.HEADMASTER_TEL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>headmaster_tel IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByHeadmasterTel(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.HEADMASTER_TEL, values);
    }

    /**
     * Fetch records that have <code>company_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfCompanyName(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.COMPANY_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>company_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByCompanyName(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.COMPANY_NAME, values);
    }

    /**
     * Fetch records that have <code>company_address BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfCompanyAddress(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.COMPANY_ADDRESS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>company_address IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByCompanyAddress(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.COMPANY_ADDRESS, values);
    }

    /**
     * Fetch records that have <code>company_contact BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfCompanyContact(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.COMPANY_CONTACT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>company_contact IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByCompanyContact(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.COMPANY_CONTACT, values);
    }

    /**
     * Fetch records that have <code>company_mobile BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfCompanyMobile(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.COMPANY_MOBILE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>company_mobile IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByCompanyMobile(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.COMPANY_MOBILE, values);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfSchoolGuidanceTeacher(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchBySchoolGuidanceTeacher(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER, values);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher_tel BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfSchoolGuidanceTeacherTel(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER_TEL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher_tel IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchBySchoolGuidanceTeacherTel(String... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER_TEL, values);
    }

    /**
     * Fetch records that have <code>start_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfStartTime(Date lowerInclusive, Date upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.START_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>start_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByStartTime(Date... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.START_TIME, values);
    }

    /**
     * Fetch records that have <code>end_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfEndTime(Date lowerInclusive, Date upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.END_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>end_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByEndTime(Date... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.END_TIME, values);
    }

    /**
     * Fetch records that have <code>commitment_book BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfCommitmentBook(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.COMMITMENT_BOOK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>commitment_book IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByCommitmentBook(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.COMMITMENT_BOOK, values);
    }

    /**
     * Fetch records that have <code>safety_responsibility_book BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfSafetyResponsibilityBook(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.SAFETY_RESPONSIBILITY_BOOK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>safety_responsibility_book IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchBySafetyResponsibilityBook(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.SAFETY_RESPONSIBILITY_BOOK, values);
    }

    /**
     * Fetch records that have <code>practice_agreement BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfPracticeAgreement(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.PRACTICE_AGREEMENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>practice_agreement IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByPracticeAgreement(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.PRACTICE_AGREEMENT, values);
    }

    /**
     * Fetch records that have <code>internship_application BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfInternshipApplication(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_APPLICATION, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_application IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByInternshipApplication(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.INTERNSHIP_APPLICATION, values);
    }

    /**
     * Fetch records that have <code>practice_receiving BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfPracticeReceiving(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.PRACTICE_RECEIVING, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>practice_receiving IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByPracticeReceiving(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.PRACTICE_RECEIVING, values);
    }

    /**
     * Fetch records that have <code>security_education_agreement BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfSecurityEducationAgreement(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.SECURITY_EDUCATION_AGREEMENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>security_education_agreement IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchBySecurityEducationAgreement(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.SECURITY_EDUCATION_AGREEMENT, values);
    }

    /**
     * Fetch records that have <code>parental_consent BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchRangeOfParentalConsent(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(InternshipInfo.INTERNSHIP_INFO.PARENTAL_CONSENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>parental_consent IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipInfo> fetchByParentalConsent(Byte... values) {
        return fetch(InternshipInfo.INTERNSHIP_INFO.PARENTAL_CONSENT, values);
    }
}
