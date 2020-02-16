package top.zbeboy.zone.service.internship;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
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
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.internship.apply.InternshipApplyAddVo;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("internshipInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipInfoServiceImpl implements InternshipInfoService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private InternshipInfoDao internshipInfoDao;

    @Autowired
    InternshipInfoServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, INTERNSHIP_INFO, dataTablesUtil, false);
    }

    @Override
    public Result<Record> export(DataTablesUtil dataTablesUtil) {
        return queryAll(create, INTERNSHIP_INFO, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_INFO, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, INTERNSHIP_INFO, dataTablesUtil, false);
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

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String realName = StringUtils.trim(search.getString("realName"));
            String studentNumber = StringUtils.trim(search.getString("studentNumber"));
            String organizeName = StringUtils.trim(search.getString("organizeName"));
            String mobile = StringUtils.trim(search.getString("mobile"));
            String headmaster = StringUtils.trim(search.getString("headmaster"));
            String schoolGuidanceTeacher = StringUtils.trim(search.getString("schoolGuidanceTeacher"));
            if (StringUtils.isNotBlank(realName)) {
                a = INTERNSHIP_INFO.STUDENT_NAME.like(SQLQueryUtil.likeAllParam(realName));
            }

            if (StringUtils.isNotBlank(studentNumber)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_INFO.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber));
                } else {
                    a = a.and(INTERNSHIP_INFO.STUDENT_NUMBER.like(SQLQueryUtil.likeAllParam(studentNumber)));
                }
            }

            if (StringUtils.isNotBlank(organizeName)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_INFO.ORGANIZE_NAME.like(SQLQueryUtil.likeAllParam(organizeName));
                } else {
                    a = a.and(INTERNSHIP_INFO.ORGANIZE_NAME.eq(SQLQueryUtil.likeAllParam(organizeName)));
                }
            }

            if (StringUtils.isNotBlank(mobile)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_INFO.MOBILE.like(SQLQueryUtil.likeAllParam(mobile));
                } else {
                    a = a.and(INTERNSHIP_INFO.MOBILE.eq(SQLQueryUtil.likeAllParam(mobile)));
                }
            }

            if (StringUtils.isNotBlank(headmaster)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_INFO.HEADMASTER.like(SQLQueryUtil.likeAllParam(headmaster));
                } else {
                    a = a.and(INTERNSHIP_INFO.HEADMASTER.eq(SQLQueryUtil.likeAllParam(headmaster)));
                }
            }

            if (StringUtils.isNotBlank(schoolGuidanceTeacher)) {
                if (Objects.isNull(a)) {
                    a = INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER.like(SQLQueryUtil.likeAllParam(schoolGuidanceTeacher));
                } else {
                    a = a.and(INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER.eq(SQLQueryUtil.likeAllParam(schoolGuidanceTeacher)));
                }
            }
        }
        return a;
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String internshipReleaseId = StringUtils.trim(search.getString("internshipReleaseId"));
            a = INTERNSHIP_INFO.INTERNSHIP_RELEASE_ID.eq(internshipReleaseId);
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("studentName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.STUDENT_NAME.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.STUDENT_NAME.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                }
            }

            if (StringUtils.equals("organizeName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.ORGANIZE_NAME.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.ORGANIZE_NAME.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                }
            }

            if (StringUtils.equals("studentSex", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.STUDENT_SEX.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.STUDENT_SEX.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("studentNumber", orderColumnName)) {
                sortField = new SortField[1];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("mobile", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.MOBILE.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.MOBILE.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("qqMailbox", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.QQ_MAILBOX.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.QQ_MAILBOX.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("parentContactPhone", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.PARENT_CONTACT_PHONE.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.PARENT_CONTACT_PHONE.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("headmaster", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.HEADMASTER.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.HEADMASTER.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("headmasterTel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.HEADMASTER_TEL.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.HEADMASTER_TEL.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("companyName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_NAME.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_NAME.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("companyAddress", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_ADDRESS.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_ADDRESS.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("companyContact", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_CONTACT.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_CONTACT.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("companyMobile", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_MOBILE.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.COMPANY_MOBILE.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("schoolGuidanceTeacher", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("schoolGuidanceTeacherTel", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER_TEL.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.SCHOOL_GUIDANCE_TEACHER_TEL.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("startTime", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.START_TIME.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.START_TIME.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("endTime", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.END_TIME.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.END_TIME.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("commitmentBook", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.COMMITMENT_BOOK.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.COMMITMENT_BOOK.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("safetyResponsibilityBook", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.SAFETY_RESPONSIBILITY_BOOK.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.SAFETY_RESPONSIBILITY_BOOK.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("practiceAgreement", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.PRACTICE_AGREEMENT.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.PRACTICE_AGREEMENT.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("internshipApplication", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.INTERNSHIP_APPLICATION.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.INTERNSHIP_APPLICATION.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("practiceReceiving", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.PRACTICE_RECEIVING.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.PRACTICE_RECEIVING.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("securityEducationAgreement", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.SECURITY_EDUCATION_AGREEMENT.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.SECURITY_EDUCATION_AGREEMENT.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }

            if (StringUtils.equals("parentalConsent", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = INTERNSHIP_INFO.PARENTAL_CONSENT.asc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.asc();
                } else {
                    sortField[0] = INTERNSHIP_INFO.PARENTAL_CONSENT.desc();
                    sortField[1] = INTERNSHIP_INFO.STUDENT_NUMBER.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }
}
