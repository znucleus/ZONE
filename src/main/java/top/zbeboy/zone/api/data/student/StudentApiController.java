package top.zbeboy.zone.api.data.student;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.RosterData;
import top.zbeboy.zbase.domain.tables.pojos.Student;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.vo.data.student.StudentAddVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class StudentApiController {

    @Resource
    private StudentService studentService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private RosterReleaseService rosterReleaseService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UsersService usersService;

    /**
     * API:获取学生信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "学生数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/student")
    public ResponseEntity<Map<String, Object>> users(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
            if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                Map<String, Object> outPut = new HashMap<>();
                outPut.put("studentId", studentBean.getStudentId());
                outPut.put("studentNumber", studentBean.getStudentNumber());
                outPut.put("organizeId", studentBean.getOrganizeId());
                outPut.put("organizeName", studentBean.getOrganizeName());
                outPut.put("schoolId", studentBean.getSchoolId());
                outPut.put("realName", studentBean.getRealName());
                ajaxUtil.success().msg("获取用户信息成功").map(outPut);
            } else {
                ajaxUtil.fail().msg("未查询到学生信息");
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验学号是否正常
     *
     * @param studentNumber 学号
     * @return 数据
     */
    @PostMapping("/api/student/check/number")
    public ResponseEntity<Map<String, Object>> update(@RequestParam("studentNumber") String studentNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Student student = studentService.findByStudentNumber(studentNumber);
        if (Objects.nonNull(student.getStudentId()) && student.getStudentId() > 0) {
            ajaxUtil.success().msg("查询正常");
        } else {
            ajaxUtil.fail().msg("根据学号未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生注册
     *
     * @param studentAddVo 学生数据
     * @return 注册
     */
    @PostMapping("/overt/register/student")
    public ResponseEntity<Map<String, Object>> overtDataRegisterStudent(StudentAddVo studentAddVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        boolean canRegister = false;
        // step 1.手机号是否已验证
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        if (stringRedisTemplate.hasKey(studentAddVo.getMobile() + "_" + studentAddVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID)) {
            String validContent = ops.get(studentAddVo.getMobile() + "_" + studentAddVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID);
            boolean isValid = BooleanUtils.toBoolean(validContent);
            if (isValid) {
                // step 2.检验账号
                ajaxUtil = usersService.anyoneCheckUsername(studentAddVo.getUsername());
                if (ajaxUtil.getState()) {
                    // step 3.检验邮箱
                    ajaxUtil = usersService.anyoneCheckEmail(studentAddVo.getEmail());
                    if (ajaxUtil.getState()) {
                        // step 4.检验学号
                        ajaxUtil = studentService.anyoneCheckStudentNumber(studentAddVo.getStudentNumber());
                        if (ajaxUtil.getState()) {
                            // step 5.检验手机号
                            ajaxUtil = usersService.anyoneCheckMobile(studentAddVo.getMobile());
                            if (ajaxUtil.getState()) {
                                // step 6.检验密码是否一致
                                if (studentAddVo.getPassword().equals(studentAddVo.getOkPassword())) {
                                    canRegister = true;
                                } else {
                                    ajaxUtil.fail().msg("密码不一致");
                                }
                            }
                        }
                    }
                }
            } else {
                ajaxUtil.fail().msg("验证码未验证通过");
            }
        } else {
            ajaxUtil.fail().msg("验证码未验证，请先验证");
        }

        if (canRegister) {
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
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    private boolean checkUsername(String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = usersService.anyoneCheckUsername(username);
        return ajaxUtil.getState();
    }
}
