package top.zbeboy.zone.api.data.student;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.WeiXinAppBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.config.system.mobile.SystemMobileConfig;
import top.zbeboy.zbase.domain.tables.pojos.RosterData;
import top.zbeboy.zbase.domain.tables.pojos.Student;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.campus.roster.CampusRosterService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.data.WeiXinService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.vo.data.student.StudentAddVo;
import top.zbeboy.zbase.vo.data.student.StudentEditVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.platform.common.PlatformControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class StudentApiController {

    @Resource
    private StudentService studentService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private CampusRosterService campusRosterService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UsersService usersService;

    @Resource
    private WeiXinService weiXinService;

    @Resource
    private PlatformControllerCommon platformControllerCommon;

    /**
     * API:获取学生信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "学生数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/data/student")
    public ResponseEntity<Map<String, Object>> student(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                Map<String, Object> outPut = new HashMap<>();
                outPut.put("studentId", studentBean.getStudentId());
                outPut.put("studentNumber", studentBean.getStudentNumber());
                outPut.put("schoolId", studentBean.getSchoolId());
                outPut.put("schoolName", studentBean.getSchoolName());
                outPut.put("collegeId", studentBean.getCollegeId());
                outPut.put("collegeName", studentBean.getCollegeName());
                outPut.put("collegeAddress", studentBean.getCollegeAddress());
                outPut.put("collegeCoordinate", studentBean.getCollegeCoordinate());
                outPut.put("collegeZipCode", studentBean.getCollegeZipCode());
                outPut.put("departmentId", studentBean.getDepartmentId());
                outPut.put("departmentName", studentBean.getDepartmentName());
                outPut.put("scienceId", studentBean.getScienceId());
                outPut.put("scienceName", studentBean.getScienceName());
                outPut.put("gradeId", studentBean.getGradeId());
                outPut.put("grade", studentBean.getGrade());
                outPut.put("organizeId", studentBean.getOrganizeId());
                outPut.put("organizeName", studentBean.getOrganizeName());
                String clientId = ((OAuth2Authentication) principal).getOAuth2Request().getClientId();
                if (StringUtils.isNotBlank(clientId) && Workbook.advancedApp().contains(clientId)) {
                    outPut.put("nationId", studentBean.getNationId());
                    outPut.put("nationName", studentBean.getNationName());
                    outPut.put("politicalLandscapeId", studentBean.getPoliticalLandscapeId());
                    outPut.put("politicalLandscapeName", studentBean.getPoliticalLandscapeName());
                    outPut.put("birthday", Objects.nonNull(studentBean.getBirthday()) ? DateTimeUtil.defaultFormatLocalDate(studentBean.getBirthday()) : studentBean.getBirthday());
                    outPut.put("sex", studentBean.getSex());
                    outPut.put("familyResidence", studentBean.getFamilyResidence());
                    outPut.put("dormitoryNumber", studentBean.getDormitoryNumber());
                    outPut.put("parentName", studentBean.getParentName());
                    outPut.put("parentContactPhone", studentBean.getParentContactPhone());
                    outPut.put("placeOrigin", studentBean.getPlaceOrigin());
                }
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
    @PostMapping("/api/data/student/check-student-number")
    public ResponseEntity<Map<String, Object>> checkStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<Student> optionalStudent = studentService.findByStudentNumber(studentNumber);
        if (optionalStudent.isPresent()) {
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
            Optional<UsersType> optionalUsersType = usersTypeService.findByUsersTypeName(Workbook.STUDENT_USERS_TYPE);
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                studentAddVo.setEnabled(BooleanUtil.toByte(true));
                studentAddVo.setAccountNonExpired(BooleanUtil.toByte(true));
                studentAddVo.setCredentialsNonExpired(BooleanUtil.toByte(true));
                studentAddVo.setAccountNonLocked(BooleanUtil.toByte(true));
                studentAddVo.setUsersTypeId(usersType.getUsersTypeId());
                studentAddVo.setAvatar(Workbook.USERS_AVATAR);
                DateTime dateTime = DateTime.now();
                dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                studentAddVo.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                studentAddVo.setMailboxVerifyValid(DateTimeUtil.utilDateToLocalDateTime(dateTime.toDate()));
                studentAddVo.setLangKey(request.getLocale().toLanguageTag());
                studentAddVo.setJoinDate(DateTimeUtil.getNowLocalDate());
                studentAddVo.setBaseUrl(RequestUtil.getBaseUrl(request));
                // 同步花名册
                Optional<RosterData> optionalRosterData = campusRosterService.findRosterDataByStudentNumber(studentAddVo.getStudentNumber());
                if (optionalRosterData.isPresent()) {
                    RosterData rosterData = optionalRosterData.get();
                    studentAddVo.setBirthday(rosterData.getBirthday());
                    studentAddVo.setSex(rosterData.getSex());
                    studentAddVo.setPoliticalLandscapeId(rosterData.getPoliticalLandscapeId());
                    studentAddVo.setNationId(rosterData.getNationId());
                    studentAddVo.setDormitoryNumber(rosterData.getDormitoryNumber());
                    studentAddVo.setParentName(rosterData.getParentName());
                    studentAddVo.setParentContactPhone(rosterData.getParentContactPhone());
                }

                ajaxUtil = studentService.save(studentAddVo);

                platformControllerCommon.personalQrCode(studentAddVo.getUsername(), studentAddVo.getAvatar(), studentAddVo.getUsersTypeId(), RequestUtil.getRealPath(request));
                if (ajaxUtil.getState()) {
                    // 注册微信
                    if (StringUtils.isNotBlank(studentAddVo.getResCode()) && StringUtils.isNotBlank(studentAddVo.getAppId())) {
                        weiXinService.save(studentAddVo.getResCode(), studentAddVo.getAppId(), WeiXinAppBook.getAppSecret(studentAddVo.getAppId()), studentAddVo.getUsername());
                    }
                }
            } else {
                ajaxUtil.fail().msg("未查询到用户类型信息");
            }
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生班级更新
     *
     * @param studentEditVo 数据
     * @return 成功与否
     */
    @ApiLoggingRecord(remark = "学生更新学校", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/data/student/update/school")
    public ResponseEntity<Map<String, Object>> userStudentUpdateSchool(StudentEditVo studentEditVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
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
    @ApiLoggingRecord(remark = "学生更新信息", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/data/student/update/info")
    public ResponseEntity<Map<String, Object>> userStudentUpdateInfo(StudentEditVo studentEditVo, Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        studentEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = studentService.userStudentUpdateInfo(studentEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
