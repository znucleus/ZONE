/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.AttendData;
import top.zbeboy.zone.domain.tables.records.AttendDataRecord;


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
public class AttendDataDao extends DAOImpl<AttendDataRecord, top.zbeboy.zone.domain.tables.pojos.AttendData, Record2<String, Integer>> {

    /**
     * Create a new AttendDataDao without any configuration
     */
    public AttendDataDao() {
        super(AttendData.ATTEND_DATA, top.zbeboy.zone.domain.tables.pojos.AttendData.class);
    }

    /**
     * Create a new AttendDataDao with an attached configuration
     */
    @Autowired
    public AttendDataDao(Configuration configuration) {
        super(AttendData.ATTEND_DATA, top.zbeboy.zone.domain.tables.pojos.AttendData.class, configuration);
    }

    @Override
    public Record2<String, Integer> getId(top.zbeboy.zone.domain.tables.pojos.AttendData object) {
        return compositeKeyRecord(object.getAttendUsersId(), object.getAttendReleaseSubId());
    }

    /**
     * Fetch records that have <code>attend_users_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfAttendUsersId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.ATTEND_USERS_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_users_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByAttendUsersId(String... values) {
        return fetch(AttendData.ATTEND_DATA.ATTEND_USERS_ID, values);
    }

    /**
     * Fetch records that have <code>attend_release_sub_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfAttendReleaseSubId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.ATTEND_RELEASE_SUB_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_release_sub_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByAttendReleaseSubId(Integer... values) {
        return fetch(AttendData.ATTEND_DATA.ATTEND_RELEASE_SUB_ID, values);
    }

    /**
     * Fetch records that have <code>location BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfLocation(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.LOCATION, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>location IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByLocation(String... values) {
        return fetch(AttendData.ATTEND_DATA.LOCATION, values);
    }

    /**
     * Fetch records that have <code>address BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfAddress(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.ADDRESS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>address IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByAddress(String... values) {
        return fetch(AttendData.ATTEND_DATA.ADDRESS, values);
    }

    /**
     * Fetch records that have <code>device_same BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfDeviceSame(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.DEVICE_SAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>device_same IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByDeviceSame(Byte... values) {
        return fetch(AttendData.ATTEND_DATA.DEVICE_SAME, values);
    }

    /**
     * Fetch records that have <code>attend_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfAttendDate(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.ATTEND_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByAttendDate(Timestamp... values) {
        return fetch(AttendData.ATTEND_DATA.ATTEND_DATE, values);
    }

    /**
     * Fetch records that have <code>attend_remark BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchRangeOfAttendRemark(String lowerInclusive, String upperInclusive) {
        return fetchRange(AttendData.ATTEND_DATA.ATTEND_REMARK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>attend_remark IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AttendData> fetchByAttendRemark(String... values) {
        return fetch(AttendData.ATTEND_DATA.ATTEND_REMARK, values);
    }
}
