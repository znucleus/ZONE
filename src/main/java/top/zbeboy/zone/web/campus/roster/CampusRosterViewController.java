package top.zbeboy.zone.web.campus.roster;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.campus.roster.RosterDataBean;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.RosterRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.PinYinUtil;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class CampusRosterViewController {

    @Resource
    private RosterReleaseService rosterReleaseService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    /**
     * 校园花名册
     *
     * @return 校园花名册页面
     */
    @GetMapping("/web/menu/campus/roster")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("canRelease", rosterReleaseService.canRelease(users.getUsername()));
        return "web/campus/roster/roster_release::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/campus/roster/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (rosterReleaseService.canRelease(users.getUsername())) {
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }

                modelMap.addAttribute("collegeId", collegeId);
                page = "web/campus/roster/roster_release_add::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到用户类型");
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

    /**
     * 编辑页面
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/campus/roster/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (rosterReleaseService.canOperator(users.getUsername(), id)) {
            RosterRelease rosterRelease = rosterReleaseService.findById(id);
            modelMap.addAttribute("rosterRelease", rosterRelease);
            modelMap.addAttribute("startTimeStr", DateTimeUtil.defaultFormatSqlTimestamp(rosterRelease.getStartTime()));
            modelMap.addAttribute("endTimeStr", DateTimeUtil.defaultFormatSqlTimestamp(rosterRelease.getEndTime()));
            page = "web/campus/roster/roster_release_edit::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 数据添加页面
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/campus/roster/data/add/{id}")
    public String dataAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (rosterReleaseService.canRegister(users.getUsername(), id) &&
                !rosterReleaseService.canDataEdit(users.getUsername(), id)) {
            // 添加
            StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
            if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                studentBean.setNamePinyin(PinYinUtil.changeToUpper(studentBean.getRealName()));
                modelMap.addAttribute("student", studentBean);
                modelMap.addAttribute("rosterReleaseId", id);
                page = "web/campus/roster/roster_date_inside_add::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到学生信息");
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

    /**
     * 数据编辑页面
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/campus/roster/data/edit/{id}")
    public String dataEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (rosterReleaseService.canDataEdit(users.getUsername(), id)) {
            StudentBean studentBean = studentService.findByUsername(users.getUsername());
            if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                RosterDataBean bean = rosterReleaseService.findRosterDataByStudentNumberAndRosterReleaseIdRelation(studentBean.getStudentNumber(), id);
                bean.setBusSection(bean.getBusSection().substring(bean.getBusSection().indexOf("-") + 1));
                modelMap.addAttribute("rosterData", bean);
                page = "web/campus/roster/roster_date_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到学生信息");
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

    /**
     * 数据查看页面
     *
     * @param modelMap 页面对象
     * @return 查看页面
     */
    @GetMapping("/web/campus/roster/data/look/{id}")
    public String dataLook(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (rosterReleaseService.canDataLook(users.getUsername(), id)) {
            StudentBean studentBean = studentService.findByUsername(users.getUsername());
            if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                RosterDataBean bean = rosterReleaseService.findRosterDataByStudentNumberAndRosterReleaseIdRelation(studentBean.getStudentNumber(), id);
                bean.setBusSection(bean.getBusSection().substring(bean.getBusSection().indexOf("-") + 1));
                modelMap.addAttribute("rosterData", bean);
                page = "web/campus/roster/roster_date_look::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到学生信息");
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

    /**
     * 权限分配页面
     *
     * @param modelMap 页面对象
     * @return 权限分配页面
     */
    @GetMapping("/web/campus/roster/authorize/add")
    public String authorizeAdd(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (rosterReleaseService.canAuthorize(users.getUsername())) {
            page = "web/campus/roster/roster_authorize::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
