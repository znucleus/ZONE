package top.zbeboy.zone.api.achievement.student;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.achievement.student.StudentAchievementSemesterBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.StudentAchievementNew;
import top.zbeboy.zbase.domain.tables.pojos.StudentAchievementSemester;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.achievement.student.StudentAchievementService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.achievement.student.StudentAchievementHttpClient;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StudentAchievementApiController {

    @Resource
    private StudentAchievementService studentAchievementService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private RoleService roleService;

    /**
     * 查询成绩
     *
     * @param username 用户名
     * @param password 密码
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教务成绩查询", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/achievement/student/query/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("username") String username,
                                                    @RequestParam("password") String password,
                                                    Principal principal,
                                                    HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        try {
            StudentAchievementHttpClient studentAchievementHttpClient = new StudentAchievementHttpClient();
            Map<String, Object> result = studentAchievementHttpClient.eduData(username, password);
            Boolean hasError = (Boolean) result.get("hasError");
            if (!hasError) {
                ajaxUtil = studentAchievementService.batchSave((List<Map<String, Object>>) result.get("data"));
            } else {
                ajaxUtil.fail().msg(result.get("statusCode") + ": " + result.get("reasonPhrase"));
            }
        } catch (Exception e) {
            ajaxUtil.fail().msg("查询失败: 异常: " + e.getMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查询学期数据
     *
     * @param studentNumber 学号
     * @return 数据
     */
    @ApiLoggingRecord(remark = "学期查询", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/achievement/student/query/history/new/semester")
    public ResponseEntity<Map<String, Object>> semester(String studentNumber, Principal principal, HttpServletRequest request) {
        AjaxUtil<StudentAchievementSemesterBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (StringUtils.isBlank(studentNumber)) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (optionalStudentBean.isPresent()) {
                        StudentBean studentBean = optionalStudentBean.get();
                        studentNumber = studentBean.getStudentNumber();
                    }
                }
            }
        } else {
            boolean canQuery = false;
            if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name()) ||
                    roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
                canQuery = true;
            } else {
                Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                if (optionalUsersType.isPresent()) {
                    UsersType usersType = optionalUsersType.get();
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        canQuery = true;
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (optionalStudentBean.isPresent()) {
                            StudentBean studentBean = optionalStudentBean.get();
                            if(StringUtils.equals(studentNumber, studentBean.getStudentNumber())){
                                canQuery = true;
                            }
                        }
                    }
                }
            }

            if (!canQuery) {
                studentNumber = "";
            }
        }

        if (StringUtils.isNotBlank(studentNumber)) {
            List<StudentAchievementSemesterBean> beans = new ArrayList<>();
            Optional<List<StudentAchievementSemesterBean>> optionalList = studentAchievementService.findSemesterByStudentNumber(studentNumber);
            if (optionalList.isPresent()) {
                beans = optionalList.get();
            }
            ajaxUtil.success().msg("获取数据成功").list(beans);
        } else {
            ajaxUtil.fail().msg("您无权限或非本人查询");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 查询成绩数据
     *
     * @param semesterId 学期id
     * @return 数据
     */
    @ApiLoggingRecord(remark = "成绩查询", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/achievement/student/query/history/new/data/{semesterId}")
    public ResponseEntity<Map<String, Object>> data(@PathVariable("semesterId") String semesterId, Principal principal, HttpServletRequest request) {
        AjaxUtil<StudentAchievementNew> ajaxUtil = AjaxUtil.of();
        boolean canQuery = false;
        Users users = SessionUtil.getUserFromOauth(principal);
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            canQuery = true;
        } else {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    canQuery = true;
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (optionalStudentBean.isPresent()) {
                        StudentBean studentBean = optionalStudentBean.get();
                        String studentNumber = studentBean.getStudentNumber();
                        Optional<StudentAchievementSemester> optionalStudentAchievementSemester = studentAchievementService.findSemesterById(semesterId);
                        if (optionalStudentAchievementSemester.isPresent()) {
                            StudentAchievementSemester studentAchievementSemester = optionalStudentAchievementSemester.get();
                            if (StringUtils.equals(studentNumber, studentAchievementSemester.getStudentNumber())) {
                                canQuery = true;
                            }
                        }
                    }
                }
            }
        }

        if (canQuery) {
            List<StudentAchievementNew> beans = new ArrayList<>();
            Optional<List<StudentAchievementNew>> optionalList = studentAchievementService.findAchievementBySemesterId(semesterId);
            if (optionalList.isPresent()) {
                beans = optionalList.get();
            }
            ajaxUtil.success().msg("获取数据成功").list(beans);
        } else {
            ajaxUtil.fail().msg("您无权限或非本人查询");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
