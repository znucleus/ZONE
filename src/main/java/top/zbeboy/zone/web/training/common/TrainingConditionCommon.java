package top.zbeboy.zone.web.training.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.domain.tables.records.TrainingAuthoritiesRecord;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.training.TrainingAuthoritiesService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Component
public class TrainingConditionCommon {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingAuthoritiesService trainingAuthoritiesService;

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
     * @param trainingReleaseId 发布id
     * @return true or false
     */
    public boolean canOperator(String trainingReleaseId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(trainingReleaseId);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);

                Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                if (optionalUsersType.isPresent()) {
                    UsersType usersType = optionalUsersType.get();
                    int collegeId = 0;
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                        if (optionalStaffBean.isPresent()) {
                            collegeId = optionalStaffBean.get().getCollegeId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (optionalStudentBean.isPresent()) {
                            collegeId = optionalStudentBean.get().getCollegeId();
                        }
                    }

                    canOperator = bean.getCollegeId() == collegeId;
                }
            }
        } else {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(trainingReleaseId);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);
                canOperator = StringUtils.equals(bean.getUsername(), users.getUsername());
            }
        }

        return canOperator;
    }

    /**
     * 名单操作条件
     *
     * @param trainingReleaseId 实训发布id
     * @return true or false
     */
    public boolean usersCondition(String trainingReleaseId) {
        boolean canOperator = false;
        if (canOperator(trainingReleaseId)) {
            canOperator = true;
        } else {
            Users users = SessionUtil.getUserFromSession();
            Result<TrainingAuthoritiesRecord> records = trainingAuthoritiesService.findEffectiveByTrainingReleaseIdAndUsername(trainingReleaseId, users.getUsername());
            if (records.isNotEmpty()) {
                canOperator = true;
            }
        }
        return canOperator;
    }

    /**
     * 报告条件
     *
     * @return true or false
     */
    public boolean reportCondition() {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            canOperator = true;
        } else {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    canOperator = true;
                }
            }
        }
        return canOperator;
    }

    /**
     * 专题条件
     *
     * @return true or false
     */
    public boolean specialCondition() {
        Users users = SessionUtil.getUserFromSession();
        return roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name());
    }
}
