package top.zbeboy.zone.web.data.course;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Course;
import top.zbeboy.zone.domain.tables.records.CourseRecord;
import top.zbeboy.zone.service.data.CourseService;
import top.zbeboy.zone.web.bean.data.course.CourseBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.course.CourseAddVo;
import top.zbeboy.zone.web.vo.data.course.CourseEditVo;
import top.zbeboy.zone.web.vo.data.course.CourseSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class CourseRestController {

    @Resource
    private CourseService courseService;

    /**
     * 获取全部有效课程
     *
     * @return 课程数据
     */
    @GetMapping("/users/data/course")
    public ResponseEntity<Map<String, Object>> usersData(CourseSearchVo courseSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<CourseRecord> records = courseService.findBySchoolYearAndTermAndCollegeIdAndCourseIsDel(courseSearchVo.getSchoolYear(), courseSearchVo.getTerm(), courseSearchVo.getCollegeId(), BooleanUtil.toByte(false));
        if (records.isNotEmpty()) {
            List<Course> courses = records.into(Course.class);
            courses.forEach(course -> select2Data.add(course.getCourseId().toString(), course.getCourseName()));
        }
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/course/data")
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
        Result<Record> records = courseService.findAllByPage(dataTablesUtil);
        List<CourseBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(CourseBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(courseService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(courseService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验课程名是否重复
     *
     * @param courseName 课程名
     * @param collegeId  院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/course/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("courseName") String courseName, @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(courseName);
        Result<CourseRecord> records = courseService.findByCourseNameAndCollegeId(param, collegeId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("课程名不重复");
        } else {
            ajaxUtil.fail().msg("课程名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param courseAddVo   课程
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/course/save")
    public ResponseEntity<Map<String, Object>> save(@Valid CourseAddVo courseAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Course course = new Course();
            course.setCourseIsDel(ByteUtil.toByte(1).equals(courseAddVo.getCourseIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            course.setCourseName(courseAddVo.getCourseName());
            course.setCollegeId(courseAddVo.getCollegeId());
            course.setCourseCredit(courseAddVo.getCourseCredit());
            course.setCourseHours(courseAddVo.getCourseHours());
            course.setCourseType(courseAddVo.getCourseType());
            course.setSchoolYear(courseAddVo.getSchoolYear());
            course.setTerm(courseAddVo.getTerm());
            course.setCourseCode(courseAddVo.getCourseCode());
            course.setCourseBrief(courseAddVo.getCourseBrief());
            courseService.save(course);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
    @PostMapping("/web/data/course/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("courseId") int courseId,
                                                             @RequestParam("courseName") String courseName,
                                                             @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(courseName);
        Result<CourseRecord> records = courseService.findByCourseNameAndCollegeIdNeCourseId(param, collegeId, courseId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("课程名不重复");
        } else {
            ajaxUtil.fail().msg("课程名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param courseEditVo  课程
     * @param bindingResult 检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/course/update")
    public ResponseEntity<Map<String, Object>> update(@Valid CourseEditVo courseEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Course course = courseService.findById(courseEditVo.getCourseId());
            if (Objects.nonNull(course)) {
                course.setCourseIsDel(ByteUtil.toByte(1).equals(courseEditVo.getCourseIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                course.setCourseName(courseEditVo.getCourseName());
                course.setCollegeId(courseEditVo.getCollegeId());
                course.setCourseCredit(courseEditVo.getCourseCredit());
                course.setCourseHours(courseEditVo.getCourseHours());
                course.setCourseType(courseEditVo.getCourseType());
                course.setSchoolYear(courseEditVo.getSchoolYear());
                course.setTerm(courseEditVo.getTerm());
                course.setCourseCode(courseEditVo.getCourseCode());
                course.setCourseBrief(courseEditVo.getCourseBrief());
                courseService.update(course);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据课程ID未查询到课程数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
    public ResponseEntity<Map<String, Object>> status(String courseIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(courseIds)) {
            courseService.updateIsDel(SmallPropsUtil.StringIdsToNumberList(courseIds), isDel);
            ajaxUtil.success().msg("更新状态成功");
        } else {
            ajaxUtil.fail().msg("请选择课程");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
