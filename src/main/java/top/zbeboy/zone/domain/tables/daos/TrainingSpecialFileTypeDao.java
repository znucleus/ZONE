/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.TrainingSpecialFileType;
import top.zbeboy.zone.domain.tables.records.TrainingSpecialFileTypeRecord;


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
public class TrainingSpecialFileTypeDao extends DAOImpl<TrainingSpecialFileTypeRecord, top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType, String> {

    /**
     * Create a new TrainingSpecialFileTypeDao without any configuration
     */
    public TrainingSpecialFileTypeDao() {
        super(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE, top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType.class);
    }

    /**
     * Create a new TrainingSpecialFileTypeDao with an attached configuration
     */
    @Autowired
    public TrainingSpecialFileTypeDao(Configuration configuration) {
        super(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE, top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType object) {
        return object.getFileTypeId();
    }

    /**
     * Fetch records that have <code>file_type_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType> fetchRangeOfFileTypeId(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.FILE_TYPE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>file_type_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType> fetchByFileTypeId(String... values) {
        return fetch(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.FILE_TYPE_ID, values);
    }

    /**
     * Fetch a unique record that has <code>file_type_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType fetchOneByFileTypeId(String value) {
        return fetchOne(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.FILE_TYPE_ID, value);
    }

    /**
     * Fetch records that have <code>file_type_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType> fetchRangeOfFileTypeName(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.FILE_TYPE_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>file_type_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType> fetchByFileTypeName(String... values) {
        return fetch(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.FILE_TYPE_NAME, values);
    }

    /**
     * Fetch records that have <code>training_special_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType> fetchRangeOfTrainingSpecialId(String lowerInclusive, String upperInclusive) {
        return fetchRange(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.TRAINING_SPECIAL_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>training_special_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.TrainingSpecialFileType> fetchByTrainingSpecialId(String... values) {
        return fetch(TrainingSpecialFileType.TRAINING_SPECIAL_FILE_TYPE.TRAINING_SPECIAL_ID, values);
    }
}
