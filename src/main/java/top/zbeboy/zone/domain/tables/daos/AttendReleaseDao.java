/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.AttendRelease;
import top.zbeboy.zone.domain.tables.records.AttendReleaseRecord;


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
public class AttendReleaseDao extends DAOImpl<AttendReleaseRecord, top.zbeboy.zone.domain.tables.pojos.AttendRelease, String> {

    /**
     * Create a new AttendReleaseDao without any configuration
     */
    public AttendReleaseDao() {
        super(AttendRelease.ATTEND_RELEASE, top.zbeboy.zone.domain.tables.pojos.AttendRelease.class);
    }

    /**
     * Create a new AttendReleaseDao with an attached configuration
     */
    @Autowired
    public AttendReleaseDao(Configuration configuration) {
        super(AttendRelease.ATTEND_RELEASE, top.zbeboy.zone.domain.tables.pojos.AttendRelease.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.AttendRelease object) {
        return object.getAttendReleaseId();
    }

    /**
     * Fetch records that have <code>attend_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfAttendReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.ATTEND_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByAttendReleaseId(String... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.ATTEND_RELEASE_ID, values);
    }

    /**
     * Fetch a unique record that has <code>attend_release_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.AttendRelease fetchOneByAttendReleaseId(String value) {
        return fetchOne(AttendRelease.ATTEND_RELEASE.ATTEND_RELEASE_ID, value);
    }

    /**
     * Fetch records that have <code>title BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfTitle(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.TITLE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByTitle(String... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.TITLE, values);
    }

    /**
     * Fetch records that have <code>attend_start_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfAttendStartTime(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.ATTEND_START_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_start_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByAttendStartTime(Timestamp... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.ATTEND_START_TIME, values);
    }

    /**
     * Fetch records that have <code>attend_end_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfAttendEndTime(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.ATTEND_END_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_end_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByAttendEndTime(Timestamp... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.ATTEND_END_TIME, values);
    }

    /**
     * Fetch records that have <code>is_auto BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfIsAuto(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.IS_AUTO, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>is_auto IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByIsAuto(Byte... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.IS_AUTO, values);
    }

    /**
     * Fetch records that have <code>valid_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfValidDate(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.VALID_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>valid_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByValidDate(Timestamp... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.VALID_DATE, values);
    }

    /**
     * Fetch records that have <code>expire_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfExpireDate(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.EXPIRE_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>expire_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByExpireDate(Timestamp... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.EXPIRE_DATE, values);
    }

    /**
     * Fetch records that have <code>organize_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfOrganizeId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.ORGANIZE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>organize_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByOrganizeId(Integer... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.ORGANIZE_ID, values);
    }

    /**
     * Fetch records that have <code>username BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfUsername(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.USERNAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByUsername(String... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.USERNAME, values);
    }

    /**
     * Fetch records that have <code>release_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchRangeOfReleaseTime(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(AttendRelease.ATTEND_RELEASE.RELEASE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>release_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendRelease> fetchByReleaseTime(Timestamp... values) {
        return fetch(AttendRelease.ATTEND_RELEASE.RELEASE_TIME, values);
    }
}
