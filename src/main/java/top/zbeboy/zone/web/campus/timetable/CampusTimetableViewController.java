package top.zbeboy.zone.web.campus.timetable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;
import top.zbeboy.zbase.domain.tables.pojos.CampusCourseRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.timetable.CampusTimetableService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zone.web.campus.common.CampusUrlCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class CampusTimetableViewController {

    @Resource
    private CampusTimetableService campusTimetableService;

    /**
     * 课表
     *
     * @return 课表页面
     */
    @GetMapping("/web/menu/campus/timetable")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("weekday", DateTimeUtil.getNowDayOfWeek());
        return "web/campus/timetable/timetable_look::#page-wrapper";
    }

    /**
     * 共享课表页面
     *
     * @param modelMap 页面对象
     * @return 共享课表页面
     */
    @GetMapping(CampusUrlCommon.ANYONE_TIMETABLE_LOOK_URL + "/{id}")
    public String anyoneLook(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("weekday", DateTimeUtil.getNowDayOfWeek());
        return "web/campus/timetable/timetable_outer_look";
    }

    /**
     * 新建空白课表
     *
     * @return 新建空白课表
     */
    @GetMapping("/web/campus/timetable/release/add")
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
        if (campusTimetableService.canOperator(users.getUsername(), id)) {
            Optional<CampusCourseRelease> optionalCampusCourseRelease = campusTimetableService.findById(id);
            if (optionalCampusCourseRelease.isPresent()) {
                modelMap.addAttribute("campusCourseRelease", optionalCampusCourseRelease.get());
                page = "web/campus/timetable/timetable_release_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到课表发布数据");
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
     * 添加课程
     *
     * @return 添加课程
     */
    @GetMapping("/web/campus/timetable/course/add/{id}")
    public String courseAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusTimetableService.canOperator(users.getUsername(), id)) {
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
     * 添加共享课程
     *
     * @return 添加共享课程
     */
    @GetMapping("/web/campus/timetable/course/share/add/{id}")
    public String courseShareAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusTimetableService.canOperator(users.getUsername(), id)) {
            modelMap.addAttribute("campusCourseReleaseId", id);
            page = "web/campus/timetable/timetable_course_share_add::#page-wrapper";
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
        Optional<CampusCourseData> optionalCampusCourseData = campusTimetableService.findCourseById(id);
        if (optionalCampusCourseData.isPresent()) {
            CampusCourseData campusCourseData = optionalCampusCourseData.get();
            Users users = SessionUtil.getUserFromSession();
            if (campusTimetableService.canOperator(users.getUsername(), campusCourseData.getCampusCourseReleaseId())) {
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

    /**
     * 新教务系统数据导入
     *
     * @param id       课表id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/campus/timetable/course/new-edu/add/{id}")
    public String newEduAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (campusTimetableService.canOperator(users.getUsername(), id)) {
            modelMap.addAttribute("campusCourseReleaseId", id);
            page = "web/campus/timetable/timetable_course_new_edu_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
