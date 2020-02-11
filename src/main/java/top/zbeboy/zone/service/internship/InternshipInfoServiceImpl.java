package top.zbeboy.zone.service.internship;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.InternshipInfoDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipInfo;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.vo.internship.apply.InternshipApplyAddVo;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.INTERNSHIP_APPLY;
import static top.zbeboy.zone.domain.Tables.INTERNSHIP_CHANGE_HISTORY;
import static top.zbeboy.zone.domain.Tables.INTERNSHIP_INFO;

@Service("internshipInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipInfoServiceImpl implements InternshipInfoService {

    private final DSLContext create;

    @Resource
    private InternshipInfoDao internshipInfoDao;

    @Autowired
    InternshipInfoServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.INTERNSHIP_INFO, key = "#internshipReleaseId + '_' + #studentId")
    @Override
    public Optional<Record> findByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        return create.select()
                .from(INTERNSHIP_INFO)
                .where(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId).and(INTERNSHIP_INFO.STUDENT_ID.eq(studentId)))
                .fetchOptional();
    }

    @Override
    public void saveWithTransaction(InternshipApplyAddVo internshipApplyAddVo) {
        create.transaction(configuration -> {
            int state = 0;
            DSL.using(configuration)
                    .insertInto(INTERNSHIP_APPLY)
                    .set(INTERNSHIP_APPLY.INTERNSHIP_APPLY_ID, UUIDUtil.getUUID())
                    .set(INTERNSHIP_APPLY.INTERNSHIP_RELEASE_ID, internshipApplyAddVo.getInternshipReleaseId())
                    .set(INTERNSHIP_APPLY.STUDENT_ID, internshipApplyAddVo.getStudentId())
                    .set(INTERNSHIP_APPLY.APPLY_TIME, DateTimeUtil.getNowSqlTimestamp())
                    .set(INTERNSHIP_APPLY.INTERNSHIP_APPLY_STATE, state)
                    .execute();

            DSL.using(configuration)
                    .insertInto(INTERNSHIP_INFO)
                    .set(INTERNSHIP_INFO.INTERNSHIP_INFO_ID, UUIDUtil.getUUID())
                    .set(INTERNSHIP_INFO.STUDENT_ID, internshipApplyAddVo.getStudentId())
                    .set(INTERNSHIP_INFO.STUDENT_USERNAME, internshipApplyAddVo.getUsername())
                    .set(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID, internshipApplyAddVo.getInternshipReleaseId())
                    .set(INTERNSHIP_INFO.STUDENT_NAME, internshipApplyAddVo.getRealName())
                    .set(INTERNSHIP_INFO.ORGANIZE_NAME, internshipApplyAddVo.getOrganizeName())
                    .set(INTERNSHIP_INFO.STUDENT_SEX, internshipApplyAddVo.getStudentSex())
                    .set(INTERNSHIP_INFO.STUDENT_NUMBER, internshipApplyAddVo.getStudentNumber())
                    .set(INTERNSHIP_INFO.MOBILE, internshipApplyAddVo.getMobile())
                    .set(INTERNSHIP_INFO.QQ_MAILBOX, internshipApplyAddVo.getQqMailbox())
                    .set(INTERNSHIP_INFO.PARENT_CONTACT_PHONE, internshipApplyAddVo.getParentContactPhone())
                    .set(INTERNSHIP_INFO.HEADMASTER, internshipApplyAddVo.getHeadmaster())
                    .set(INTERNSHIP_INFO.HEADMASTER_TEL, internshipApplyAddVo.getHeadmasterTel())
                    .set(INTERNSHIP_INFO.COMPANY_NAME, internshipApplyAddVo.getCompanyName())
                    .set(INTERNSHIP_INFO.COMPANY_ADDRESS, internshipApplyAddVo.getCompanyAddress())
                    .set(INTERNSHIP_INFO.COMPANY_CONTACT, internshipApplyAddVo.getCompanyContact())
                    .set(INTERNSHIP_INFO.COMPANY_MOBILE, internshipApplyAddVo.getCompanyMobile())
                    .set(INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER, internshipApplyAddVo.getSchoolGuidanceTeacher())
                    .set(INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER_TEL, internshipApplyAddVo.getSchoolGuidanceTeacherTel())
                    .set(INTERNSHIP_INFO.START_TIME, DateTimeUtil.defaultParseSqlDate(internshipApplyAddVo.getStartTime()))
                    .set(INTERNSHIP_INFO.END_TIME, DateTimeUtil.defaultParseSqlDate(internshipApplyAddVo.getEndTime()))
                    .execute();

            DSL.using(configuration)
                    .insertInto(INTERNSHIP_CHANGE_HISTORY)
                    .set(INTERNSHIP_CHANGE_HISTORY.INTERNSHIP_CHANGE_HISTORY_ID, UUIDUtil.getUUID())
                    .set(INTERNSHIP_CHANGE_HISTORY.INTERNSHIP_RELEASE_ID, internshipApplyAddVo.getInternshipReleaseId())
                    .set(INTERNSHIP_CHANGE_HISTORY.STUDENT_ID, internshipApplyAddVo.getStudentId())
                    .set(INTERNSHIP_CHANGE_HISTORY.STATE, state)
                    .set(INTERNSHIP_CHANGE_HISTORY.APPLY_TIME, DateTimeUtil.getNowSqlTimestamp())
                    .execute();
        });
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_INFO, key = "#internshipInfo.internshipReleaseId + '_' + #internshipInfo.studentId")
    @Override
    public void update(InternshipInfo internshipInfo) {
        internshipInfoDao.update(internshipInfo);
    }

    @CacheEvict(cacheNames = CacheBook.INTERNSHIP_INFO, key = "#internshipReleaseId + '_' + #studentId")
    @Override
    public void deleteByInternshipReleaseIdAndStudentId(String internshipReleaseId, int studentId) {
        create.deleteFrom(INTERNSHIP_INFO).where(INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId)
        .and(INTERNSHIP_INFO.STUDENT_ID.eq(studentId))).execute();
    }
}
