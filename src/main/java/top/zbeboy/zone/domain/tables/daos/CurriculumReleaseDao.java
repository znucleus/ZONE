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

import top.zbeboy.zone.domain.tables.CurriculumRelease;
import top.zbeboy.zone.domain.tables.records.CurriculumReleaseRecord;


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
@Repository
public class CurriculumReleaseDao extends DAOImpl<CurriculumReleaseRecord, top.zbeboy.zone.domain.tables.pojos.CurriculumRelease, String> {

    /**
     * Create a new CurriculumReleaseDao without any configuration
     */
    public CurriculumReleaseDao() {
        super(CurriculumRelease.CURRICULUM_RELEASE, top.zbeboy.zone.domain.tables.pojos.CurriculumRelease.class);
    }

    /**
     * Create a new CurriculumReleaseDao with an attached configuration
     */
    @Autowired
    public CurriculumReleaseDao(Configuration configuration) {
        super(CurriculumRelease.CURRICULUM_RELEASE, top.zbeboy.zone.domain.tables.pojos.CurriculumRelease.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getId(top.zbeboy.zone.domain.tables.pojos.CurriculumRelease object) {
        return object.getCurriculumReleaseId();
    }

    /**
     * Fetch records that have <code>curriculum_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByCurriculumReleaseId(String... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.CURRICULUM_RELEASE_ID, values);
    }

    /**
     * Fetch a unique record that has <code>curriculum_release_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.CurriculumRelease fetchOneByCurriculumReleaseId(String value) {
        return fetchOne(CurriculumRelease.CURRICULUM_RELEASE.CURRICULUM_RELEASE_ID, value);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByTitle(String... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.TITLE, values);
    }

    /**
     * Fetch records that have <code>start_year IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByStartYear(Integer... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.START_YEAR, values);
    }

    /**
     * Fetch records that have <code>end_year IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByEndYear(Integer... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.END_YEAR, values);
    }

    /**
     * Fetch records that have <code>term IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByTerm(Byte... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.TERM, values);
    }

    /**
     * Fetch records that have <code>college_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByCollegeId(Integer... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.COLLEGE_ID, values);
    }

    /**
     * Fetch records that have <code>release_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByReleaseTime(Timestamp... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.RELEASE_TIME, values);
    }

    /**
     * Fetch records that have <code>release_username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByReleaseUsername(String... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.RELEASE_USERNAME, values);
    }

    /**
     * Fetch records that have <code>release_publisher IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByReleasePublisher(String... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.RELEASE_PUBLISHER, values);
    }

    /**
     * Fetch records that have <code>weeks IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByWeeks(Byte... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.WEEKS, values);
    }

    /**
     * Fetch records that have <code>start_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByStartDate(Date... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.START_DATE, values);
    }

    /**
     * Fetch records that have <code>end_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CurriculumRelease> fetchByEndDate(Date... values) {
        return fetch(CurriculumRelease.CURRICULUM_RELEASE.END_DATE, values);
    }
}
