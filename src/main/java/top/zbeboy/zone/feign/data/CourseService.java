package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.Course;
import top.zbeboy.zone.hystrix.data.CourseHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.course.CourseBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.course.CourseAddVo;
import top.zbeboy.zbase.vo.data.course.CourseEditVo;
import top.zbeboy.zbase.vo.data.course.CourseSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = CourseHystrixClientFallbackFactory.class)
public interface CourseService {

    /**
     * 获取课程
     *
     * @param id 课程主键
     * @return 课程数据
     */
    @GetMapping("/base/data/course_relation/{id}")
    CourseBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取全部有效课程
     *
     * @return 课程数据
     */
    @PostMapping("/base/data/courses/search")
    List<Course> findByCollegeIdAndCourseIsDel(@RequestBody CourseSearchVo courseSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/courses/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验课程名是否重复
     *
     * @param courseName 课程名
     * @param collegeId  院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/course/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("courseName") String courseName, @RequestParam(value = "collegeId") int collegeId);

    /**
     * 保存
     *
     * @param courseAddVo 课程
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/course/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody CourseAddVo courseAddVo);

    /**
     * 检验编辑时课程名重复
     *
     * @param courseId   课程id
     * @param courseName 课程名
     * @param collegeId  院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/course/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("courseId") int courseId, @RequestParam("courseName") String courseName, @RequestParam("collegeId") int collegeId);

    /**
     * 更新
     *
     * @param courseEditVo 课程
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/course/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody CourseEditVo courseEditVo);

    /**
     * 批量更改课程状态
     *
     * @param courseIds 课程ids
     * @param isDel     is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/courses/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "courseIds", required = false) String courseIds, @RequestParam("isDel") Byte isDel);
}
