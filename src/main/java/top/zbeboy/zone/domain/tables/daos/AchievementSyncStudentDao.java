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

import top.zbeboy.zone.domain.tables.AchievementSyncStudent;
import top.zbeboy.zone.domain.tables.records.AchievementSyncStudentRecord;


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
public class AchievementSyncStudentDao extends DAOImpl<AchievementSyncStudentRecord, top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent, String> {

    /**
     * Create a new AchievementSyncStudentDao without any configuration
     */
    public AchievementSyncStudentDao() {
        super(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT, top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent.class);
    }

    /**
     * Create a new AchievementSyncStudentDao with an attached configuration
     */
    @Autowired
    public AchievementSyncStudentDao(Configuration configuration) {
        super(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT, top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getId(top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent object) {
        return object.getStudentNumber();
    }

    /**
     * Fetch records that have <code>student_number IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByStudentNumber(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.STUDENT_NUMBER, values);
    }

    /**
     * Fetch a unique record that has <code>student_number = value</code>
     */
    public top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent fetchOneByStudentNumber(String value) {
        return fetchOne(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.STUDENT_NUMBER, value);
    }

    /**
     * Fetch records that have <code>real_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByRealName(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.REAL_NAME, values);
    }

    /**
     * Fetch records that have <code>sex IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchBySex(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.SEX, values);
    }

    /**
     * Fetch records that have <code>science_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByScienceName(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.SCIENCE_NAME, values);
    }

    /**
     * Fetch records that have <code>organize_name IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByOrganizeName(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.ORGANIZE_NAME, values);
    }

    /**
     * Fetch records that have <code>grade IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByGrade(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.GRADE, values);
    }

    /**
     * Fetch records that have <code>is_pe_student IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByIsPeStudent(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.IS_PE_STUDENT, values);
    }

    /**
     * Fetch records that have <code>student_status IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByStudentStatus(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.STUDENT_STATUS, values);
    }

    /**
     * Fetch records that have <code>alien_type IN (values)</code>
     */
    public List<top.zbeboy.zone.domain.tables.pojos.AchievementSyncStudent> fetchByAlienType(String... values) {
        return fetch(AchievementSyncStudent.ACHIEVEMENT_SYNC_STUDENT.ALIEN_TYPE, values);
    }
}
