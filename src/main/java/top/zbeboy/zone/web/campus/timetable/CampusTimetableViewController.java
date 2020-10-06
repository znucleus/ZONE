package top.zbeboy.zone.web.campus.timetable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.domain.tables.pojos.RosterRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.campus.timetable.CampusCourseReleaseService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
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
}