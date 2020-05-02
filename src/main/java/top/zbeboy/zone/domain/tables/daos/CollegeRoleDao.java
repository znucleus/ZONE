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

import top.zbeboy.zone.domain.tables.CollegeRole;
import top.zbeboy.zone.domain.tables.records.CollegeRoleRecord;


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
public class CollegeRoleDao extends DAOImpl<CollegeRoleRecord, top.zbeboy.zone.domain.tables.pojos.CollegeRole, Record2<String, Integer>> {

    /**
     * Create a new CollegeRoleDao without any configuration
     */
    public CollegeRoleDao() {
        super(CollegeRole.COLLEGE_ROLE, top.zbeboy.zone.domain.tables.pojos.CollegeRole.class);
    }

    /**
     * Create a new CollegeRoleDao with an attached configuration
     */
    @Autowired
    public CollegeRoleDao(Configuration configuration) {
        super(CollegeRole.COLLEGE_ROLE, top.zbeboy.zone.domain.tables.pojos.CollegeRole.class, configuration);
    }

    @Override
    public Record2<String, Integer> getId(top.zbeboy.zone.domain.tables.pojos.CollegeRole object) {
        return compositeKeyRecord(object.getRoleId(), object.getCollegeId());
    }

    /**
     * Fetch records that have <code>role_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CollegeRole> fetchRangeOfRoleId(String lowerInclusive, String upperInclusive) {
        return fetchRange(CollegeRole.COLLEGE_ROLE.ROLE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>role_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CollegeRole> fetchByRoleId(String... values) {
        return fetch(CollegeRole.COLLEGE_ROLE.ROLE_ID, values);
    }

    /**
     * Fetch records that have <code>college_id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CollegeRole> fetchRangeOfCollegeId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(CollegeRole.COLLEGE_ROLE.COLLEGE_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>college_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.CollegeRole> fetchByCollegeId(Integer... values) {
        return fetch(CollegeRole.COLLEGE_ROLE.COLLEGE_ID, values);
    }
}
