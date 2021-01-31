package top.zbeboy.zone.web.educational.calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SchoolCalendar;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.educational.calendar.EducationalCalendarService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class CalendarViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private EducationalCalendarService educationalCalendarService;

    /**
     * 校历
     *
     * @return 校历页面
     */
    @GetMapping("/web/menu/educational/calendar")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        int schoolId = 0;
        int collegeId = 0;
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                    schoolId = bean.getSchoolId();
                    collegeId = bean.getCollegeId();
                }
            } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                    schoolId = studentBean.getSchoolId();
                    collegeId = studentBean.getCollegeId();
                }
            }
        }

        modelMap.addAttribute("schoolId", schoolId);
        modelMap.addAttribute("collegeId", collegeId);
        modelMap.addAttribute("canRelease", educationalCalendarService.canRelease(users.getUsername()));
        return "web/educational/calendar/calendar_look::#page-wrapper";
    }

    /**
     * 数据管理
     *
     * @return 数据页面
     */
    @GetMapping("/web/educational/calendar/list")
    public String list(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalCalendarService.canRelease(users.getUsername())) {
            page = "web/educational/calendar/calendar_data::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 添加页面
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/educational/calendar/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalCalendarService.canRelease(users.getUsername())) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
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
                page = "web/educational/calendar/calendar_add::#page-wrapper";
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
    @GetMapping("/web/educational/calendar/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalCalendarService.canOperator(users.getUsername(), id)) {
            Optional<SchoolCalendar> optionalSchoolCalendar = educationalCalendarService.findById(id);
            if(optionalSchoolCalendar.isPresent()){
                modelMap.addAttribute("schoolCalendar", optionalSchoolCalendar.get());
                page = "web/educational/calendar/calendar_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到数据");
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
    @GetMapping("/web/educational/calendar/authorize/add")
    public String authorizeAdd(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalCalendarService.canAuthorize(users.getUsername())) {
            page = "web/educational/calendar/calendar_authorize::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
