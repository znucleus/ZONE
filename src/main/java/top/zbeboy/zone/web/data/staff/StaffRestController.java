package top.zbeboy.zone.web.data.staff;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jooq.Record;
import org.jooq.Record21;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.StaffRecord;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.*;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;
import top.zbeboy.zone.web.vo.data.staff.StaffEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
import java.util.*;
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

    @Resource
    private RoleService roleService;

    @Resource
    private CollegeRoleService collegeRoleService;

    @Resource
    private RoleApplyService roleApplyService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private UserNotifyService userNotifyService;

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
    @PostMapping("/users/check/staff/number")
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
    @PostMapping("/users/staff/update/school")
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
    @PostMapping("/users/staff/update/info")
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

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/staff/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("username");
        headers.add("staffNumber");
        headers.add("email");
        headers.add("mobile");
        headers.add("idCard");
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("academicTitleName");
        headers.add("post");
        headers.add("sex");
        headers.add("birthday");
        headers.add("nationName");
        headers.add("politicalLandscapeName");
        headers.add("familyResidence");
        headers.add("enabled");
        headers.add("accountNonLocked");
        headers.add("langKey");
        headers.add("joinDate");
        headers.add("operator");

        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record21<String, String, String, String, String, Byte, String, String, String, String, String, String, String, Date, String, String, String, Byte, Byte, String, Date>>
                records = staffService.findAllByPage(dataTablesUtil);
        List<StaffBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(StaffBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(staffService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(staffService.countByCondition(dataTablesUtil));

        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 用户角色数据
     *
     * @param username 用户账号
     * @return 数据
     */
    @PostMapping("/web/data/staff/role/data")
    public ResponseEntity<Map<String, Object>> roleData(@RequestParam("username") String username) {
        AjaxUtil<Role> ajaxUtil = AjaxUtil.of();
        List<Role> roles = new ArrayList<>();
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {

            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                roles.add(roleService.findByRoleEnName(Workbook.authorities.ROLE_ADMIN.name()));
                roles.add(roleService.findByRoleEnName(Workbook.authorities.ROLE_ACTUATOR.name()));
            }

            int collegeId = 0;
            Optional<Record> record = staffService.findByUsernameRelation(username);
            if (record.isPresent()) {
                collegeId = record.get().into(College.class).getCollegeId();
            }

            if (collegeId > 0) {
                Result<Record> records = collegeRoleService.findByCollegeIdRelation(collegeId);
                if (records.isNotEmpty()) {
                    roles.addAll(records.into(Role.class));
                }
            }
        } else {
            int departmentId = 0;
            Optional<Record> record = staffService.findByUsernameRelation(username);
            if (record.isPresent()) {
                departmentId = record.get().into(Department.class).getDepartmentId();
            }

            if (departmentId > 0) {
                Users curUsers = usersService.getUserFromSession();
                Result<Record> records =
                        roleApplyService.findNormalByUsernameAndAuthorizeTypeIdAndDataScopeAndDataIdAndApplyStatus(curUsers.getUsername(), 1, 1, departmentId, ByteUtil.toByte(1));
                if (records.isNotEmpty()) {
                    List<Role> temp = records.into(Role.class);
                    for (Role t : temp) {
                        boolean hasRole = false;
                        for (Role r : roles) {
                            if (StringUtils.equals(t.getRoleId(), r.getRoleId())) {
                                hasRole = true;
                                break;
                            }
                        }

                        if (!hasRole) {
                            roles.add(t);
                        }
                    }
                }
            }
        }

        ajaxUtil.success().list(roles).msg("获取数据成功");

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
    @PostMapping("/web/data/staff/role/save")
    public ResponseEntity<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("roles") String roles,
                                                        HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(roles)) {
            Users users = usersService.findByUsername(username);
            if (Objects.nonNull(users)) {
                if (Objects.nonNull(users.getVerifyMailbox()) && BooleanUtil.toBoolean(users.getVerifyMailbox())) {
                    List<String> roleList = SmallPropsUtil.StringIdsToStringList(roles);
                    // 禁止非系统用户 提升用户权限到系统或管理员级别权限
                    if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) && (roleList.contains(Workbook.authorities.ROLE_SYSTEM.name()) ||
                            roleList.contains(Workbook.authorities.ROLE_ADMIN.name()) || roleList.contains(Workbook.authorities.ROLE_ACTUATOR.name()))) {
                        ajaxUtil.fail().msg("禁止非系统用户角色提升用户权限到系统或管理员级别权限");
                    } else {
                        boolean canOperator = true;
                        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) &&
                                !roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                            int departmentId = 0;
                            Optional<Record> record = staffService.findByUsernameRelation(users.getUsername());
                            if (record.isPresent()) {
                                departmentId = record.get().into(Department.class).getDepartmentId();
                            }
                            if (departmentId > 0) {
                                Users curUsers = usersService.getUserFromSession();
                                Result<Record> records =
                                        roleApplyService.findNormalByUsernameAndAuthorizeTypeIdAndDataScopeAndDataIdAndApplyStatus(curUsers.getUsername(), 1, 1, departmentId, ByteUtil.toByte(1));
                                if (records.isNotEmpty()) {
                                    List<Role> temp = records.into(Role.class);
                                    for (String role : roleList) {
                                        boolean hasRole = false;
                                        for (Role r : temp) {
                                            if (StringUtils.equals(role, r.getRoleEnName())) {
                                                hasRole = true;
                                                break;
                                            }
                                        }

                                        if (!hasRole) {
                                            canOperator = false;
                                            break;
                                        }
                                    }
                                } else {
                                    canOperator = false;
                                }
                            } else {
                                canOperator = false;
                            }
                        }

                        if (canOperator) {
                            authoritiesService.deleteByUsername(username);
                            List<Authorities> authorities = new ArrayList<>();
                            roleList.forEach(role -> authorities.add(new Authorities(username, role)));
                            authoritiesService.batchSave(authorities);

                            String notify = "您的权限已发生变更，请登录查看。";

                            // 检查邮件推送是否被关闭
                            SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                            if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                                systemMailService.sendNotifyMail(users, RequestUtil.getBaseUrl(request), notify);
                            }

                            Users curUsers = usersService.getUserFromSession();
                            UserNotify userNotify = new UserNotify();
                            userNotify.setUserNotifyId(UUIDUtil.getUUID());
                            userNotify.setSendUser(curUsers.getUsername());
                            userNotify.setAcceptUser(users.getUsername());
                            userNotify.setIsSee(BooleanUtil.toByte(false));
                            userNotify.setNotifyType(Workbook.notifyType.info.name());
                            userNotify.setNotifyTitle("权限变更");
                            userNotify.setNotifyContent(notify);
                            userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                            userNotifyService.save(userNotify);

                            ajaxUtil.success().msg("更改用户角色成功");
                        } else {
                            ajaxUtil.fail().msg("更改用户角色失败");
                        }
                    }
                } else {
                    ajaxUtil.fail().msg("该用户未激活邮箱");
                }
            } else {
                ajaxUtil.fail().msg("未查询到该用户信息");
            }
        } else {
            ajaxUtil.fail().msg("用户角色参数异常");
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
    @PostMapping("/web/data/staff/update/enabled")
    public ResponseEntity<Map<String, Object>> updateEnabled(String userIds, Byte enabled) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(userIds)) {
            List<String> ids = SmallPropsUtil.StringIdsToStringList(userIds);
            if (checkRoleApply(ids)) {
                usersService.updateEnabled(ids, enabled);
                ajaxUtil.success().msg("注销用户成功");
            } else {
                ajaxUtil.fail().msg("注销用户失败");
            }
        } else {
            ajaxUtil.fail().msg("用户账号不能为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/web/data/staff/update/locked")
    public ResponseEntity<Map<String, Object>> updateLocked(String userIds, Byte locked) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(userIds)) {
            List<String> ids = SmallPropsUtil.StringIdsToStringList(userIds);
            if (checkRoleApply(ids)) {
                usersService.updateLocked(ids, locked);
                ajaxUtil.success().msg("修改用户锁定状态成功");
            } else {
                ajaxUtil.fail().msg("修改用户锁定状态失败");
            }
        } else {
            ajaxUtil.fail().msg("用户账号不能为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新密码
     *
     * @param username 账号
     * @return success or fail
     */
    @PostMapping("/web/data/staff/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestParam("username") String username) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        boolean canOperator = true;
        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) &&
                !roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            List<String> ids = new ArrayList<>();
            ids.add(username);
            if (checkRoleApply(ids)) {
                String password = RandomUtil.generatePassword();
                usersService.updatePassword(username, BCryptUtil.bCryptPassword(password));
                ajaxUtil.success().msg("更改用户密码成功，新密码为：" + password + "，请牢记或及时更改！");
            } else {
                ajaxUtil.fail().msg("更改用户密码失败");
            }

        } else {
            ajaxUtil.fail().msg("更改用户密码失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/web/data/staff/delete")
    public ResponseEntity<Map<String, Object>> delete(String userIds) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(userIds)) {
            List<String> ids = SmallPropsUtil.StringIdsToStringList(userIds);
            if (checkRoleApply(ids)) {
                usersService.deleteById(ids);
                ajaxUtil.success().msg("删除用户成功");
            } else {
                ajaxUtil.fail().msg("删除用户失败");
            }

        } else {
            ajaxUtil.fail().msg("用户账号不能为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检查权限
     *
     * @param ids 用户账号
     * @return true or false
     */
    private boolean checkRoleApply(List<String> ids) {
        boolean canOperator = true;
        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) &&
                !roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Users curUsers = usersService.getUserFromSession();
            for (String id : ids) {
                int departmentId = 0;
                Optional<Record> record = staffService.findByUsernameRelation(id);
                if (record.isPresent()) {
                    departmentId = record.get().into(Department.class).getDepartmentId();
                }
                if (departmentId > 0) {
                    Result<Record> records =
                            roleApplyService.findNormalByUsernameAndAuthorizeTypeIdAndDataScopeAndDataIdAndApplyStatus(curUsers.getUsername(), 1, 1, departmentId, ByteUtil.toByte(1));
                    if (records.isEmpty()) {
                        canOperator = false;
                        break;
                    }
                } else {
                    canOperator = false;
                    break;
                }
            }
        }

        return canOperator;
    }
}
