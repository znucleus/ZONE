package top.zbeboy.zone.web.data.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Course;
import top.zbeboy.zbase.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.CourseService;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.course.CourseAddVo;
import top.zbeboy.zbase.vo.data.course.CourseEditVo;
import top.zbeboy.zbase.vo.data.course.CourseSearchVo;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CourseRestController {

    @Resource
    private CourseService courseService;

    @Resource
    private SystemLogService systemLogService;

    /**
     * 获取全部有效课程
     *
     * @return 课程数据
     */
    @GetMapping("/users/data/course")
    public ResponseEntity<Map<String, Object>> usersData(CourseSearchVo courseSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Optional<List<Course>> optionalCourses = courseService.findByCollegeIdAndCourseIsDel(courseSearchVo);
        optionalCourses.ifPresent(courses -> courses.forEach(course -> select2Data.add(course.getCourseId().toString(), course.getCourseName())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/course/paging")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("courseId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("courseName");
        headers.add("courseCredit");
        headers.add("courseHours");
        headers.add("courseType");
        headers.add("courseCode");
        headers.add("schoolYear");
        headers.add("term");
        headers.add("courseIsDel");
        headers.add("courseBrief");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(courseService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验课程名是否重复
     *
     * @param courseName 课程名
     * @param collegeId  院id
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/course/check-add-name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("courseName") String courseName, @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = courseService.checkAddName(courseName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param courseAddVo 课程
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/course/save")
    public ResponseEntity<Map<String, Object>> save(CourseAddVo courseAddVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = courseService.save(courseAddVo);
        Users users = SessionUtil.getUserFromSession();
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                "添加课程[" + courseAddVo.getCourseName() + "]",
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(),
                RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时课程名重复
     *
     * @param courseId   课程id
     * @param courseName 课程名
     * @param collegeId  院id
     * @return true 合格 false 不合格
     */
    @GetMapping("/web/data/course/check-edit-name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("courseId") int courseId,
                                                             @RequestParam("courseName") String courseName,
                                                             @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = courseService.checkEditName(courseId, courseName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param courseEditVo 课程
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/course/update")
    public ResponseEntity<Map<String, Object>> update(CourseEditVo courseEditVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = courseService.update(courseEditVo);
        Users users = SessionUtil.getUserFromSession();
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                "更新课程" + courseEditVo.getCourseId() + "[" + courseEditVo.getCourseName() + "]",
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(), RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改课程状态
     *
     * @param courseIds 课程ids
     * @param isDel     is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/course/status")
    public ResponseEntity<Map<String, Object>> status(String courseIds, Byte isDel, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = courseService.status(courseIds, isDel);
        Users users = SessionUtil.getUserFromSession();
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                "修改课程状态[" + courseIds + "]" + isDel,
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(), RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
