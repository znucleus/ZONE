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

import top.zbeboy.zone.domain.tables.AnswerOption;
import top.zbeboy.zone.domain.tables.records.AnswerOptionRecord;


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
public class AnswerOptionDao extends DAOImpl<AnswerOptionRecord, top.zbeboy.zone.domain.tables.pojos.AnswerOption, String> {

    /**
     * Create a new AnswerOptionDao without any configuration
     */
    public AnswerOptionDao() {
        super(AnswerOption.ANSWER_OPTION, top.zbeboy.zone.domain.tables.pojos.AnswerOption.class);
    }

    /**
     * Create a new AnswerOptionDao with an attached configuration
     */
    @Autowired
    public AnswerOptionDao(Configuration configuration) {
        super(AnswerOption.ANSWER_OPTION, top.zbeboy.zone.domain.tables.pojos.AnswerOption.class, configuration);
    }

    @Override
    public String getId(top.zbeboy.zone.domain.tables.pojos.AnswerOption object) {
        return object.getAnswerOptionId();
    }

    /**
     * Fetch records that have <code>answer_option_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchRangeOfAnswerOptionId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerOption.ANSWER_OPTION.ANSWER_OPTION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>answer_option_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchByAnswerOptionId(String... values) {
        return fetch(AnswerOption.ANSWER_OPTION.ANSWER_OPTION_ID, values);
    }

    /**
     * Fetch a unique record that has <code>answer_option_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.AnswerOption fetchOneByAnswerOptionId(String value) {
        return fetchOne(AnswerOption.ANSWER_OPTION.ANSWER_OPTION_ID, value);
    }

    /**
     * Fetch records that have <code>option_content BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchRangeOfOptionContent(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerOption.ANSWER_OPTION.OPTION_CONTENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>option_content IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchByOptionContent(String... values) {
        return fetch(AnswerOption.ANSWER_OPTION.OPTION_CONTENT, values);
    }

    /**
     * Fetch records that have <code>sort BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchRangeOfSort(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(AnswerOption.ANSWER_OPTION.SORT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>sort IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchBySort(Byte... values) {
        return fetch(AnswerOption.ANSWER_OPTION.SORT, values);
    }

    /**
     * Fetch records that have <code>option_key BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchRangeOfOptionKey(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerOption.ANSWER_OPTION.OPTION_KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>option_key IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchByOptionKey(String... values) {
        return fetch(AnswerOption.ANSWER_OPTION.OPTION_KEY, values);
    }

    /**
     * Fetch records that have <code>answer_subject_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchRangeOfAnswerSubjectId(String lowerInclusive, String upperInclusive) {
        return fetchRange(AnswerOption.ANSWER_OPTION.ANSWER_SUBJECT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>answer_subject_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AnswerOption> fetchByAnswerSubjectId(String... values) {
        return fetch(AnswerOption.ANSWER_OPTION.ANSWER_SUBJECT_ID, values);
    }
}
