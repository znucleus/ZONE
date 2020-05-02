/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.InternshipRegulate;
import top.zbeboy.zone.domain.tables.records.InternshipRegulateRecord;


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
public class InternshipRegulateDao extends DAOImpl<InternshipRegulateRecord, top.zbeboy.zone.domain.tables.pojos.InternshipRegulate, String> {

    /**
     * Create a new InternshipRegulateDao without any configuration
     */
    public InternshipRegulateDao() {
        super(InternshipRegulate.INTERNSHIP_REGULATE, top.zbeboy.zone.domain.tables.pojos.InternshipRegulate.class);
    }

    /**
     * Create a new InternshipRegulateDao with an attached configuration
     */
    @Autowired
    public InternshipRegulateDao(Configuration configuration) {
        super(InternshipRegulate.INTERNSHIP_REGULATE, top.zbeboy.zone.domain.tables.pojos.InternshipRegulate.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.InternshipRegulate object) {
        return object.getInternshipRegulateId();
    }

    /**
     * Fetch records that have <code>internship_regulate_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfInternshipRegulateId(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_REGULATE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_regulate_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByInternshipRegulateId(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_REGULATE_ID, values);
    }

    /**
     * Fetch a unique record that has <code>internship_regulate_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.InternshipRegulate fetchOneByInternshipRegulateId(String value) {
        return fetchOne(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_REGULATE_ID, value);
    }

    /**
     * Fetch records that have <code>student_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfStudentName(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByStudentName(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_NAME, values);
    }

    /**
     * Fetch records that have <code>student_number BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfStudentNumber(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_NUMBER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_number IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByStudentNumber(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_NUMBER, values);
    }

    /**
     * Fetch records that have <code>student_tel BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfStudentTel(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_TEL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_tel IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByStudentTel(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_TEL, values);
    }

    /**
     * Fetch records that have <code>internship_content BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfInternshipContent(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_CONTENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_content IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByInternshipContent(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_CONTENT, values);
    }

    /**
     * Fetch records that have <code>internship_progress BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfInternshipProgress(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_PROGRESS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_progress IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByInternshipProgress(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_PROGRESS, values);
    }

    /**
     * Fetch records that have <code>report_way BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfReportWay(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.REPORT_WAY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>report_way IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByReportWay(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.REPORT_WAY, values);
    }

    /**
     * Fetch records that have <code>report_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfReportDate(Date lowerInclusive, Date upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.REPORT_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>report_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByReportDate(Date... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.REPORT_DATE, values);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfSchoolGuidanceTeacher(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.SCHOOL_GUIDANCE_TEACHER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>school_guidance_teacher IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchBySchoolGuidanceTeacher(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.SCHOOL_GUIDANCE_TEACHER, values);
    }

    /**
     * Fetch records that have <code>tliy BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfTliy(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.TLIY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>tliy IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByTliy(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.TLIY, values);
    }

    /**
     * Fetch records that have <code>create_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfCreateDate(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.CREATE_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByCreateDate(Timestamp... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.CREATE_DATE, values);
    }

    /**
     * Fetch records that have <code>student_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfStudentId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByStudentId(Integer... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.STUDENT_ID, values);
    }

    /**
     * Fetch records that have <code>internship_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfInternshipReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByInternshipReleaseId(String... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.INTERNSHIP_RELEASE_ID, values);
    }

    /**
     * Fetch records that have <code>staff_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchRangeOfStaffId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(InternshipRegulate.INTERNSHIP_REGULATE.STAFF_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>staff_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipRegulate> fetchByStaffId(Integer... values) {
        return fetch(InternshipRegulate.INTERNSHIP_REGULATE.STAFF_ID, values);
    }
}
