package top.zbeboy.zone.web.register.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.EpidemicRegisterData;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.data.StudentService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.register.EpidemicRegisterDataService;
import top.zbeboy.zone.service.register.LeaverRegisterDataService;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.service.register.LeaverRegisterScopeService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Objects;

@Component
public class RegisterConditionCommon {

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private EpidemicRegisterDataService epidemicRegisterDataService;

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private LeaverRegisterScopeService leaverRegisterScopeService;

    @Resource
    private LeaverRegisterDataService leaverRegisterDataService;

    /**
     * 是否可操作
     *
     * @return true or false
     */
    public boolean epidemicOperator() {
        return roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name());
    }

    /**
     * 是否可统计
     *
     * @return true or false
     */
    public boolean epidemicReview() {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            canOperator = true;
        } else {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                canOperator = StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName());
            }
        }
        return canOperator;
    }

    /**
     * 是否可删除
     *
     * @return true or false
     */
    public boolean epidemicDelete(String epidemicRegisterDataId) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else {
            EpidemicRegisterData epidemicRegisterData = epidemicRegisterDataService.findById(epidemicRegisterDataId);
            if (Objects.nonNull(epidemicRegisterData)) {
                if (StringUtils.equals(epidemicRegisterData.getRegisterType(), Workbook.STAFF_USERS_TYPE)) {
                    if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                        Users users = usersService.getUserFromSession();
                        StaffBean bean1 = staffService.findByUsernameRelation(users.getUsername());
                        StaffBean bean2 = staffService.findByUsernameRelation(epidemicRegisterData.getRegisterUsername());
                        if (Objects.nonNull(bean1) && bean1.getStaffId() > 0 && Objects.nonNull(bean2) && bean2.getStaffId() > 0) {
                            canOperator = Objects.equals(bean1.getCollegeId(), bean2.getCollegeId());
                        }
                    } else {
                        Users users = usersService.getUserFromSession();
                        canOperator = StringUtils.equals(users.getUsername(), epidemicRegisterData.getRegisterUsername());
                    }
                } else if (StringUtils.equals(epidemicRegisterData.getRegisterType(), Workbook.STUDENT_USERS_TYPE)) {
                    if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                        Users users = usersService.getUserFromSession();
                        StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                        StudentBean studentBean = studentService.findByUsernameRelation(epidemicRegisterData.getRegisterUsername());
                        if (Objects.nonNull(staffBean) && staffBean.getStaffId() > 0 && Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                            canOperator = Objects.equals(staffBean.getCollegeId(), studentBean.getCollegeId());
                        }
                    } else {
                        Users users = usersService.getUserFromSession();
                        if (StringUtils.equals(users.getUsername(), epidemicRegisterData.getRegisterUsername())) {
                            canOperator = true;
                        } else {
                            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                            if (Objects.nonNull(usersType)) {
                                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                                    StaffBean staffBean = staffService.findByUsernameRelation(users.getUsername());
                                    StudentBean studentBean = studentService.findByUsernameRelation(epidemicRegisterData.getRegisterUsername());
                                    if (Objects.nonNull(staffBean) && staffBean.getStaffId() > 0 && Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                                        canOperator = Objects.equals(staffBean.getDepartmentId(), studentBean.getDepartmentId());
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
     * 是否可操作
     *
     * @return true or false
     */
    public boolean leaverOperator(String leaverRegisterReleaseId, String channel, Principal principal) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            // 发布人所在院与管理员院相同
            int collegeId = getCurrentUserCollegeId(channel, principal);
            int publishCollegeId = getPublishCollegeId(leaverRegisterReleaseId);
            canOperator = publishCollegeId != 0 && collegeId == publishCollegeId;
        } else {
            LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
            if (Objects.nonNull(leaverRegisterRelease)) {
                Users users = usersService.getUserByChannel(channel, principal);
                canOperator = StringUtils.equals(users.getUsername(), leaverRegisterRelease.getUsername());
            }
        }
        return canOperator;
    }

    /**
     * 是否可统计
     *
     * @return true or false
     */
    public boolean leaverReview(String leaverRegisterReleaseId, String channel, Principal principal) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            // 发布人所在院与管理员院相同
            int collegeId = getCurrentUserCollegeId(channel, principal);
            int publishCollegeId = getPublishCollegeId(leaverRegisterReleaseId);
            canOperator = publishCollegeId != 0 && collegeId == publishCollegeId;
        } else {
            Users users = usersService.getUserByChannel(channel, principal);
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        int collegeId = bean.getCollegeId();
                        int publishCollegeId = getPublishCollegeId(leaverRegisterReleaseId);
                        canOperator = publishCollegeId != 0 && collegeId == publishCollegeId;
                    }
                } else {
                    LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
                    if (Objects.nonNull(leaverRegisterRelease)) {
                        canOperator = StringUtils.equals(users.getUsername(), leaverRegisterRelease.getUsername());
                    }
                }
            }
        }
        return canOperator;
    }

    /**
     * 登记条件
     *
     * @param leaverRegisterReleaseId id
     * @return true or false
     */
    public boolean leaverRegister(String leaverRegisterReleaseId, String channel, Principal principal) {
        boolean canOperator = false;
        Users users = usersService.getUserByChannel(channel, principal);
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                    LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
                    if (Objects.nonNull(leaverRegisterRelease)) {
                        Result<LeaverRegisterScopeRecord> registerScopeRecords = leaverRegisterScopeService.findByLeaverRegisterReleaseId(leaverRegisterRelease.getLeaverRegisterReleaseId());
                        if (registerScopeRecords.isNotEmpty()) {
                            for (LeaverRegisterScopeRecord record : registerScopeRecords) {
                                switch (leaverRegisterRelease.getDataScope()) {
                                    case 1:
                                        // 院
                                        canOperator = Objects.equals(studentBean.getCollegeId(), record.getDataId());
                                        break;
                                    case 2:
                                        // 系
                                        canOperator = Objects.equals(studentBean.getDepartmentId(), record.getDataId());
                                        break;
                                    case 3:
                                        // 专业
                                        canOperator = Objects.equals(studentBean.getScienceId(), record.getDataId());
                                        break;
                                    case 4:
                                        // 年级
                                        canOperator = Objects.equals(studentBean.getGradeId(), record.getDataId());
                                        break;
                                    case 5:
                                        // 班级
                                        canOperator = Objects.equals(studentBean.getOrganizeId(), record.getDataId());
                                        break;
                                }

                                if (canOperator) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return canOperator;
    }

    private int getCurrentUserCollegeId(String channel, Principal principal) {
        int collegeId = 0;
        Users users = usersService.getUserByChannel(channel, principal);
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                    collegeId = bean.getCollegeId();
                }
            } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                    collegeId = studentBean.getCollegeId();
                }
            }
        }
        return collegeId;
    }

    private int getPublishCollegeId(String leaverRegisterReleaseId) {
        int publishCollegeId = 0;
        LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
        if (Objects.nonNull(leaverRegisterRelease)) {
            Users users = usersService.findByUsername(leaverRegisterRelease.getUsername());
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        publishCollegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                        publishCollegeId = studentBean.getCollegeId();
                    }
                }
            }
        }

        return publishCollegeId;
    }

    boolean isRegisterLeaver(String leaverRegisterReleaseId, String channel, Principal principal) {
        boolean isRegister = false;
        Users users = usersService.getUserByChannel(channel, principal);
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsername(users.getUsername());
                if (Objects.nonNull(studentBean) && studentBean.getStudentId() > 0) {
                    isRegister = leaverRegisterDataService.findByLeaverRegisterReleaseIdAndStudentId(leaverRegisterReleaseId, studentBean.getStudentId()).isPresent();
                }
            }
        }
        return isRegister;
    }

    boolean isStudent(String channel, Principal principal) {
        boolean isStudent = false;
        Users users = usersService.getUserByChannel(channel, principal);
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                isStudent = true;
            }
        }
        return isStudent;
    }
}
