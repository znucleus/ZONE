package top.zbeboy.zone.web.register.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.register.EpidemicRegisterDataService;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.service.register.LeaverRegisterScopeService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

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
    private CollegeService collegeService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScienceService scienceService;

    @Resource
    private GradeService gradeService;

    @Resource
    private OrganizeService organizeService;

    @Resource
    private EpidemicRegisterDataService epidemicRegisterDataService;

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private LeaverRegisterScopeService leaverRegisterScopeService;

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
                        Optional<Record> record1 = staffService.findByUsernameRelation(users.getUsername());
                        Optional<Record> record2 = staffService.findByUsernameRelation(epidemicRegisterData.getRegisterUsername());
                        if (record1.isPresent() && record2.isPresent()) {
                            StaffBean bean1 = record1.get().into(StaffBean.class);
                            StaffBean bean2 = record2.get().into(StaffBean.class);
                            canOperator = Objects.equals(bean1.getCollegeId(), bean2.getCollegeId());
                        }
                    } else {
                        Users users = usersService.getUserFromSession();
                        canOperator = StringUtils.equals(users.getUsername(), epidemicRegisterData.getRegisterUsername());
                    }
                } else if (StringUtils.equals(epidemicRegisterData.getRegisterType(), Workbook.STUDENT_USERS_TYPE)) {
                    if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                        Users users = usersService.getUserFromSession();
                        Optional<Record> record1 = staffService.findByUsernameRelation(users.getUsername());
                        Optional<Record> record2 = studentService.findByUsernameRelation(epidemicRegisterData.getRegisterUsername());
                        if (record1.isPresent() && record2.isPresent()) {
                            StaffBean bean1 = record1.get().into(StaffBean.class);
                            StudentBean bean2 = record2.get().into(StudentBean.class);
                            canOperator = Objects.equals(bean1.getCollegeId(), bean2.getCollegeId());
                        }
                    } else {
                        Users users = usersService.getUserFromSession();
                        if (StringUtils.equals(users.getUsername(), epidemicRegisterData.getRegisterUsername())) {
                            canOperator = true;
                        } else {
                            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                            if (Objects.nonNull(usersType)) {
                                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                                    Optional<Record> record1 = staffService.findByUsernameRelation(users.getUsername());
                                    Optional<Record> record2 = studentService.findByUsernameRelation(epidemicRegisterData.getRegisterUsername());
                                    if (record1.isPresent() && record2.isPresent()) {
                                        StaffBean bean1 = record1.get().into(StaffBean.class);
                                        StudentBean bean2 = record2.get().into(StudentBean.class);
                                        canOperator = Objects.equals(bean1.getDepartmentId(), bean2.getDepartmentId());
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
    public boolean leaverOperator(String leaverRegisterReleaseId) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else {
            LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
            if (Objects.nonNull(leaverRegisterRelease)) {
                Users users = usersService.getUserFromSession();
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
    public boolean leaverReview(String leaverRegisterReleaseId) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            canOperator = true;
        } else {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    canOperator = true;
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
    public boolean leaverRegister(String leaverRegisterReleaseId) {
        boolean canOperator = false;
        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<Record> studentRecord = studentService.findByUsernameRelation(users.getUsername());
                if (studentRecord.isPresent()) {
                    StudentBean student = studentRecord.get().into(StudentBean.class);
                    LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(leaverRegisterReleaseId);
                    if (Objects.nonNull(leaverRegisterRelease)) {
                        Result<LeaverRegisterScopeRecord> registerScopeRecords = leaverRegisterScopeService.findByLeaverRegisterReleaseId(leaverRegisterRelease.getLeaverRegisterReleaseId());
                        if (registerScopeRecords.isNotEmpty()) {
                            for (LeaverRegisterScopeRecord record : registerScopeRecords) {
                                switch (leaverRegisterRelease.getDataScope()) {
                                    case 1:
                                        // 院
                                        canOperator = Objects.equals(student.getCollegeId(), record.getDataId());
                                        break;
                                    case 2:
                                        // 系
                                        canOperator = Objects.equals(student.getDepartmentId(), record.getDataId());
                                        break;
                                    case 3:
                                        // 专业
                                        canOperator = Objects.equals(student.getScienceId(), record.getDataId());
                                        break;
                                    case 4:
                                        // 年级
                                        canOperator = Objects.equals(student.getGradeId(), record.getDataId());
                                        break;
                                    case 5:
                                        // 班级
                                        canOperator = Objects.equals(student.getOrganizeId(), record.getDataId());
                                        break;
                                }

                                if(canOperator){
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
}
