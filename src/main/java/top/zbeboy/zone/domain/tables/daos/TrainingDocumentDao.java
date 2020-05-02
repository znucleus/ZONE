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

import top.zbeboy.zone.domain.tables.TrainingDocument;
import top.zbeboy.zone.domain.tables.records.TrainingDocumentRecord;


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
public class TrainingDocumentDao extends DAOImpl<TrainingDocumentRecord, top.zbeboy.zone.domain.tables.pojos.TrainingDocument, String> {

    /**
     * Create a new TrainingDocumentDao without any configuration
     */
    public TrainingDocumentDao() {
        super(TrainingDocument.TRAINING_DOCUMENT, top.zbeboy.zone.domain.tables.pojos.TrainingDocument.class);
    }

    /**
     * Create a new TrainingDocumentDao with an attached configuration
     */
    @Autowired
    public TrainingDocumentDao(Configuration configuration) {
        super(TrainingDocument.TRAINING_DOCUMENT, top.zbeboy.zone.domain.tables.pojos.TrainingDocument.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.TrainingDocument object) {
        return object.getTrainingDocumentId();
    }

    /**
     * Fetch records that have <code>training_document_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfTrainingDocumentId(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.TRAINING_DOCUMENT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>training_document_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByTrainingDocumentId(String... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.TRAINING_DOCUMENT_ID, values);
    }

    /**
     * Fetch a unique record that has <code>training_document_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.TrainingDocument fetchOneByTrainingDocumentId(String value) {
        return fetchOne(TrainingDocument.TRAINING_DOCUMENT.TRAINING_DOCUMENT_ID, value);
    }

    /**
     * Fetch records that have <code>training_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfTrainingReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.TRAINING_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>training_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByTrainingReleaseId(String... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.TRAINING_RELEASE_ID, values);
    }

    /**
     * Fetch records that have <code>document_title BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfDocumentTitle(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.DOCUMENT_TITLE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>document_title IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByDocumentTitle(String... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.DOCUMENT_TITLE, values);
    }

    /**
     * Fetch records that have <code>username BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfUsername(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.USERNAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByUsername(String... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.USERNAME, values);
    }

    /**
     * Fetch records that have <code>course_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfCourseId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.COURSE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>course_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByCourseId(Integer... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.COURSE_ID, values);
    }

    /**
     * Fetch records that have <code>creator BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfCreator(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.CREATOR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>creator IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByCreator(String... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.CREATOR, values);
    }

    /**
     * Fetch records that have <code>create_date BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfCreateDate(Timestamp lowerInclusive, Timestamp upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.CREATE_DATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>create_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByCreateDate(Timestamp... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.CREATE_DATE, values);
    }

    /**
     * Fetch records that have <code>reading BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfReading(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.READING, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>reading IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByReading(Integer... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.READING, values);
    }

    /**
     * Fetch records that have <code>is_original BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfIsOriginal(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.IS_ORIGINAL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>is_original IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByIsOriginal(Byte... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.IS_ORIGINAL, values);
    }

    /**
     * Fetch records that have <code>origin BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchRangeOfOrigin(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingDocument.TRAINING_DOCUMENT.ORIGIN, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>origin IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingDocument> fetchByOrigin(String... values) {
        return fetch(TrainingDocument.TRAINING_DOCUMENT.ORIGIN, values);
    }
}
