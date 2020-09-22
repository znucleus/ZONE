package top.zbeboy.zone.web.data.student;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.student.StudentAddVo;
import top.zbeboy.zbase.vo.data.student.StudentEditVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class StudentRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private StudentService studentService;

    @Resource
    private UsersService usersService;

    @Resource
    private RosterReleaseService rosterReleaseService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private SystemLogService systemLogService;

    /**
     * 检验学号是否被注册
     *
     * @param studentNumber 学号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/student/number")
    public ResponseEntity<Map<String, Object>> anyoneCheckStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.anyoneCheckStudentNumber(studentNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验学号是否被注册
     *
     * @param studentNumber 学号
     * @return 是否被注册
     */
    @PostMapping("/users/check/student/number")
    public ResponseEntity<Map<String, Object>> userCheckStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.userCheckStudentNumber(users.getUsername(), studentNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据学号检验是否存在以及该用户状态是否正常
     *
     * @param studentNumber 学号
     * @return 是否存在以及该用户状态是否正常
     */
    @PostMapping("/users/check/student/status")
    public ResponseEntity<Map<String, Object>> userCheckStatusByStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.userCheckStatusByStudentNumber(studentNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生注册
     *
     * @param studentAddVo 学生数据
     * @return 注册
     */
    @PostMapping("/anyone/data/register/student")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterStudent(StudentAddVo studentAddVo, HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // step 2.手机号是否已验证
        if (!ObjectUtils.isEmpty(session.getAttribute(studentAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
            boolean isValid = (boolean) session.getAttribute(studentAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
            if (isValid) {
                studentAddVo.setEnabled(BooleanUtil.toByte(true));
                studentAddVo.setAccountNonExpired(BooleanUtil.toByte(true));
                studentAddVo.setCredentialsNonExpired(BooleanUtil.toByte(true));
                studentAddVo.setAccountNonLocked(BooleanUtil.toByte(true));
                studentAddVo.setUsersTypeId(usersTypeService.findByUsersTypeName(Workbook.STUDENT_USERS_TYPE).getUsersTypeId());
                studentAddVo.setAvatar(Workbook.USERS_AVATAR);
                DateTime dateTime = DateTime.now();
                dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                studentAddVo.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                studentAddVo.setMailboxVerifyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                studentAddVo.setLangKey(request.getLocale().toLanguageTag());
                studentAddVo.setJoinDate(DateTimeUtil.getNowSqlDate());

                // 同步花名册
                RosterData rosterData = rosterReleaseService.findRosterDataByStudentNumber(studentAddVo.getStudentNumber());
                if (Objects.nonNull(rosterData) && StringUtils.isNotBlank(rosterData.getRosterDataId())) {
                    studentAddVo.setBirthday(rosterData.getBirthday());
                    studentAddVo.setSex(rosterData.getSex());
                    studentAddVo.setPoliticalLandscapeId(rosterData.getPoliticalLandscapeId());
                    studentAddVo.setNationId(rosterData.getNationId());
                    studentAddVo.setDormitoryNumber(rosterData.getDormitoryNumber());
                    studentAddVo.setParentName(rosterData.getParentName());
                    studentAddVo.setParentContactPhone(rosterData.getParentContactPhone());
                }

                ajaxUtil = studentService.save(studentAddVo);
                if (ajaxUtil.getState()) {
                    Users users = new Users();
                    users.setUsername(studentAddVo.getUsername());
                    users.setLangKey(studentAddVo.getLangKey());
                    users.setMailboxVerifyCode(studentAddVo.getMailboxVerifyCode());
                    users.setMailboxVerifyValid(studentAddVo.getMailboxVerifyValid());
                    users.setEmail(studentAddVo.getEmail());
                    users.setRealName(studentAddVo.getRealName());
                    systemMailService.sendValidEmailMail(users, RequestUtil.getBaseUrl(request));
                }
            } else {
                ajaxUtil.fail().msg("验证手机号失败");
            }
        } else {
            ajaxUtil.fail().msg("请重新验证手机号");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生班级更新
     *
     * @param studentEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/users/student/update/school")
    public ResponseEntity<Map<String, Object>> userStudentUpdateSchool(StudentEditVo studentEditVo) {
        Users users = SessionUtil.getUserFromSession();
        studentEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.userStudentUpdateSchool(studentEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param studentEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/users/student/update/info")
    public ResponseEntity<Map<String, Object>> userStudentUpdateInfo(StudentEditVo studentEditVo) {
        Users users = SessionUtil.getUserFromSession();
        studentEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.userStudentUpdateInfo(studentEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/student/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("username");
        headers.add("studentNumber");
        headers.add("email");
        headers.add("mobile");
        headers.add("idCard");
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("scienceName");
        headers.add("grade");
        headers.add("organizeName");
        headers.add("sex");
        headers.add("birthday");
        headers.add("nationName");
        headers.add("politicalLandscapeName");
        headers.add("dormitoryNumber");
        headers.add("placeOrigin");
        headers.add("parentName");
        headers.add("parentContactPhone");
        headers.add("familyResidence");
        headers.add("enabled");
        headers.add("accountNonLocked");
        headers.add("langKey");
        headers.add("joinDate");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(studentService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 用户角色数据
     *
     * @param username 用户账号
     * @return 数据
     */
    @PostMapping("/web/data/student/role/data")
    public ResponseEntity<Map<String, Object>> roleData(@RequestParam("username") String username) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Role> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(studentService.roleData(users.getUsername(), username)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @param request  请求
     * @return success or false
     */
    @PostMapping("/web/data/student/role/save")
    public ResponseEntity<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("roles") String roles,
                                                        HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.roleSave(users.getUsername(), username, roles);

        if (ajaxUtil.getState()) {
            String notify = "您的权限已发生变更，请登录查看。";

            // 检查邮件推送是否被关闭
            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                systemMailService.sendNotifyMail(usersService.findByUsername(username), RequestUtil.getBaseUrl(request), notify);
            }

            SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                    users.getUsername() + "更改学生: " + username + " 角色为[" + roles + "]",
                    DateTimeUtil.getNowSqlTimestamp(), users.getUsername(),
                    RequestUtil.getIpAddress(request));
            systemLogService.save(systemLog);
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新状态
     *
     * @param userIds 账号
     * @param enabled 状态
     * @return 是否成功
     */
    @PostMapping("/web/data/student/update/enabled")
    public ResponseEntity<Map<String, Object>> updateEnabled(String userIds, Byte enabled, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.updateEnabled(users.getUsername(), userIds, enabled);
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                users.getUsername() + "更改学生: " + userIds + " 状态为[" + enabled + "]",
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(),
                RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/web/data/student/update/locked")
    public ResponseEntity<Map<String, Object>> updateLocked(String userIds, Byte locked, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.updateLocked(users.getUsername(), userIds, locked);
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                users.getUsername() + "更改学生: " + userIds + " 锁定为[" + locked + "]",
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(),
                RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新密码
     *
     * @param username 账号
     * @return success or fail
     */
    @PostMapping("/web/data/student/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestParam("username") String username, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.updatePassword(users.getUsername(), username);
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                users.getUsername() + "更改学生: " + username + " 密码",
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(),
                RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/web/data/student/delete")
    public ResponseEntity<Map<String, Object>> delete(String userIds, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.delete(users.getUsername(), userIds);
        SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(),
                users.getUsername() + "删除学生: " + userIds,
                DateTimeUtil.getNowSqlTimestamp(), users.getUsername(),
                RequestUtil.getIpAddress(request));
        systemLogService.save(systemLog);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
