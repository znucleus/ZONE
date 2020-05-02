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

import top.zbeboy.zone.domain.tables.AnswerSolving;
import top.zbeboy.zone.domain.tables.records.AnswerSolvingRecord;


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
public class AnswerSolvingDao extends DAOImpl<AnswerSolvingRecord, top.zbeboy.zone.domain.tables.pojos.AnswerSolving, String> {

    /**
     * Create a new AnswerSolvingDao without any configuration
     */
    public AnswerSolvingDao() {
        super(AnswerSolving.ANSWER_SOLVING, top.zbeboy.zone.domain.tables.pojos.AnswerSolving.class);
    }

    /**
     * Create a new AnswerSolvingDao with an attached configuration
     */
    @Autowired
    public AnswerSolvingDao(Configuration configuration) {
        super(AnswerSolving.ANSWER_SOLVING, top.zbeboy.zone.domain.tables.pojos.AnswerSolving.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.AnswerSolving object) {
        return object.getAnswerSolvingId();
    }

    /**
     * Fetch records that have <code>answer_solving_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfAnswerSolvingId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.ANSWER_SOLVING_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>answer_solving_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchByAnswerSolvingId(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.ANSWER_SOLVING_ID, values);
    }

    /**
     * Fetch a unique record that has <code>answer_solving_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.AnswerSolving fetchOneByAnswerSolvingId(String value) {
        return fetchOne(AnswerSolving.ANSWER_SOLVING.ANSWER_SOLVING_ID, value);
    }

    /**
     * Fetch records that have <code>answer_subject_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfAnswerSubjectId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.ANSWER_SUBJECT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>answer_subject_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchByAnswerSubjectId(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.ANSWER_SUBJECT_ID, values);
    }

    /**
     * Fetch records that have <code>select_key BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfSelectKey(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.SELECT_KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>select_key IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchBySelectKey(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.SELECT_KEY, values);
    }

    /**
     * Fetch records that have <code>right_key BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfRightKey(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.RIGHT_KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>right_key IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchByRightKey(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.RIGHT_KEY, values);
    }

    /**
     * Fetch records that have <code>user_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfUserId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.USER_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchByUserId(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.USER_ID, values);
    }

    /**
     * Fetch records that have <code>user_name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfUserName(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.USER_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchByUserName(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.USER_NAME, values);
    }

    /**
     * Fetch records that have <code>answer_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchRangeOfAnswerReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerSolving.ANSWER_SOLVING.ANSWER_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>answer_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerSolving> fetchByAnswerReleaseId(String... values) {
        return fetch(AnswerSolving.ANSWER_SOLVING.ANSWER_RELEASE_ID, values);
    }
}
