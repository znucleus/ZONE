package top.zbeboy.zone.api.educational.timetable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.educational.timetable.TimetableSemesterBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;
import top.zbeboy.zbase.domain.tables.pojos.TimetableSemester;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.educational.timetable.EducationalTimetableService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.IPTimeStamp;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.educational.TimetableService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@RestController
public class TimetableApiController {

    private final Logger log = LoggerFactory.getLogger(TimetableApiController.class);

    @Resource
    private TimetableService timetableService;

    @Resource
    private EducationalTimetableService educationalTimetableService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    /**
     * 导入数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表导入", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/import/save")
    public ResponseEntity<Map<String, Object>> timetableImportSave(@RequestParam("username") String username, @RequestParam("password") String password,
                                                                   @RequestParam("schoolYear") int schoolYear, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            Users users = SessionUtil.getUserFromOauth(principal);
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (optionalStudentBean.isPresent()) {
                        StudentBean studentBean = optionalStudentBean.get();
                        if (studentBean.getCollegeId() == 1) {
                            timetableService.syncWithStudent(username, password, studentBean.getCollegeId(), schoolYear);
                            ajaxUtil.success().msg("导入成功");
                        } else {
                            ajaxUtil.fail().msg("目前仅支持昆明理工大学城市学院使用");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到学生信息");
                    }
                } else {
                    ajaxUtil.fail().msg("抱歉，暂时仅支持学生用户导入");
                }
            } else {
                ajaxUtil.fail().msg("未查询到用户类型信息");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("导入失败，error: " + e.getMessage());
            log.error("教务课表导入错误", e);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学年数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "新教务课表学期", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/import/semesters")
    public ResponseEntity<Map<String, Object>> semesters(@RequestParam("username") String username, @RequestParam("password") String password, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            List<Map<String, Object>> semesters = timetableService.semesters(username, password);
            ajaxUtil.success().msg("获取数据成功").list(semesters);
        } catch (Exception e) {
            log.error("教务课表学期查询错误", e);
            ajaxUtil.fail().msg("查询失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学年数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表学年", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/school-year")
    public ResponseEntity<Map<String, Object>> schoolYear(Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableSemesterBean> ajaxUtil = AjaxUtil.of();
        List<TimetableSemesterBean> timetableSemesters = new ArrayList<>();
        Users users = SessionUtil.getUserFromOauth(principal);
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                if (optionalStudentBean.isPresent()) {
                    StudentBean studentBean = optionalStudentBean.get();
                    Optional<List<TimetableSemesterBean>> optionalTimetableSemesterBeans = educationalTimetableService.findSemesterByCollegeId(studentBean.getCollegeId());
                    if (optionalTimetableSemesterBeans.isPresent()) {
                        timetableSemesters = optionalTimetableSemesterBeans.get();
                    }
                }
            } else if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                if (optionalStaffBean.isPresent()) {
                    StaffBean staffBean = optionalStaffBean.get();
                    Optional<List<TimetableSemesterBean>> optionalTimetableSemesterBeans = educationalTimetableService.findSemesterByCollegeId(staffBean.getCollegeId());
                    if (optionalTimetableSemesterBeans.isPresent()) {
                        timetableSemesters = optionalTimetableSemesterBeans.get();
                    }
                }
            } else {
                Optional<List<TimetableSemesterBean>> optionalTimetableSemesterBeans = educationalTimetableService.semesters();
                if (optionalTimetableSemesterBeans.isPresent()) {
                    timetableSemesters = optionalTimetableSemesterBeans.get();
                }
            }
        }
        ajaxUtil.success().msg("获取数据成功").list(timetableSemesters);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学年信息
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表学年", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/school-year-info/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> schoolYearInfo(@PathVariable("timetableSemesterId") String timetableSemesterId, Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Map<String, Object> map = new HashMap<>();
        Optional<TimetableSemester> optionalTimetableSemester = educationalTimetableService.findSemesterById(timetableSemesterId);
        if (optionalTimetableSemester.isPresent()) {
            TimetableSemester timetableSemester = optionalTimetableSemester.get();
            map.put("startDate", timetableSemester.getStartDate());
            map.put("endDate", timetableSemester.getEndDate());
            map.put("totalWeeks", DateTimeUtil.calculationTwoDateDifferWeeks(timetableSemester.getStartDate(), timetableSemester.getEndDate()));
            map.put("week", DateTimeUtil.getNowDayOfWeek());
            if (DateTimeUtil.nowRangeSqlDate(timetableSemester.getStartDate(), timetableSemester.getEndDate())) {
                map.put("curWeeks", DateTimeUtil.calculationTwoDateDifferWeeks(timetableSemester.getStartDate(), DateTimeUtil.getNowSqlDate()));
            } else {
                map.put("curWeeks", "0");
            }
            ajaxUtil.success().msg("获取数据成功").map(map);
        } else {
            ajaxUtil.fail().msg("获取数据失败");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 搜索数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表搜索", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/search")
    public ResponseEntity<Map<String, Object>> search(TimetableCourse timetableCourse, Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableCourse> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().msg("获取数据成功").list(search(timetableCourse));
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教室数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教室", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/room/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> room(@PathVariable("timetableSemesterId") String timetableSemesterId, Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableCourse> ajaxUtil = AjaxUtil.of();
        Optional<List<TimetableCourse>> optionalTimetableCourses = educationalTimetableService.courseRoom(timetableSemesterId);
        List<TimetableCourse> list = new ArrayList<>();
        if (optionalTimetableCourses.isPresent()) {
            list = optionalTimetableCourses.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 班级数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表班级", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/lesson-name/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> lessonName(@PathVariable("timetableSemesterId") String timetableSemesterId, Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableCourse> ajaxUtil = AjaxUtil.of();
        Optional<List<TimetableCourse>> optionalTimetableCourses = educationalTimetableService.courseLessonName(timetableSemesterId);
        List<TimetableCourse> list = new ArrayList<>();
        if (optionalTimetableCourses.isPresent()) {
            list = optionalTimetableCourses.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 课程数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表课程", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/course-name/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> courseName(@PathVariable("timetableSemesterId") String timetableSemesterId, Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableCourse> ajaxUtil = AjaxUtil.of();
        Optional<List<TimetableCourse>> optionalTimetableCourses = educationalTimetableService.courseName(timetableSemesterId);
        List<TimetableCourse> list = new ArrayList<>();
        if (optionalTimetableCourses.isPresent()) {
            list = optionalTimetableCourses.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教师数据
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务课表教师", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/teacher-name/{timetableSemesterId}")
    public ResponseEntity<Map<String, Object>> teacherName(@PathVariable("timetableSemesterId") String timetableSemesterId, Principal principal, HttpServletRequest request) {
        AjaxUtil<TimetableCourse> ajaxUtil = AjaxUtil.of();
        Optional<List<TimetableCourse>> optionalTimetableCourses = educationalTimetableService.courseTeacherName(timetableSemesterId);
        List<TimetableCourse> list = new ArrayList<>();
        if (optionalTimetableCourses.isPresent()) {
            list = optionalTimetableCourses.get();
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出数据为日历格式
     *
     * @param request 请求
     */
    @ApiLoggingRecord(remark = "教务课表导出日历", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/educational/timetable/generate-ics")
    public ResponseEntity<Map<String, Object>> generateIcs(TimetableCourse timetableCourse, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            List<TimetableCourse> timetableCourses = search(timetableCourse);
            if (CollectionUtils.isNotEmpty(timetableCourses)) {
                IPTimeStamp ipTimeStamp = new IPTimeStamp(RequestUtil.getIpAddress(request));
                String filename = ipTimeStamp.getIPTimeRand() + ".ics";
                if (filename.contains(":")) {
                    filename = filename.substring(filename.lastIndexOf(':') + 1);
                }
                String filePath = Workbook.educationalTimetableIcsFilePath() + filename;
                String path = RequestUtil.getRealPath(request) + filePath;
                timetableService.generateIcs(timetableCourses, path);
                ajaxUtil.success().msg("生成成功").put("path", filePath);
            } else {
                ajaxUtil.fail().msg("无数据可生成");
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("生成文件异常，error: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据搜索
     *
     * @param timetableCourse 搜索条件
     * @return 数据
     */
    private List<TimetableCourse> search(TimetableCourse timetableCourse) {
        List<TimetableCourse> timetableCourses = new ArrayList<>();
        String courseName = timetableCourse.getCourseName();
        String lessonName = timetableCourse.getLessonName();
        String room = timetableCourse.getRoom();
        String teachers = timetableCourse.getTeachers();
        String timetableSemesterId = timetableCourse.getTimetableSemesterId();

        if (Objects.nonNull(timetableSemesterId) &&
                (StringUtils.isNotBlank(courseName) ||
                        StringUtils.isNotBlank(lessonName) ||
                        StringUtils.isNotBlank(room) ||
                        StringUtils.isNotBlank(teachers))) {
            Optional<List<TimetableCourse>> optionalTimetableCourses = educationalTimetableService.courseSearch(timetableCourse);
            if (optionalTimetableCourses.isPresent()) {
                timetableCourses = optionalTimetableCourses.get();
            }
        }
        return timetableCourses;
    }
}
