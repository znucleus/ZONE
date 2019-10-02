package top.zbeboy.zone.web.data.staff;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
import top.zbeboy.zone.domain.tables.pojos.Staff;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.StaffRecord;
import top.zbeboy.zone.service.data.StaffService;
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
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;
import top.zbeboy.zone.web.vo.data.staff.StaffEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class StaffRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private StaffService staffService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private UsersService usersService;

    /**
     * 检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/staff/number")
    public ResponseEntity<Map<String, Object>> anyoneCheckStaffNumber(@RequestParam("staffNumber") String staffNumber) {
        String param = StringUtils.trim(staffNumber);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = Workbook.STAFF_NUM_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("工号至少1位数字");
        } else {
            Staff staff = staffService.findByStaffNumber(param);
            if (!ObjectUtils.isEmpty(staff)) {
                ajaxUtil.fail().msg("工号已被注册");
            } else {
                ajaxUtil.success().msg("工号未被注册");
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @PostMapping("/user/check/staff/number")
    public ResponseEntity<Map<String, Object>> userCheckStaffNumber(@RequestParam("staffNumber") String staffNumber) {
        String param = StringUtils.trim(staffNumber);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String regex = Workbook.STAFF_NUM_REGEX;
        if (!Pattern.matches(regex, param)) {
            ajaxUtil.fail().msg("工号至少1位数字");
        } else {
            Users users = usersService.getUserFromSession();
            Result<StaffRecord> records = staffService.findByStaffNumberNeUsername(param, users.getUsername());
            if (records.isNotEmpty()) {
                ajaxUtil.fail().msg("工号已被注册");
            } else {
                ajaxUtil.success().msg("工号未被注册");
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教职工注册
     *
     * @param staffAddVo    教职工数据
     * @param bindingResult 检验
     * @return 注册
     */
    @PostMapping("/anyone/data/register/staff")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterStaff(@Valid StaffAddVo staffAddVo, BindingResult bindingResult,
                                                                       HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            // step 1.检验账号
            SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.FORBIDDEN_REGISTER.name());
            String[] forbiddenRegister = systemConfigure.getDataValue().split(",");
            boolean isForbidden = false;
            for (String fr : forbiddenRegister) {
                if (fr.equalsIgnoreCase(staffAddVo.getUsername())) {
                    isForbidden = true;
                    break;
                }
            }
            if (!isForbidden) {
                // step 2.手机号是否已验证
                if (!ObjectUtils.isEmpty(session.getAttribute(staffAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
                    boolean isValid = (boolean) session.getAttribute(staffAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
                    if (isValid) {
                        // step 3.密码是否一致
                        if (StringUtils.equals(staffAddVo.getPassword(), staffAddVo.getOkPassword())) {
                            // step 4.检查邮件推送是否被关闭
                            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                // step 5.注册
                                staffAddVo.setEnabled(BooleanUtil.toByte(true));
                                staffAddVo.setAccountNonExpired(BooleanUtil.toByte(true));
                                staffAddVo.setCredentialsNonExpired(BooleanUtil.toByte(true));
                                staffAddVo.setAccountNonLocked(BooleanUtil.toByte(true));
                                staffAddVo.setUsersTypeId(usersTypeService.findByUsersTypeName(Workbook.STAFF_USERS_TYPE).getUsersTypeId());
                                staffAddVo.setAvatar(Workbook.USERS_AVATAR);
                                DateTime dateTime = DateTime.now();
                                dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                                staffAddVo.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                                staffAddVo.setMailboxVerifyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                                staffAddVo.setLangKey(request.getLocale().toLanguageTag());
                                staffAddVo.setJoinDate(DateTimeUtil.getNowSqlDate());
                                staffService.saveWithUsers(staffAddVo);
                                Users users = new Users();
                                users.setUsername(staffAddVo.getUsername());
                                users.setLangKey(staffAddVo.getLangKey());
                                users.setMailboxVerifyCode(staffAddVo.getMailboxVerifyCode());
                                users.setMailboxVerifyValid(staffAddVo.getMailboxVerifyValid());
                                users.setEmail(staffAddVo.getEmail());
                                users.setRealName(staffAddVo.getRealName());
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
     * 教职工信息更新
     *
     * @param staffEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/user/staff/update/school")
    public ResponseEntity<Map<String, Object>> userStaffUpdateSchool(StaffEditVo staffEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (staffEditVo.getDepartmentId() > 0) {
            Users users = usersService.getUserFromSession();
            Optional<StaffRecord> record = staffService.findByUsername(users.getUsername());
            if (record.isPresent()) {
                Staff staff = record.get().into(Staff.class);
                staff.setDepartmentId(staffEditVo.getDepartmentId());
                staffService.update(staff);
                ajaxUtil.success().msg("更新学校成功");
            } else {
                ajaxUtil.fail().msg("更新学校失败，查询教职工信息为空");
            }
        } else {
            ajaxUtil.fail().msg("班级ID错误");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param staffEditVo   数据
     * @param bindingResult 检验
     * @return 更新信息
     */
    @PostMapping("/user/staff/update/info")
    public ResponseEntity<Map<String, Object>> userStudentUpdateInfo(@Valid StaffEditVo staffEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Users users = usersService.getUserFromSession();
            Optional<StaffRecord> data = staffService.findByUsername(users.getUsername());
            if (data.isPresent()) {
                Staff staff = data.get().into(Staff.class);
                String staffNumber = staffEditVo.getStaffNumber();
                if (StringUtils.isNotBlank(staffNumber)) {
                    String regex = Workbook.STAFF_NUM_REGEX;
                    if (Pattern.matches(regex, staffNumber)) {
                        staff.setStaffNumber(staffNumber);
                    }
                }
                String birthday = staffEditVo.getBirthday();
                if (StringUtils.isNotBlank(birthday)) {
                    staff.setBirthday(DateTimeUtil.defaultParseSqlDate(birthday));
                } else {
                    staff.setBirthday(null);
                }
                staff.setSex(staffEditVo.getSex());
                staff.setFamilyResidence(staffEditVo.getFamilyResidence());
                staff.setPoliticalLandscapeId(staffEditVo.getPoliticalLandscapeId());
                staff.setNationId(staffEditVo.getNationId());
                staff.setPost(staffEditVo.getPost());
                staff.setAcademicTitleId(staffEditVo.getAcademicTitleId());

                staffService.update(staff);
                ajaxUtil.success().msg("更新信息成功");
            } else {
                ajaxUtil.fail().msg("更新信息失败，查询教职工信息为空");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
