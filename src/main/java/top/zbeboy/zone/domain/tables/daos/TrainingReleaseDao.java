/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.TrainingRelease;
import top.zbeboy.zone.domain.tables.records.TrainingReleaseRecord;


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
public class TrainingReleaseDao extends DAOImpl<TrainingReleaseRecord, top.zbeboy.zone.domain.tables.pojos.TrainingRelease, String> {

    /**
     * Create a new TrainingReleaseDao without any configuration
     */
    public TrainingReleaseDao() {
        super(TrainingRelease.TRAINING_RELEASE, top.zbeboy.zone.domain.tables.pojos.TrainingRelease.class);
    }

    /**
     * Create a new TrainingReleaseDao with an attached configuration
     */
    @Autowired
    public TrainingReleaseDao(Configuration configuration) {
        super(TrainingRelease.TRAINING_RELEASE, top.zbeboy.zone.domain.tables.pojos.TrainingRelease.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getId(top.zbeboy.zone.domain.tables.pojos.TrainingRelease object) {
        return object.getTrainingReleaseId();
    }

    /**
     * Fetch records that have <code>training_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByTrainingReleaseId(String... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.TRAINING_RELEASE_ID, values);
    }

    /**
     * Fetch a unique record that has <code>training_release_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.TrainingRelease fetchOneByTrainingReleaseId(String value) {
        return fetchOne(TrainingRelease.TRAINING_RELEASE.TRAINING_RELEASE_ID, value);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByTitle(String... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.TITLE, values);
    }

    /**
     * Fetch records that have <code>start_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByStartDate(Date... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.START_DATE, values);
    }

    /**
     * Fetch records that have <code>end_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByEndDate(Date... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.END_DATE, values);
    }

    /**
     * Fetch records that have <code>cycle IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByCycle(String... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.CYCLE, values);
    }

    /**
     * Fetch records that have <code>start_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByStartTime(Time... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.START_TIME, values);
    }

    /**
     * Fetch records that have <code>end_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByEndTime(Time... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.END_TIME, values);
    }

    /**
     * Fetch records that have <code>schoolroom_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchBySchoolroomId(Integer... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.SCHOOLROOM_ID, values);
    }

    /**
     * Fetch records that have <code>username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByUsername(String... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.USERNAME, values);
    }

    /**
     * Fetch records that have <code>course_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByCourseId(Integer... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.COURSE_ID, values);
    }

    /**
     * Fetch records that have <code>organize_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByOrganizeId(Integer... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.ORGANIZE_ID, values);
    }

    /**
     * Fetch records that have <code>publisher IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByPublisher(String... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.PUBLISHER, values);
    }

    /**
     * Fetch records that have <code>release_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingRelease> fetchByReleaseTime(Timestamp... values) {
        return fetch(TrainingRelease.TRAINING_RELEASE.RELEASE_TIME, values);
    }
}
