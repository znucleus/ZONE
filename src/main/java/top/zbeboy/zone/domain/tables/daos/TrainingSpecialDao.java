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

import top.zbeboy.zone.domain.tables.TrainingSpecial;
import top.zbeboy.zone.domain.tables.records.TrainingSpecialRecord;


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
public class TrainingSpecialDao extends DAOImpl<TrainingSpecialRecord, top.zbeboy.zone.domain.tables.pojos.TrainingSpecial, String> {

    /**
     * Create a new TrainingSpecialDao without any configuration
     */
    public TrainingSpecialDao() {
        super(TrainingSpecial.TRAINING_SPECIAL, top.zbeboy.zone.domain.tables.pojos.TrainingSpecial.class);
    }

    /**
     * Create a new TrainingSpecialDao with an attached configuration
     */
    @Autowired
    public TrainingSpecialDao(Configuration configuration) {
        super(TrainingSpecial.TRAINING_SPECIAL, top.zbeboy.zone.domain.tables.pojos.TrainingSpecial.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.TrainingSpecial object) {
        return object.getTrainingSpecialId();
    }

    /**
     * Fetch records that have <code>training_special_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchRangeOfTrainingSpecialId(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecial.TRAINING_SPECIAL.TRAINING_SPECIAL_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>training_special_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchByTrainingSpecialId(String... values) {
        return fetch(TrainingSpecial.TRAINING_SPECIAL.TRAINING_SPECIAL_ID, values);
    }

    /**
     * Fetch a unique record that has <code>training_special_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.TrainingSpecial fetchOneByTrainingSpecialId(String value) {
        return fetchOne(TrainingSpecial.TRAINING_SPECIAL.TRAINING_SPECIAL_ID, value);
    }

    /**
     * Fetch records that have <code>title BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchRangeOfTitle(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecial.TRAINING_SPECIAL.TITLE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>title IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchByTitle(String... values) {
        return fetch(TrainingSpecial.TRAINING_SPECIAL.TITLE, values);
    }

    /**
     * Fetch records that have <code>cover BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchRangeOfCover(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecial.TRAINING_SPECIAL.COVER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cover IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchByCover(String... values) {
        return fetch(TrainingSpecial.TRAINING_SPECIAL.COVER, values);
    }

    /**
     * Fetch records that have <code>username BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchRangeOfUsername(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecial.TRAINING_SPECIAL.USERNAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchByUsername(String... values) {
        return fetch(TrainingSpecial.TRAINING_SPECIAL.USERNAME, values);
    }

    /**
     * Fetch records that have <code>publisher BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchRangeOfPublisher(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecial.TRAINING_SPECIAL.PUBLISHER, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>publisher IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchByPublisher(String... values) {
        return fetch(TrainingSpecial.TRAINING_SPECIAL.PUBLISHER, values);
    }

    /**
     * Fetch records that have <code>release_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchRangeOfReleaseTime(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(TrainingSpecial.TRAINING_SPECIAL.RELEASE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>release_time IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecial> fetchByReleaseTime(Timestamp... values) {
        return fetch(TrainingSpecial.TRAINING_SPECIAL.RELEASE_TIME, values);
    }
}
