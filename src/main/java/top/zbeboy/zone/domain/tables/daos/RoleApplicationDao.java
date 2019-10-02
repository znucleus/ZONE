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

import top.zbeboy.zone.domain.tables.RoleApplication;
import top.zbeboy.zone.domain.tables.records.RoleApplicationRecord;


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
public class RoleApplicationDao extends DAOImpl<RoleApplicationRecord, top.zbeboy.zone.domain.tables.pojos.RoleApplication, Record2<String, String>> {

    /**
     * Create a new RoleApplicationDao without any configuration
     */
    public RoleApplicationDao() {
        super(RoleApplication.ROLE_APPLICATION, top.zbeboy.zone.domain.tables.pojos.RoleApplication.class);
    }

    /**
     * Create a new RoleApplicationDao with an attached configuration
     */
    @Autowired
    public RoleApplicationDao(Configuration configuration) {
        super(RoleApplication.ROLE_APPLICATION, top.zbeboy.zone.domain.tables.pojos.RoleApplication.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Record2<String, String> getId(top.zbeboy.zone.domain.tables.pojos.RoleApplication object) {
        return compositeKeyRecord(object.getRoleId(), object.getApplicationId());
    }

    /**
     * Fetch records that have <code>role_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.RoleApplication> fetchByRoleId(String... values) {
        return fetch(RoleApplication.ROLE_APPLICATION.ROLE_ID, values);
    }

    /**
     * Fetch records that have <code>application_id IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.RoleApplication> fetchByApplicationId(String... values) {
        return fetch(RoleApplication.ROLE_APPLICATION.APPLICATION_ID, values);
    }
}
