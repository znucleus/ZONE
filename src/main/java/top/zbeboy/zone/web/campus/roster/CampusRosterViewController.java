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
import top.zbeboy.zbase.feign.campus.roster.CampusRosterService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.PinYinUtil;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class CampusRosterViewController {

    @Resource
    private CampusRosterService campusRosterService;

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
        modelMap.addAttribute("canRelease", campusRosterService.canRelease(users.getUsername()));
        return "web/campus/roster/roster_release::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/campus/roster/release/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusRosterService.canRelease(users.getUsername())) {
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
    @GetMapping("/web/campus/roster/release/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusRosterService.canOperator(users.getUsername(), id)) {
            Optional<RosterRelease> optionalRosterRelease = campusRosterService.findById(id);
            if (optionalRosterRelease.isPresent()) {
                RosterRelease rosterRelease = optionalRosterRelease.get();
                modelMap.addAttribute("rosterRelease", rosterRelease);
                modelMap.addAttribute("startTimeStr", DateTimeUtil.defaultFormatLocalDateTime(rosterRelease.getStartTime()));
                modelMap.addAttribute("endTimeStr", DateTimeUtil.defaultFormatLocalDateTime(rosterRelease.getEndTime()));
                page = "web/campus/roster/roster_release_edit::#page-wrapper";
            } else {
                config.buildWarningTip("查询错误", "未查询到数据");
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
        if (campusRosterService.canRegister(users.getUsername(), id) &&
                !campusRosterService.canDataEdit(users.getUsername(), id)) {
            // 添加
            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                studentBean.setNamePinyin(PinYinUtil.changeToUpper(studentBean.getRealName()));
                modelMap.addAttribute("student", studentBean);
                modelMap.addAttribute("rosterReleaseId", id);
                page = "web/campus/roster/roster_data_inside_add::#page-wrapper";
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
     * 数据添加页面
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping(CampusUrlCommon.ANYONE_ROSTER_DATE_ADD_URL + "{id}")
    public String dataOuterAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig();
        Optional<RosterRelease> optionalRosterRelease = campusRosterService.findById(id);
        if (optionalRosterRelease.isPresent()) {
            RosterRelease rosterRelease = optionalRosterRelease.get();
            // 时间范围
            if (DateTimeUtil.nowAfterLocalDateTime(rosterRelease.getStartTime()) &&
                    DateTimeUtil.nowBeforeLocalDateTime(rosterRelease.getEndTime())) {
                modelMap.addAttribute("rosterRelease", rosterRelease);
                modelMap.addAttribute("startTimeStr", DateTimeUtil.defaultFormatLocalDateTime(rosterRelease.getStartTime()));
                modelMap.addAttribute("endTimeStr", DateTimeUtil.defaultFormatLocalDateTime(rosterRelease.getEndTime()));
                return "web/campus/roster/roster_data_outer_add";
            } else {

                config.buildWarningTip(
                        "进入花名册失败。",
                        "不在花名册填写时间范围。");
                config.addLoginButton();
                config.addHomeButton();
                config.dataMerging(modelMap);
            }
        } else {
            config.buildDangerTip(
                    "进入花名册失败。",
                    "未查询到花名册信息。");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(modelMap);
        }
        return "tip";
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
        if (campusRosterService.canDataEdit(users.getUsername(), id)) {
            Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                Optional<RosterDataBean> optionalRosterDataBean = campusRosterService.findRosterDataByStudentNumberAndRosterReleaseIdRelation(optionalStudentBean.get().getStudentNumber(), id);
                if (optionalRosterDataBean.isPresent()) {
                    RosterDataBean rosterDataBean = optionalRosterDataBean.get();
                    rosterDataBean.setBusSection(rosterDataBean.getBusSection().substring(rosterDataBean.getBusSection().indexOf("-") + 1));
                    modelMap.addAttribute("rosterData", rosterDataBean);
                    page = "web/campus/roster/roster_data_edit::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到花名册数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
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
        if (campusRosterService.canDataLook(users.getUsername(), id)) {
            Optional<StudentBean> optionalStudentBean = studentService.findByUsername(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                Optional<RosterDataBean> optionalRosterDataBean = campusRosterService.findRosterDataByStudentNumberAndRosterReleaseIdRelation(optionalStudentBean.get().getStudentNumber(), id);
                if (optionalRosterDataBean.isPresent()) {
                    RosterDataBean rosterDataBean = optionalRosterDataBean.get();
                    rosterDataBean.setBusSection(rosterDataBean.getBusSection().substring(rosterDataBean.getBusSection().indexOf("-") + 1));
                    modelMap.addAttribute("rosterData", rosterDataBean);
                    page = "web/campus/roster/roster_data_look::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到花名册数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
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
        if (campusRosterService.canAuthorize(users.getUsername())) {
            page = "web/campus/roster/roster_authorize::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 统计页面
     *
     * @param modelMap 页面对象
     * @return 统计页面
     */
    @GetMapping("/web/campus/roster/review/list/{id}")
    public String review(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusRosterService.canReview(users.getUsername(), id)) {
            modelMap.addAttribute("rosterReleaseId", id);
            page = "web/campus/roster/roster_review::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 数据保存成功
     *
     * @param modelMap 页面数据
     * @return 成功
     */
    @GetMapping("/anyone/campus/roster/data/save/success")
    public String dataSaveSuccess(ModelMap modelMap) {
        SystemTipConfig config = new SystemTipConfig(
                "花名册数据保存成功",
                "您的数据已保存成功，请尽快注册并登录系统，可在花名册菜单下对您的数据进行修改，感谢您的使用。");
        config.addLoginButton();
        config.addHomeButton();
        config.dataMerging(modelMap);
        return "tip";
    }
}
