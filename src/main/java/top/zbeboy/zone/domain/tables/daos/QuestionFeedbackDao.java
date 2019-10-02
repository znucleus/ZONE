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

import top.zbeboy.zone.domain.tables.QuestionFeedback;
import top.zbeboy.zone.domain.tables.records.QuestionFeedbackRecord;


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
public class QuestionFeedbackDao extends DAOImpl<QuestionFeedbackRecord, top.zbeboy.zone.domain.tables.pojos.QuestionFeedback, String> {

    /**
     * Create a new QuestionFeedbackDao without any configuration
     */
    public QuestionFeedbackDao() {
        super(QuestionFeedback.QUESTION_FEEDBACK, top.zbeboy.zone.domain.tables.pojos.QuestionFeedback.class);
    }

    /**
     * Create a new QuestionFeedbackDao with an attached configuration
     */
    @Autowired
    public QuestionFeedbackDao(Configuration configuration) {
        super(QuestionFeedback.QUESTION_FEEDBACK, top.zbeboy.zone.domain.tables.pojos.QuestionFeedback.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getId(top.zbeboy.zone.domain.tables.pojos.QuestionFeedback object) {
        return object.getFeedbackId();
    }

    /**
     * Fetch records that have <code>feedback_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByFeedbackId(String... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.FEEDBACK_ID, values);
    }

    /**
     * Fetch a unique record that has <code>feedback_id = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.QuestionFeedback fetchOneByFeedbackId(String value) {
        return fetchOne(QuestionFeedback.QUESTION_FEEDBACK.FEEDBACK_ID, value);
    }

    /**
     * Fetch records that have <code>real_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByRealName(String... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.REAL_NAME, values);
    }

    /**
     * Fetch records that have <code>user_email IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByUserEmail(String... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.USER_EMAIL, values);
    }

    /**
     * Fetch records that have <code>simple_des IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchBySimpleDes(String... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.SIMPLE_DES, values);
    }

    /**
     * Fetch records that have <code>detail_des IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByDetailDes(String... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.DETAIL_DES, values);
    }

    /**
     * Fetch records that have <code>feedback_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByFeedbackDate(Timestamp... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.FEEDBACK_DATE, values);
    }

    /**
     * Fetch records that have <code>deal_status IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByDealStatus(Integer... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.DEAL_STATUS, values);
    }

    /**
     * Fetch records that have <code>deal_date IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByDealDate(Timestamp... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.DEAL_DATE, values);
    }

    /**
     * Fetch records that have <code>deal_username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.QuestionFeedback> fetchByDealUsername(String... values) {
        return fetch(QuestionFeedback.QUESTION_FEEDBACK.DEAL_USERNAME, values);
    }
}
