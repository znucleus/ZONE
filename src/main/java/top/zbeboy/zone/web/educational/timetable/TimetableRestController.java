package top.zbeboy.zone.web.educational.timetable;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.elastic.*;
import top.zbeboy.zbase.feign.city.educational.EducationalTimetableService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.ElasticUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class TimetableRestController {

    @Resource
    private EducationalTimetableService educationalTimetableService;

    @Resource
    private RoleService roleService;

    /**
     * 同步数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表同步", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/sync")
    public ResponseEntity<Map<String, Object>> sync(HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
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
    @ApiLoggingRecord(remark = "教务课表标识", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/uniques")
    public ResponseEntity<Map<String, Object>> uniques(HttpServletRequest request) {
        AjaxUtil<TimetableUniqueElastic> ajaxUtil = AjaxUtil.of();
        // 排序
        List<TimetableUniqueElastic> list = new ArrayList<>();
        Optional<List<TimetableUniqueElastic>> optionalTimetableUniqueElastics = educationalTimetableService.uniques();
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
    @ApiLoggingRecord(remark = "教务课表搜索", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/search")
    public ResponseEntity<Map<String, Object>> search(ElasticUtil elasticUtil, HttpServletRequest request) {
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
    @ApiLoggingRecord(remark = "教务课表教室", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/classroom/{identification}")
    public ResponseEntity<Map<String, Object>> classroom(@PathVariable("identification") String identification, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableClassroomElastic> timetableClassroomElastics = new ArrayList<>();
        Optional<List<TimetableClassroomElastic>> optionalTimetableClassroomElastics = educationalTimetableService.classrooms(identification);
        if(optionalTimetableClassroomElastics.isPresent()){
            timetableClassroomElastics = optionalTimetableClassroomElastics.get();
        }
        timetableClassroomElastics.forEach(data -> select2Data.add(data.getClassroom(), data.getClassroom()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 班级数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表班级", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/attend-class/{identification}")
    public ResponseEntity<Map<String, Object>> attendClass(@PathVariable("identification") String identification, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableAttendClassElastic> timetableAttendClassElastics = new ArrayList<>();
        Optional<List<TimetableAttendClassElastic>> optionalTimetableAttendClassElastics = educationalTimetableService.attendClasses(identification);
        if(optionalTimetableAttendClassElastics.isPresent()){
            timetableAttendClassElastics = optionalTimetableAttendClassElastics.get();
        }
        timetableAttendClassElastics.forEach(data -> select2Data.add(data.getAttendClass(), data.getAttendClass()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 课程数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表课程", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/course-name/{identification}")
    public ResponseEntity<Map<String, Object>> courseName(@PathVariable("identification") String identification, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableCourseNameElastic> timetableCourseNameElastics = new ArrayList<>();
        Optional<List<TimetableCourseNameElastic>> optionalTimetableCourseNameElastics = educationalTimetableService.courseNames(identification);
        if(optionalTimetableCourseNameElastics.isPresent()){
            timetableCourseNameElastics = optionalTimetableCourseNameElastics.get();
        }
        timetableCourseNameElastics.forEach(data -> select2Data.add(data.getCourseName(), data.getCourseName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 教师数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教师", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/teacher-name/{identification}")
    public ResponseEntity<Map<String, Object>> teacherName(@PathVariable("identification") String identification, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableTeacherNameElastic> timetableTeacherNameElastics = new ArrayList<>();
        Optional<List<TimetableTeacherNameElastic>> optionalTimetableTeacherNameElastics = educationalTimetableService.teacherNames(identification);
        if(optionalTimetableTeacherNameElastics.isPresent()){
            timetableTeacherNameElastics = optionalTimetableTeacherNameElastics.get();
        }
        timetableTeacherNameElastics.forEach(data -> select2Data.add(data.getTeacherName(), data.getTeacherName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
