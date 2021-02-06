package top.zbeboy.zone.web.theory.common;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.domain.tables.records.TheoryAuthoritiesRecord;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.theory.TheoryAuthoritiesService;
import top.zbeboy.zone.service.theory.TheoryReleaseService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Component
public class TheoryConditionCommon {

    @Resource
    private TheoryReleaseService theoryReleaseService;

    @Resource
    private TheoryAuthoritiesService theoryAuthoritiesService;

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
     * @param theoryReleaseId 发布id
     * @return true or false
     */
    public boolean canOperator(String theoryReleaseId) {
        boolean canOperator = false;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            canOperator = true;
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> theoryReleaseRecord = theoryReleaseService.findByIdRelation(theoryReleaseId);
            if (theoryReleaseRecord.isPresent()) {
                TheoryReleaseBean bean = theoryReleaseRecord.get().into(TheoryReleaseBean.class);

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
            Optional<Record> theoryReleaseRecord = theoryReleaseService.findByIdRelation(theoryReleaseId);
            if (theoryReleaseRecord.isPresent()) {
                TheoryReleaseBean bean = theoryReleaseRecord.get().into(TheoryReleaseBean.class);
                canOperator = StringUtils.equals(bean.getUsername(), users.getUsername());
            }
        }

        return canOperator;
    }

    /**
     * 名单操作条件
     *
     * @param theoryReleaseId 理论发布id
     * @return true or false
     */
    public boolean usersCondition(String theoryReleaseId) {
        boolean canOperator = false;
        if (canOperator(theoryReleaseId)) {
            canOperator = true;
        } else {
            Users users = SessionUtil.getUserFromSession();
            Result<TheoryAuthoritiesRecord> records = theoryAuthoritiesService.findEffectiveByTheoryReleaseIdAndUsername(theoryReleaseId, users.getUsername());
            if (records.isNotEmpty()) {
                canOperator = true;
            }
        }
        return canOperator;
    }
}
