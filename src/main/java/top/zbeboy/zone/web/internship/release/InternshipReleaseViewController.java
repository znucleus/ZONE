package top.zbeboy.zone.web.internship.release;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class InternshipReleaseViewController {

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
    private InternshipReleaseService internshipReleaseService;

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
        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
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
        Optional<Record> record = internshipReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            InternshipReleaseBean bean = record.get().into(InternshipReleaseBean.class);
            bean.setTeacherDistributionStartTimeStr(DateTimeUtil.formatSqlTimestamp(bean.getTeacherDistributionStartTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
            bean.setTeacherDistributionEndTimeStr(DateTimeUtil.formatSqlTimestamp(bean.getTeacherDistributionEndTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
            bean.setStartTimeStr(DateTimeUtil.formatSqlTimestamp(bean.getStartTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
            bean.setEndTimeStr(DateTimeUtil.formatSqlTimestamp(bean.getEndTime(), DateTimeUtil.YEAR_MONTH_DAY_FORMAT));
            modelMap.addAttribute("internshipRelease", bean);
            if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
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

        return page;
    }
}
