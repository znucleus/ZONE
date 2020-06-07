package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Course;
import top.zbeboy.zone.feign.data.CourseService;
import top.zbeboy.zone.web.bean.data.course.CourseBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.course.CourseAddVo;
import top.zbeboy.zone.web.vo.data.course.CourseEditVo;
import top.zbeboy.zone.web.vo.data.course.CourseSearchVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CourseHystrixClientFallbackFactory implements CourseService {
    @Override
    public CourseBean findByIdRelation(int id) {
        return new CourseBean();
    }

    @Override
    public List<Course> findByCollegeIdAndCourseIsDel(CourseSearchVo courseSearchVo) {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String courseName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(CourseAddVo courseAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(int courseId, String courseName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(CourseEditVo courseEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> status(String courseIds, Byte isDel) {
        return AjaxUtil.of();
    }
}
