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

import top.zbeboy.zone.domain.tables.LeaverRegisterData;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterDataRecord;


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
public class LeaverRegisterDataDao extends DAOImpl<LeaverRegisterDataRecord, top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData, String> {

    /**
     * Create a new LeaverRegisterDataDao without any configuration
     */
    public LeaverRegisterDataDao() {
        super(LeaverRegisterData.LEAVER_REGISTER_DATA, top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData.class);
    }

    /**
     * Create a new LeaverRegisterDataDao with an attached configuration
     */
    @Autowired
    public LeaverRegisterDataDao(Configuration configuration) {
        super(LeaverRegisterData.LEAVER_REGISTER_DATA, top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData object) {
        return object.getLeaverRegisterDataId();
    }

    /**
     * Fetch records that have <code>leaver_register_data_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchRangeOfLeaverRegisterDataId(String lowerInclusive, String upperInclusive) {
        return fetchRange(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_REGISTER_DATA_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>leaver_register_data_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchByLeaverRegisterDataId(String... values) {
        return fetch(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_REGISTER_DATA_ID, values);
    }

    /**
     * Fetch a unique record that has <code>leaver_register_data_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData fetchOneByLeaverRegisterDataId(String value) {
        return fetchOne(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_REGISTER_DATA_ID, value);
    }

    /**
     * Fetch records that have <code>student_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchRangeOfStudentId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(LeaverRegisterData.LEAVER_REGISTER_DATA.STUDENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>student_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchByStudentId(Integer... values) {
        return fetch(LeaverRegisterData.LEAVER_REGISTER_DATA.STUDENT_ID, values);
    }

    /**
     * Fetch records that have <code>leaver_register_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchRangeOfLeaverRegisterReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>leaver_register_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchByLeaverRegisterReleaseId(String... values) {
        return fetch(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_REGISTER_RELEASE_ID, values);
    }

    /**
     * Fetch records that have <code>leaver_address BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchRangeOfLeaverAddress(String lowerInclusive, String upperInclusive) {
        return fetchRange(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_ADDRESS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>leaver_address IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchByLeaverAddress(String... values) {
        return fetch(LeaverRegisterData.LEAVER_REGISTER_DATA.LEAVER_ADDRESS, values);
    }

    /**
     * Fetch records that have <code>register_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchRangeOfRegisterDate(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(LeaverRegisterData.LEAVER_REGISTER_DATA.REGISTER_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>register_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchByRegisterDate(Timestamp... values) {
        return fetch(LeaverRegisterData.LEAVER_REGISTER_DATA.REGISTER_DATE, values);
    }

    /**
     * Fetch records that have <code>remark BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchRangeOfRemark(String lowerInclusive, String upperInclusive) {
        return fetchRange(LeaverRegisterData.LEAVER_REGISTER_DATA.REMARK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>remark IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.LeaverRegisterData> fetchByRemark(String... values) {
        return fetch(LeaverRegisterData.LEAVER_REGISTER_DATA.REMARK, values);
    }
}