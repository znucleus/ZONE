package top.zbeboy.zone.web.internship.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.service.internship.*;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Component
public class InternshipConditionCommon {

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipTeacherDistributionService internshipTeacherDistributionService;

    @Resource
    private InternshipApplyService internshipApplyService;

    @Resource
    private InternshipReviewAuthorizeService internshipReviewAuthorizeService;

    @Resource
    private InternshipJournalService internshipJournalService;

    @Resource
    private InternshipRegulateService internshipRegulateService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private RoleService roleService;

    /**
     * 是否可操作
     *
     * @param internshipReleaseId 发布id
     * @return true or false
     */
    public boolean canOperator(String internshipReleaseId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (internshipReleaseRecord.isPresent()) {
                InternshipReleaseBean bean = internshipReleaseRecord.get().into(InternshipReleaseBean.class);

                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                    int collegeId = 0;
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                            collegeId = staffBean.getCollegeId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                            collegeId = studentBean.getCollegeId();
                        }
                    }

                    canOperator = bean.getCollegeId() == collegeId;
                }
            }
        } else {
            Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (internshipReleaseRecord.isPresent()) {
                InternshipReleaseBean bean = internshipReleaseRecord.get().into(InternshipReleaseBean.class);
                canOperator = StringUtils.equals(bean.getUsername(), users.getUsername());
            }
        }

        return canOperator;
    }

    /**
     * 基础条件
     *
     * @param internshipReleaseId 发布id
     * @return true or false
     */
    public boolean basicCondition(String internshipReleaseId) {
        boolean canOperator = false;
        Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
        if (record.isPresent()) {
            InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
            // 检测状态正常
            if (!BooleanUtil.toBoolean(internshipRelease.getInternshipReleaseIsDel())) {
                canOperator = true;
            }
        }
        return canOperator;
    }

    /**
     * 教师分配条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean teacherDistributionCondition(String internshipReleaseId) {
        boolean canOperator = false;
        if (basicCondition(internshipReleaseId) && canOperator(internshipReleaseId)) {
            Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (record.isPresent()) {
                InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                // 是否需要时间限制
                boolean isTimeLimit = BooleanUtil.toBoolean(internshipRelease.getIsTimeLimit());
                if (isTimeLimit) {
                    // 检测教师分配时间
                    if (DateTimeUtil.nowAfterSqlTimestamp(internshipRelease.getTeacherDistributionStartTime()) &&
                            DateTimeUtil.nowBeforeSqlTimestamp(internshipRelease.getTeacherDistributionEndTime())) {
                        canOperator = true;
                    }
                } else {
                    canOperator = true;
                }
            }
        }
        return canOperator;
    }

    /**
     * 申请条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean applyCondition(String internshipReleaseId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
            // 检测是否学生类型
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                    Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
                    if (record.isPresent()) {
                        InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                        // 检测是否属于发布的专业
                        if (Objects.equals(studentBean.getScienceId(), internshipRelease.getScienceId())) {
                            // 检测状态正常
                            if (basicCondition(internshipReleaseId)) {
                                // 是否限制时间
                                boolean isTimeLimit = BooleanUtil.toBoolean(internshipRelease.getIsTimeLimit());
                                if (isTimeLimit) {
                                    // 检测实习申请时间
                                    if (DateTimeUtil.nowAfterSqlTimestamp(internshipRelease.getStartTime()) &&
                                            DateTimeUtil.nowBeforeSqlTimestamp(internshipRelease.getEndTime())) {
                                        // 检测是否有指导老师
                                        Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                        if (internshipTeacherDistributionRecord.isPresent()) {
                                            // 检测是否申请过
                                            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                            if (!internshipApplyRecord.isPresent()) {
                                                canOperator = true;
                                            }
                                        }
                                    }
                                } else {
                                    // 检测是否有指导老师
                                    Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                    if (internshipTeacherDistributionRecord.isPresent()) {
                                        // 检测是否申请过
                                        Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                        if (!internshipApplyRecord.isPresent()) {
                                            canOperator = true;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        return canOperator;
    }

    /**
     * 申请编辑条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean applyEditCondition(String internshipReleaseId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
            // 检测是否学生类型
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                    Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
                    if (record.isPresent()) {
                        InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                        // 检测是否属于发布的专业
                        if (Objects.equals(studentBean.getScienceId(), internshipRelease.getScienceId())) {
                            // 检测状态正常
                            if (basicCondition(internshipReleaseId)) {
                                // 是否限制时间
                                boolean isTimeLimit = BooleanUtil.toBoolean(internshipRelease.getIsTimeLimit());
                                if (isTimeLimit) {
                                    // 检测实习申请时间
                                    if (DateTimeUtil.nowAfterSqlTimestamp(internshipRelease.getStartTime()) &&
                                            DateTimeUtil.nowBeforeSqlTimestamp(internshipRelease.getEndTime())) {

                                        // 检测是否有指导老师
                                        Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                        if (internshipTeacherDistributionRecord.isPresent()) {
                                            // 检测是否申请过
                                            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                            if (internshipApplyRecord.isPresent()) {
                                                InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                                                // 时间范围内，以下几种状态都可直接编辑 0:未提交，3:未通过
                                                if (internshipApply.getInternshipApplyState() == 0 || internshipApply.getInternshipApplyState() == 3) {
                                                    canOperator = true;
                                                } else if (internshipApply.getInternshipApplyState() == 5 || internshipApply.getInternshipApplyState() == 7) {
                                                    // 状态为 5：基本信息变更填写中 或 7：单位信息变更填写中 位于这两个状态，一定是通过审核后的 无视实习时间条件 但需要判断更改时间条件
                                                    // 检测变更时间
                                                    if (Objects.nonNull(internshipApply.getChangeFillStartTime()) &&
                                                            Objects.nonNull(internshipApply.getChangeFillEndTime()) &&
                                                            DateTimeUtil.nowAfterSqlTimestamp(internshipApply.getChangeFillStartTime()) &&
                                                            DateTimeUtil.nowBeforeSqlTimestamp(internshipApply.getChangeFillEndTime())) {
                                                        canOperator = true;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        // 不在申请时间范围
                                        // 检测是否申请过
                                        Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                        if (internshipApplyRecord.isPresent()) {
                                            InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                                            // 状态为 5：基本信息变更填写中 或 7：单位信息变更填写中 位于这两个状态，一定是通过审核后的 无视实习时间条件 但需要判断更改时间条件
                                            if (internshipApply.getInternshipApplyState() == 5 || internshipApply.getInternshipApplyState() == 7) {
                                                // 判断更改时间条件
                                                // 检测变更时间
                                                if (Objects.nonNull(internshipApply.getChangeFillStartTime()) &&
                                                        Objects.nonNull(internshipApply.getChangeFillEndTime()) &&
                                                        DateTimeUtil.nowAfterSqlTimestamp(internshipApply.getChangeFillStartTime()) &&
                                                        DateTimeUtil.nowBeforeSqlTimestamp(internshipApply.getChangeFillEndTime())) {
                                                    canOperator = true;
                                                }
                                            } else if (internshipApply.getInternshipApplyState() == 3) {
                                                canOperator = true;
                                            }
                                        }
                                    }
                                } else {
                                    // 检测是否有指导老师
                                    Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                    if (internshipTeacherDistributionRecord.isPresent()) {
                                        // 检测是否申请过
                                        Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                                        if (internshipApplyRecord.isPresent()) {
                                            InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                                            // 以下几种状态都可直接编辑 0:未提交，3:未通过
                                            if (internshipApply.getInternshipApplyState() == 0 || internshipApply.getInternshipApplyState() == 3) {
                                                canOperator = true;
                                            } else if (internshipApply.getInternshipApplyState() == 5 || internshipApply.getInternshipApplyState() == 7) {
                                                // 状态为 5：基本信息变更填写中 或 7：单位信息变更填写中 位于这两个状态，一定是通过审核后的 无视实习时间条件 但需要判断更改时间条件
                                                // 检测变更时间
                                                if (Objects.nonNull(internshipApply.getChangeFillStartTime()) &&
                                                        Objects.nonNull(internshipApply.getChangeFillEndTime()) &&
                                                        DateTimeUtil.nowAfterSqlTimestamp(internshipApply.getChangeFillStartTime()) &&
                                                        DateTimeUtil.nowBeforeSqlTimestamp(internshipApply.getChangeFillEndTime())) {
                                                    canOperator = true;
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }

        return canOperator;
    }

    /**
     * 审核条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean reviewCondition(String internshipReleaseId) {
        boolean canOperator = false;
        if (basicCondition(internshipReleaseId)) {
            if (canOperator(internshipReleaseId)) {
                canOperator = true;
            } else {
                // 查询审核表
                Users users = SessionUtil.getUserFromSession();
                Optional<Record> record = internshipReviewAuthorizeService.findByInternshipReleaseIdAndUsername(internshipReleaseId, users.getUsername());
                canOperator = record.isPresent();
            }
        }
        return canOperator;
    }

    /**
     * 审核权限条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean reviewAuthorizeCondition(String internshipReleaseId) {
        return basicCondition(internshipReleaseId) && canOperator(internshipReleaseId);
    }

    /**
     * 查看我的日志条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean journalLookMyCondition(String internshipReleaseId) {
        boolean canOperator = false;
        if (basicCondition(internshipReleaseId)) {
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                        if (internshipTeacherDistributionRecord.isPresent()) {
                            canOperator = true;
                        }
                    }
                }
            }
        }

        return canOperator;
    }

    /**
     * 写日志条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean journalCondition(String internshipReleaseId) {
        boolean canOperator = false;
        if (basicCondition(internshipReleaseId)) {
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                        if (internshipTeacherDistributionRecord.isPresent()) {
                            // 检测是否申请过
                            Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentBean.getStudentId());
                            if (internshipApplyRecord.isPresent()) {
                                InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                                // 状态为 2：已通过；4：基本信息变更申请中；5：基本信息变更填写中；6：单位信息变更申请中；7：单位信息变更填写中 允许进行填写
                                if (internshipApply.getInternshipApplyState() == 2 || internshipApply.getInternshipApplyState() == 4 ||
                                        internshipApply.getInternshipApplyState() == 5 || internshipApply.getInternshipApplyState() == 6 || internshipApply.getInternshipApplyState() == 7) {
                                    canOperator = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return canOperator;
    }

    /**
     * 编辑删除日志条件
     *
     * @param internshipJournalId 日志id
     * @return true or false
     */
    public boolean journalEditCondition(String internshipJournalId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            // 本院管理员可操作
            int collegeId = 0;
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                        collegeId = staffBean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }
            }
            InternshipJournal internshipJournal = internshipJournalService.findById(internshipJournalId);
            if (Objects.nonNull(internshipJournal)) {
                Optional<Record> record = internshipReleaseService.findByIdRelation(internshipJournal.getInternshipReleaseId());
                if (record.isPresent()) {
                    InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                    canOperator = internshipRelease.getCollegeId() == collegeId;
                }
            }
        } else {
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                        // 是否是指导教师
                        InternshipJournal internshipJournal = internshipJournalService.findById(internshipJournalId);
                        if (Objects.nonNull(internshipJournal)) {
                            canOperator = Objects.equals(staffBean.getStaffId(), internshipJournal.getStaffId());
                        }
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        // 是否是本人
                        InternshipJournal internshipJournal = internshipJournalService.findById(internshipJournalId);
                        if (Objects.nonNull(internshipJournal)) {
                            canOperator = Objects.equals(studentBean.getStudentId(), internshipJournal.getStudentId());
                        }
                    }
                }
            }
        }

        return canOperator;
    }

    /**
     * 查看下载日志条件
     *
     * @param internshipJournalId 日志id
     * @return true or false
     */
    public boolean journalLookCondition(String internshipJournalId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            canOperator = true;
        } else {
            InternshipJournal internshipJournal = internshipJournalService.findById(internshipJournalId);
            if (Objects.nonNull(internshipJournal)) {
                if (internshipJournal.getIsSeeStaff() == 1) {
                    UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                    if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                        canOperator = StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName());
                    }
                } else {
                    canOperator = true;
                }
            }
        }

        return canOperator;
    }

    /**
     * 写监管条件
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    public boolean regulateCondition(String internshipReleaseId) {
        boolean canOperator = false;
        if (basicCondition(internshipReleaseId)) {
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                        Result<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStaffId(internshipReleaseId, staffBean.getStaffId());
                        if (internshipTeacherDistributionRecord.isNotEmpty()) {
                            canOperator = true;
                        }
                    }
                }
            }
        }

        return canOperator;
    }

    /**
     * 编辑删除记录条件
     *
     * @param internshipRegulateId 日志id
     * @return true or false
     */
    public boolean regulateEditCondition(String internshipRegulateId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            // 本院管理员可操作
            int collegeId = 0;
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                        collegeId = staffBean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }
            }
            InternshipRegulate internshipRegulate = internshipRegulateService.findById(internshipRegulateId);
            if (Objects.nonNull(internshipRegulate)) {
                Optional<Record> record = internshipReleaseService.findByIdRelation(internshipRegulate.getInternshipReleaseId());
                if (record.isPresent()) {
                    InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                    canOperator = internshipRelease.getCollegeId() == collegeId;
                }
            }
        } else {
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(staffBean) && Objects.nonNull(staffBean.getStaffId()) && staffBean.getStaffId() > 0) {
                        // 是否是指导教师本人
                        InternshipRegulate internshipRegulate = internshipRegulateService.findById(internshipRegulateId);
                        if (Objects.nonNull(internshipRegulate)) {
                            canOperator = Objects.equals(staffBean.getStaffId(), internshipRegulate.getStaffId());
                        }
                    }
                }
            }
        }

        return canOperator;
    }
}
