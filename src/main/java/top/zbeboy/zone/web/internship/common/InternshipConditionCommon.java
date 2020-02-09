package top.zbeboy.zone.web.internship.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Component;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.util.BooleanUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Component
public class InternshipConditionCommon {

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private UsersService usersService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    /**
     * 是否可操作
     *
     * @param internshipReleaseId 发布id
     * @return true or false
     */
    public boolean canOperator(String internshipReleaseId) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (internshipReleaseRecord.isPresent()) {
                InternshipReleaseBean bean = internshipReleaseRecord.get().into(InternshipReleaseBean.class);

                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    int collegeId = 0;
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            College college = record.get().into(College.class);
                            collegeId = college.getCollegeId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            College college = record.get().into(College.class);
                            collegeId = college.getCollegeId();
                        }
                    }

                    canOperator = bean.getCollegeId() == collegeId;
                }
            }
        } else {
            Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (internshipReleaseRecord.isPresent()) {
                InternshipReleaseBean bean = internshipReleaseRecord.get().into(InternshipReleaseBean.class);
                Users users = usersService.getUserFromSession();
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
        if (canOperator(internshipReleaseId)) {
            Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (record.isPresent()) {
                InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                // 检测状态正常
                if (!BooleanUtil.toBoolean(internshipRelease.getInternshipReleaseIsDel())) {
                    canOperator = true;
                }
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
        if (canOperator(internshipReleaseId)) {
            Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
            if (record.isPresent()) {
                InternshipReleaseBean internshipRelease = record.get().into(InternshipReleaseBean.class);
                // 检测状态正常
                if (!BooleanUtil.toBoolean(internshipRelease.getInternshipReleaseIsDel())) {
                    // 检测教师分配时间
                    if (DateTimeUtil.nowAfterSqlTimestamp(internshipRelease.getTeacherDistributionStartTime()) &&
                            DateTimeUtil.nowBeforeSqlTimestamp(internshipRelease.getTeacherDistributionEndTime())) {
                        canOperator = true;
                    }
                }
            }
        }
        return canOperator;
    }
}
