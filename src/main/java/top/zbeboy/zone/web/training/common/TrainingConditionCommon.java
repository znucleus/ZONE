package top.zbeboy.zone.web.training.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.domain.tables.records.TrainingAuthoritiesRecord;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.training.TrainingAuthoritiesService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;

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
     * @param trainingReleaseId 发布id
     * @return true or false
     */
    public boolean canOperator(String trainingReleaseId) {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(trainingReleaseId);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);

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
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(trainingReleaseId);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);
                Users users = usersService.getUserFromSession();
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
            Users users = usersService.getUserFromSession();
            Result<TrainingAuthoritiesRecord> records = trainingAuthoritiesService.findEffectiveByTrainingReleaseIdAndUsername(trainingReleaseId, users.getUsername());
            if (records.isNotEmpty()) {
                canOperator = true;
            }
        }
        return canOperator;
    }
}
