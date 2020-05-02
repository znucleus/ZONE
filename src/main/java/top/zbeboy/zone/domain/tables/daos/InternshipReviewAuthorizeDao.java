/*
 * This file is generated by jOOQ.
 */
package top.zbeboy.zone.domain.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.zbeboy.zone.domain.tables.InternshipReviewAuthorize;
import top.zbeboy.zone.domain.tables.records.InternshipReviewAuthorizeRecord;


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
public class InternshipReviewAuthorizeDao extends DAOImpl<InternshipReviewAuthorizeRecord, top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize, Record2<String, String>> {

    /**
     * Create a new InternshipReviewAuthorizeDao without any configuration
     */
    public InternshipReviewAuthorizeDao() {
        super(InternshipReviewAuthorize.INTERNSHIP_REVIEW_AUTHORIZE, top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize.class);
    }

    /**
     * Create a new InternshipReviewAuthorizeDao with an attached configuration
     */
    @Autowired
    public InternshipReviewAuthorizeDao(Configuration configuration) {
        super(InternshipReviewAuthorize.INTERNSHIP_REVIEW_AUTHORIZE, top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize.class, configuration);
    }

    @Override
    public Record2<String, String> getId(top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize object) {
        return compositeKeyRecord(object.getInternshipReleaseId(), object.getUsername());
    }

    /**
     * Fetch records that have <code>internship_release_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize> fetchRangeOfInternshipReleaseId(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipReviewAuthorize.INTERNSHIP_REVIEW_AUTHORIZE.INTERNSHIP_RELEASE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>internship_release_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize> fetchByInternshipReleaseId(String... values) {
        return fetch(InternshipReviewAuthorize.INTERNSHIP_REVIEW_AUTHORIZE.INTERNSHIP_RELEASE_ID, values);
    }

    /**
     * Fetch records that have <code>username BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize> fetchRangeOfUsername(String lowerInclusive, String upperInclusive) {
        return fetchRange(InternshipReviewAuthorize.INTERNSHIP_REVIEW_AUTHORIZE.USERNAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>username IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.InternshipReviewAuthorize> fetchByUsername(String... values) {
        return fetch(InternshipReviewAuthorize.INTERNSHIP_REVIEW_AUTHORIZE.USERNAME, values);
    }
}
