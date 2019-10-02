package top.zbeboy.zone.web.data.course;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Course;
import top.zbeboy.zone.domain.tables.records.CourseRecord;
import top.zbeboy.zone.service.data.CourseService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.course.CourseOrganizeVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class CourseRestController {

    @Resource
    private CourseService courseService;

    /**
     * 获取全部有效课程
     *
     * @return 课程数据
     */
    @GetMapping("/user/data/course")
    public ResponseEntity<Map<String, Object>> anyoneData(CourseOrganizeVo courseOrganizeVo) {
        Select2Data select2Data = Select2Data.of();
        Result<CourseRecord> records = courseService.findBySchoolYearAndTermAndCollegeIdCourseIsDel(courseOrganizeVo.getSchoolYear(), courseOrganizeVo.getTerm(), courseOrganizeVo.getCollegeId(), BooleanUtil.toByte(false));
        if (records.isNotEmpty()) {
            List<Course> courses = records.into(Course.class);
            courses.forEach(course -> select2Data.add(course.getCourseId().toString(), course.getCourseName()));
        }
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
