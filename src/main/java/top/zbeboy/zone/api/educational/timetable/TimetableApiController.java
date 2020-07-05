package top.zbeboy.zone.api.educational.timetable;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.elastic.*;
import top.zbeboy.zbase.feign.city.TimetableService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ElasticUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TimetableApiController {

    @Resource
    private TimetableService timetableService;

    /**
     * 同步数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/sync")
    public ResponseEntity<Map<String, Object>> sync(Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (SessionUtil.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal)) {
            ajaxUtil = timetableService.sync();
        } else {
            ajaxUtil.fail().msg("您无权限操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 标识数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/uniques")
    public ResponseEntity<Map<String, Object>> uniques() {
        AjaxUtil<TimetableUniqueElastic> ajaxUtil = AjaxUtil.of();
        // 排序
        List<TimetableUniqueElastic> list = timetableService.uniques();
        if (Objects.nonNull(list) && !list.isEmpty()) {
            list.sort((o1, o2) -> o2.getIdentification().compareTo(o1.getIdentification()));
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 搜索数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/search")
    public ResponseEntity<Map<String, Object>> search(ElasticUtil elasticUtil) {
        AjaxUtil<TimetableElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableElastic> timetableElastics = new ArrayList<>();
        JSONObject search = elasticUtil.getSearch();
        if (Objects.nonNull(search)) {
            String courseName = StringUtils.trim(search.getString("courseName"));
            String attendClass = StringUtils.trim(search.getString("attendClass"));
            String classroom = StringUtils.trim(search.getString("classroom"));
            String teacherName = StringUtils.trim(search.getString("teacherName"));
            String teacherNumber = StringUtils.trim(search.getString("teacherNumber"));
            String identification = StringUtils.trim(search.getString("identification"));

            if (StringUtils.isNotBlank(identification) &&
                    (StringUtils.isNotBlank(courseName) ||
                            StringUtils.isNotBlank(attendClass) ||
                            StringUtils.isNotBlank(classroom) ||
                            StringUtils.isNotBlank(teacherName) ||
                            StringUtils.isNotBlank(teacherNumber))) {
                timetableElastics = timetableService.search(elasticUtil);
            }
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教室数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/classroom/{identification}")
    public ResponseEntity<Map<String, Object>> classroom(@PathVariable("identification") String identification) {
        AjaxUtil<TimetableClassroomElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableClassroomElastic> timetableClassroomElastics = timetableService.classrooms(identification);
        ajaxUtil.success().msg("获取数据成功").list(timetableClassroomElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 班级数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/attend_class/{identification}")
    public ResponseEntity<Map<String, Object>> attendClass(@PathVariable("identification") String identification) {
        AjaxUtil<TimetableAttendClassElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableAttendClassElastic> timetableAttendClassElastics = timetableService.attendClasses(identification);
        ajaxUtil.success().msg("获取数据成功").list(timetableAttendClassElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 课程数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/course_name/{identification}")
    public ResponseEntity<Map<String, Object>> courseName(@PathVariable("identification") String identification) {
        AjaxUtil<TimetableCourseNameElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableCourseNameElastic> timetableCourseNameElastics = timetableService.courseNames(identification);
        ajaxUtil.success().msg("获取数据成功").list(timetableCourseNameElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教师数据
     *
     * @return 数据
     */
    @GetMapping("/api/educational/timetable/teacher_name/{identification}")
    public ResponseEntity<Map<String, Object>> teacherName(@PathVariable("identification") String identification) {
        AjaxUtil<TimetableTeacherNameElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableTeacherNameElastic> timetableTeacherNameElastics = timetableService.teacherNames(identification);
        ajaxUtil.success().msg("获取数据成功").list(timetableTeacherNameElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
