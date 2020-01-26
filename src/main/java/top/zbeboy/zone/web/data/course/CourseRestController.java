package top.zbeboy.zone.web.data.course;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Course;
import top.zbeboy.zone.domain.tables.records.CourseRecord;
import top.zbeboy.zone.service.data.CourseService;
import top.zbeboy.zone.web.bean.data.course.CourseBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.course.CourseOrganizeSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Map<String, Object>> usersData(CourseOrganizeSearchVo courseOrganizeSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<CourseRecord> records = courseService.findBySchoolYearAndTermAndCollegeIdAndCourseIsDel(courseOrganizeSearchVo.getSchoolYear(), courseOrganizeSearchVo.getTerm(), courseOrganizeSearchVo.getCollegeId(), BooleanUtil.toByte(false));
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
}
