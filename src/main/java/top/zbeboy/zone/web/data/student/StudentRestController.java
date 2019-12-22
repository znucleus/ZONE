package top.zbeboy.zone.web.data.student;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RandomUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.student.StudentAddVo;
import top.zbeboy.zone.web.vo.data.student.StudentEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class StudentRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private StudentService studentService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private UsersService usersService;

    /**
     * 检验学号是否被注册
     *
     * @param studentNumber 学号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/student/number")
    public ResponseEntity<Map<String, Object>> anyoneCheckStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        String param = StringUtils.trim(studentNumber);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = Workbook.STUDENT_NUM_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("学号13位数字");
        } else {
            Student student = studentService.findByStudentNumber(param);
            if (!ObjectUtils.isEmpty(student)) {
                ajaxUtil.fail().msg("学号已被注册");
            } else {
                ajaxUtil.success().msg("学号未被注册");
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验学号是否被注册
     *
     * @param studentNumber 学号
     * @return 是否被注册
     */
    @PostMapping("/user/check/student/number")
    public ResponseEntity<Map<String, Object>> userCheckStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        String param = StringUtils.trim(studentNumber);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = Workbook.STUDENT_NUM_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("学号13位数字");
        } else {
            Users users = usersService.getUserFromSession();
            Result<StudentRecord> records = studentService.findByStudentNumberNeUsername(param, users.getUsername());
            if (records.isNotEmpty()) {
                ajaxUtil.fail().msg("学号已被注册");
            } else {
                ajaxUtil.success().msg("学号未被注册");
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 根据学号检验是否存在以及该用户状态是否正常
     *
     * @param studentNumber 学号
     * @return 是否存在以及该用户状态是否正常
     */
    @PostMapping("/user/check/student/status")
    public ResponseEntity<Map<String, Object>> userCheckStatusByStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        String param = StringUtils.trim(studentNumber);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = Workbook.STUDENT_NUM_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("学号13位数字");
        } else {
            Optional<Record> record = studentService.findNormalByStudentNumberRelation(studentNumber);
            if (record.isPresent()) {
                ajaxUtil.success().msg("学生信息正常");
            } else {
                ajaxUtil.fail().msg("未查询到该学生信息或该学生状态异常");
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生注册
     *
     * @param studentAddVo  学生数据
     * @param bindingResult 检验
     * @return 注册
     */
    @PostMapping("/anyone/data/register/student")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterStudent(@Valid StudentAddVo studentAddVo, BindingResult bindingResult,
                                                                         HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            // step 1.检验账号
            SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.FORBIDDEN_REGISTER.name());
            String[] forbiddenRegister = systemConfigure.getDataValue().split(",");
            boolean isForbidden = false;
            for (String fr : forbiddenRegister) {
                if (fr.equalsIgnoreCase(studentAddVo.getUsername())) {
                    isForbidden = true;
                    break;
                }
            }
            if (!isForbidden) {
                // step 2.手机号是否已验证
                if (!ObjectUtils.isEmpty(session.getAttribute(studentAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
                    boolean isValid = (boolean) session.getAttribute(studentAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
                    if (isValid) {
                        // step 3.密码是否一致
                        if (StringUtils.equals(studentAddVo.getPassword(), studentAddVo.getOkPassword())) {
                            // step 4.检查邮件推送是否被关闭
                            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                // step 5.注册
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
                                studentService.saveWithUsers(studentAddVo);
                                Users users = new Users();
                                users.setUsername(studentAddVo.getUsername());
                                users.setLangKey(studentAddVo.getLangKey());
                                users.setMailboxVerifyCode(studentAddVo.getMailboxVerifyCode());
                                users.setMailboxVerifyValid(studentAddVo.getMailboxVerifyValid());
                                users.setEmail(studentAddVo.getEmail());
                                users.setRealName(studentAddVo.getRealName());
                                systemMailService.sendValidEmailMail(users, RequestUtil.getBaseUrl(request));
                                ajaxUtil.success().msg("恭喜您注册成功，稍后请前往您的邮箱进行邮箱验证。");
                            } else {
                                ajaxUtil.fail().msg("邮件推送已被管理员关闭");
                            }
                        } else {
                            ajaxUtil.fail().msg("请确认密码");
                        }
                    } else {
                        ajaxUtil.fail().msg("验证手机号失败");
                    }
                } else {
                    ajaxUtil.fail().msg("请重新验证手机号");
                }
            } else {
                ajaxUtil.fail().msg("账号已被注册");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 学生班级更新
     *
     * @param studentEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/user/student/update/school")
    public ResponseEntity<Map<String, Object>> userStudentUpdateSchool(StudentEditVo studentEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (studentEditVo.getOrganizeId() > 0) {
            Users users = usersService.getUserFromSession();
            Optional<StudentRecord> record = studentService.findByUsername(users.getUsername());
            if (record.isPresent()) {
                Student student = record.get().into(Student.class);
                student.setOrganizeId(studentEditVo.getOrganizeId());
                studentService.update(student);
                ajaxUtil.success().msg("更新学校成功");
            } else {
                ajaxUtil.fail().msg("更新学校失败，查询学生信息为空");
            }
        } else {
            ajaxUtil.fail().msg("班级ID错误");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param studentEditVo 数据
     * @param bindingResult 检验
     * @return 更新信息
     */
    @PostMapping("/user/student/update/info")
    public ResponseEntity<Map<String, Object>> userStudentUpdateInfo(@Valid StudentEditVo studentEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = usersService.getUserFromSession();
            Optional<StudentRecord> data = studentService.findByUsername(users.getUsername());
            if (data.isPresent()) {
                Student student = data.get().into(Student.class);
                String studentNumber = studentEditVo.getStudentNumber();
                if (StringUtils.isNotBlank(studentNumber)) {
                    String regex = Workbook.STUDENT_NUM_REGEX;
                    if (Pattern.matches(regex, studentNumber)) {
                        student.setStudentNumber(studentNumber);
                    }
                }
                String birthday = studentEditVo.getBirthday();
                if (StringUtils.isNotBlank(birthday)) {
                    student.setBirthday(DateTimeUtil.defaultParseSqlDate(birthday));
                } else {
                    student.setBirthday(null);
                }
                student.setSex(studentEditVo.getSex());
                student.setFamilyResidence(studentEditVo.getFamilyResidence());
                student.setPoliticalLandscapeId(studentEditVo.getPoliticalLandscapeId());
                student.setNationId(studentEditVo.getNationId());
                String dormitoryNumber = studentEditVo.getDormitoryNumber();
                if (StringUtils.isNotBlank(dormitoryNumber)) {
                    String regex = Workbook.DORMITORY_NUMBER_REGEX;
                    if (Pattern.matches(regex, dormitoryNumber)) {
                        student.setDormitoryNumber(dormitoryNumber);
                    }
                } else {
                    student.setDormitoryNumber(dormitoryNumber);
                }
                String parentContactPhone = studentEditVo.getParentContactPhone();
                if (StringUtils.isNotBlank(parentContactPhone)) {
                    String regex = SystemMobileConfig.MOBILE_REGEX;
                    if (Pattern.matches(regex, parentContactPhone)) {
                        student.setParentContactPhone(parentContactPhone);
                    }
                } else {
                    student.setParentContactPhone(parentContactPhone);
                }
                student.setParentName(studentEditVo.getParentName());
                student.setPlaceOrigin(studentEditVo.getPlaceOrigin());

                studentService.update(student);
                ajaxUtil.success().msg("更新信息成功");
            } else {
                ajaxUtil.fail().msg("更新信息失败，查询学生信息为空");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
