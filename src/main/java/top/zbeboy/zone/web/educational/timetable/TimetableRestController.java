package top.zbeboy.zone.web.educational.timetable;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.domain.tables.records.TimetableCourseRecord;
import top.zbeboy.zbase.domain.tables.records.TimetableSemesterRecord;
import top.zbeboy.zbase.elastic.TimetableElastic;
import top.zbeboy.zbase.feign.city.educational.EducationalTimetableService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.educational.TimetableCourseService;
import top.zbeboy.zone.service.educational.TimetableSemesterService;
import top.zbeboy.zone.service.educational.TimetableService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class TimetableRestController {

    @Resource
    private EducationalTimetableService educationalTimetableService;

    @Resource
    private TimetableService timetableService;

    @Resource
    private TimetableSemesterService timetableSemesterService;

    @Resource
    private TimetableCourseService timetableCourseService;

    @Resource
    private UsersTypeService usersTypeService;

    /**
     * 导入数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表导入", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/import/save")
    public ResponseEntity<Map<String, Object>> timetableImportSave(@RequestParam("username") String username, @RequestParam("password") String password) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromSession();
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    timetableService.syncWithStudent(username, password);
                    ajaxUtil.success().msg("导入成功");
                } else {
                    ajaxUtil.fail().msg("抱歉，暂时仅支持学生用户导入");
                }
            } else {
                ajaxUtil.fail().msg("未查询到用户类型信息");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("导入失败，error: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学年数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表学年", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/school-year")
    public ResponseEntity<Map<String, Object>> schoolYear(HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        Result<TimetableSemesterRecord> timetableSemesterRecords = timetableSemesterService.findAll();
        List<TimetableSemester> timetableSemesters = new ArrayList<>();
        if (timetableSemesterRecords.isNotEmpty()) {
            timetableSemesters = timetableSemesterRecords.into(TimetableSemester.class);
        }
        timetableSemesters.forEach(timetableSemester -> select2Data.add(timetableSemester.getTimetableSemesterId() + "", timetableSemester.getName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 搜索数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表搜索", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/search")
    public ResponseEntity<Map<String, Object>> search(TimetableCourse timetableCourse, HttpServletRequest request) {
        AjaxUtil<TimetableCourse> ajaxUtil = AjaxUtil.of();
        List<TimetableCourse> timetableCourses = new ArrayList<>();
        String courseName = timetableCourse.getCourseName();
        String lessonName = timetableCourse.getLessonName();
        String room = timetableCourse.getRoom();
        String teachers = timetableCourse.getTeachers();
        Integer timetableSemesterId = timetableCourse.getTimetableSemesterId();

        if (Objects.nonNull(timetableSemesterId) &&
                (StringUtils.isNotBlank(courseName) ||
                        StringUtils.isNotBlank(lessonName) ||
                        StringUtils.isNotBlank(room) ||
                        StringUtils.isNotBlank(teachers))) {
            Result<TimetableCourseRecord> timetableCourseRecords = timetableCourseService.search(timetableCourse);
            if (timetableCourseRecords.isNotEmpty()) {
                timetableCourses = timetableCourseRecords.into(TimetableCourse.class);
            }
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableCourses);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教室数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教室", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/room/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> room(@PathVariable("timetableSemesterId") int timetableSemesterId, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableCourse> timetableCourses = timetableCourseService.findByTimetableSemesterIdDistinctRoom(timetableSemesterId);
        timetableCourses.forEach(data -> select2Data.add(data.getRoom(), data.getRoom()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 班级数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表班级", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/lesson-name/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> attendClass(@PathVariable("timetableSemesterId") int timetableSemesterId, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableCourse> timetableCourses = timetableCourseService.findByTimetableSemesterIdDistinctLessonName(timetableSemesterId);
        timetableCourses.forEach(data -> select2Data.add(data.getLessonName(), data.getLessonName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 课程数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表课程", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/course-name/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> courseName(@PathVariable("timetableSemesterId") int timetableSemesterId, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableCourse> timetableCourses = timetableCourseService.findByTimetableSemesterIdDistinctCourseName(timetableSemesterId);
        timetableCourses.forEach(data -> select2Data.add(data.getCourseName(), data.getCourseName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 教师数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教师", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/educational/timetable/teacher-name/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> teacherName(@PathVariable("timetableSemesterId") int timetableSemesterId, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        List<TimetableCourse> timetableCourses = timetableCourseService.findByTimetableSemesterIdDistinctTeachers(timetableSemesterId);
        timetableCourses.forEach(data -> select2Data.add(data.getTeachers(), data.getTeachers()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
