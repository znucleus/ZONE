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
import top.zbeboy.zbase.feign.city.educational.EducationalTimetableService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ElasticUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@RestController
public class TimetableApiController {

    @Resource
    private EducationalTimetableService educationalTimetableService;

    /**
     * 同步数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表同步", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/sync")
    public ResponseEntity<Map<String, Object>> sync(Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (SessionUtil.isOauthUserInRole(Workbook.authorities.ROLE_SYSTEM.name(), principal)) {
            ajaxUtil = educationalTimetableService.sync();
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
    @ApiLoggingRecord(remark = "教务课表标识数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/uniques")
    public ResponseEntity<Map<String, Object>> uniques(Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableUniqueElastic> ajaxUtil = AjaxUtil.of();
        // 排序
        Optional<List<TimetableUniqueElastic>> optionalTimetableUniqueElastics = educationalTimetableService.uniques();
        List<TimetableUniqueElastic> list = new ArrayList<>();
        if (optionalTimetableUniqueElastics.isPresent()) {
            list = optionalTimetableUniqueElastics.get();
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
    @ApiLoggingRecord(remark = "教务课表搜索", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/search")
    public ResponseEntity<Map<String, Object>> search(ElasticUtil elasticUtil,
                                                      Principal principal, HttpServletRequest request) {
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
                Optional<List<TimetableElastic>> optionalTimetableElastics = educationalTimetableService.search(elasticUtil);
                if(optionalTimetableElastics.isPresent()){
                    timetableElastics = optionalTimetableElastics.get();
                }
            }
        }
        timetableElastics.forEach(timetableElastic -> timetableElastic.setTeacherNumber(""));
        ajaxUtil.success().msg("获取数据成功").list(timetableElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教室数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教室", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/classroom/{identification}")
    public ResponseEntity<Map<String, Object>> classroom(@PathVariable("identification") String identification,
                                                         Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableClassroomElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableClassroomElastic> timetableClassroomElastics = new ArrayList<>();
        Optional<List<TimetableClassroomElastic>> optionalTimetableClassroomElastics = educationalTimetableService.classrooms(identification);
        if(optionalTimetableClassroomElastics.isPresent()){
            timetableClassroomElastics = optionalTimetableClassroomElastics.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableClassroomElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 班级数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表班级", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/attend-class/{identification}")
    public ResponseEntity<Map<String, Object>> attendClass(@PathVariable("identification") String identification,
                                                           Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableAttendClassElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableAttendClassElastic> timetableAttendClassElastics = new ArrayList<>();
        Optional<List<TimetableAttendClassElastic>> optionalTimetableAttendClassElastics = educationalTimetableService.attendClasses(identification);
        if(optionalTimetableAttendClassElastics.isPresent()){
            timetableAttendClassElastics = optionalTimetableAttendClassElastics.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableAttendClassElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 课程数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表课程", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/course-name/{identification}")
    public ResponseEntity<Map<String, Object>> courseName(@PathVariable("identification") String identification,
                                                          Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableCourseNameElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableCourseNameElastic> timetableCourseNameElastics = new ArrayList<>();
        Optional<List<TimetableCourseNameElastic>> optionalTimetableCourseNameElastics = educationalTimetableService.courseNames(identification);
        if(optionalTimetableCourseNameElastics.isPresent()){
            timetableCourseNameElastics = optionalTimetableCourseNameElastics.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableCourseNameElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教师数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教师", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/teacher-name/{identification}")
    public ResponseEntity<Map<String, Object>> teacherName(@PathVariable("identification") String identification,
                                                           Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableTeacherNameElastic> ajaxUtil = AjaxUtil.of();
        List<TimetableTeacherNameElastic> timetableTeacherNameElastics =  new ArrayList<>();
        Optional<List<TimetableTeacherNameElastic>> optionalTimetableTeacherNameElastics = educationalTimetableService.teacherNames(identification);
        if(optionalTimetableTeacherNameElastics.isPresent()){
            timetableTeacherNameElastics = optionalTimetableTeacherNameElastics.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableTeacherNameElastics);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
