package top.zbeboy.zone.web.campus.timetable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.campus.timetable.CampusCourseReleaseService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class CampusTimetableViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private CampusCourseReleaseService campusCourseReleaseService;

    /**
     * 课表
     *
     * @return 课表页面
     */
    @GetMapping("/web/menu/campus/timetable")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        int schoolId = 0;
        int collegeId = 0;
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
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
        return "web/campus/timetable/timetable_look::#page-wrapper";
    }

    /**
     * 新建空白课表
     *
     * @return 新建空白课表
     */
    @GetMapping("/web/campus/timetable/add")
    public String add() {
        return "web/campus/timetable/timetable_release_blank::#page-wrapper";
    }

    /**
     * 导入共享课表
     *
     * @return 导入共享课表
     */
    @GetMapping("/web/campus/timetable/share/add")
    public String shareAdd() {
        return "web/campus/timetable/timetable_release_share::#page-wrapper";
    }

    /**
     * 编辑页面
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/campus/timetable/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusCourseReleaseService.canOperator(users.getUsername(), id)) {
            CampusCourseRelease campusCourseRelease = campusCourseReleaseService.findById(id);
            modelMap.addAttribute("campusCourseRelease", campusCourseRelease);
            page = "web/campus/timetable/timetable_release_edit::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 添加课程
     *
     * @return 添加课程
     */
    @GetMapping("/web/campus/timetable/course/add/{id}")
    public String courseAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusCourseReleaseService.canOperator(users.getUsername(), id)) {
            modelMap.addAttribute("campusCourseReleaseId", id);
            page = "web/campus/timetable/timetable_course_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 编辑课程
     *
     * @return 添加课程
     */
    @GetMapping("/web/campus/timetable/course/edit/{id}")
    public String courseEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        CampusCourseData campusCourseData = campusCourseReleaseService.findCourseById(id);
        if (Objects.nonNull(campusCourseData) && StringUtils.isNotBlank(campusCourseData.getCampusCourseDataId())) {
            Users users = SessionUtil.getUserFromSession();
            if (campusCourseReleaseService.canOperator(users.getUsername(), campusCourseData.getCampusCourseReleaseId())) {
                modelMap.addAttribute("campusCourseData", campusCourseData);
                page = "web/campus/timetable/timetable_course_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到课程信息");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
