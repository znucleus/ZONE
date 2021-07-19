package top.zbeboy.zone.web.internship.release;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class InternshipReleaseViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private RoleService roleService;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    /**
     * 实习发布数据
     *
     * @return 实习发布数据页面
     */
    @GetMapping("/web/menu/internship/release")
    public String index() {
        return "web/internship/release/internship_release::#page-wrapper";
    }

    /**
     * 实习发布添加页面
     *
     * @param modelMap 页面对象
     * @return 实习发布添加页面
     */
    @GetMapping("/web/internship/release/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
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

                if (collegeId > 0) {
                    modelMap.addAttribute("collegeId", collegeId);
                    page = "web/internship/release/internship_release_add::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到院信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            modelMap.addAttribute("collegeId", 0);
            page = "web/internship/release/internship_release_add::#page-wrapper";
        }
        return page;
    }

    /**
     * 实习发布编辑
     *
     * @param id       实习发布id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/internship/release/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.canOperator(id)) {
            Optional<Record> record = internshipReleaseService.findByIdRelation(id);
            if (record.isPresent()) {
                InternshipReleaseBean bean = record.get().into(InternshipReleaseBean.class);
                boolean isTimeLimit = BooleanUtil.toBoolean(bean.getIsTimeLimit());
                if (isTimeLimit) {
                    bean.setTeacherDistributionStartTimeStr(DateTimeUtil.formatLocalDateTime(bean.getTeacherDistributionStartTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    bean.setTeacherDistributionEndTimeStr(DateTimeUtil.formatLocalDateTime(bean.getTeacherDistributionEndTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    bean.setStartTimeStr(DateTimeUtil.formatLocalDateTime(bean.getStartTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                    bean.setEndTimeStr(DateTimeUtil.formatLocalDateTime(bean.getEndTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
                }
                modelMap.addAttribute("internshipRelease", bean);
                Users users = SessionUtil.getUserFromSession();
                if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
                    modelMap.addAttribute("collegeId", bean.getCollegeId());
                } else {
                    modelMap.addAttribute("collegeId", 0);
                }

                page = "web/internship/release/internship_release_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到实习发布数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
