package top.zbeboy.zone.web.achievement.student;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.bean.achievement.student.StudentAchievementBean;
import top.zbeboy.zbase.bean.achievement.student.StudentAchievementNewBean;
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
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StudentAchievementRestController {

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
    @ApiLoggingRecord(remark = "教务成绩查询", channel = Workbook.channel.WEB, needLogin = true)
    @PostMapping("/web/achievement/student/query/data")
    public ResponseEntity<Map<String, Object>> data(@RequestParam("username") String username,
                                                    @RequestParam("password") String password,
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
    @ApiLoggingRecord(remark = "学期查询", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/achievement/student/query/history/new/semester")
    public ResponseEntity<Map<String, Object>> semester(String studentNumber, HttpServletRequest request) {
        AjaxUtil<StudentAchievementSemesterBean> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromSession();
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

            if(!canQuery){
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
     * @param simplePaginationUtil 条件
     * @return 数据
     */
    @ApiLoggingRecord(remark = "成绩查询", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/achievement/student/query/history/new/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil, HttpServletRequest request) {
        AjaxUtil<StudentAchievementNewBean> ajaxUtil = AjaxUtil.of();
        boolean canQuery = false;
        Users users = SessionUtil.getUserFromSession();
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
                        simplePaginationUtil.setSearch("studentNumber", studentBean.getStudentNumber());
                        canQuery = true;
                    }
                }
            }
        }

        if (canQuery) {
            List<StudentAchievementNewBean> beans = new ArrayList<>();
            Optional<List<StudentAchievementNewBean>> optionalList = studentAchievementService.findAchievement(simplePaginationUtil);
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
     * 历史查询成绩
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "学生历史成绩查询", channel = Workbook.channel.WEB, needLogin = true)
    @GetMapping("/web/achievement/student/query/history/old/data")
    public ResponseEntity<Map<String, Object>> oldHistoryData(HttpServletRequest request) {
        AjaxUtil<StudentAchievementBean> ajaxUtil = AjaxUtil.of();
        List<StudentAchievementBean> list = new ArrayList<>();
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                if (optionalStudentBean.isPresent()) {
                    StudentBean studentBean = optionalStudentBean.get();
                    Optional<List<StudentAchievementBean>> optionalStudentAchievementBeans = studentAchievementService.findOldByStudentNumber(studentBean.getStudentNumber());
                    if (optionalStudentAchievementBeans.isPresent()) {
                        list = optionalStudentAchievementBeans.get();
                        list.forEach(s -> s.setCreateDateStr(DateTimeUtil.defaultFormatLocalDateTime(s.getCreateDate())));
                    }
                }
            }
        }
        ajaxUtil.success().msg("获取数据成功").list(list);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
