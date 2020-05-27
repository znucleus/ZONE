package top.zbeboy.zone.web.platform.authorize;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.feign.data.DepartmentService;
import top.zbeboy.zone.feign.data.ScienceService;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.feign.system.SystemConfigureService;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.authorize.AuthorizeAddVo;
import top.zbeboy.zone.web.vo.platform.authorize.AuthorizeEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class AuthorizeRestController {

    @Resource
    private RoleApplyService roleApplyService;

    @Resource
    private AuthorizeTypeService authorizeTypeService;

    @Resource
    private CollegeRoleService collegeRoleService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private UserNotifyService userNotifyService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScienceService scienceService;

    @Resource
    private GradeService gradeService;

    @Resource
    private OrganizeService organizeService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("realName");
        headers.add("username");
        headers.add("authorizeTypeName");
        headers.add("dataScope");
        headers.add("dataName");
        headers.add("roleName");
        headers.add("duration");
        headers.add("validDate");
        headers.add("expireDate");
        headers.add("applyStatus");
        headers.add("createDate");
        headers.add("reason");
        headers.add("refuse");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = roleApplyService.findAllByPage(dataTablesUtil);
        List<RoleApplyBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(RoleApplyBean.class);
            for (RoleApplyBean b : beans) {
                b.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(b.getValidDate()));
                b.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(b.getExpireDate()));
                b.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(b.getCreateDate()));

                if (b.getDataScope() == 1) {
                    Department department = departmentService.findById(b.getDataId());
                    if (Objects.nonNull(department)) {
                        b.setDataName(department.getDepartmentName());
                    }
                } else if (b.getDataScope() == 2) {
                    Science science = scienceService.findById(b.getDataId());
                    if (Objects.nonNull(science)) {
                        b.setDataName(science.getScienceName());
                    }
                } else if (b.getDataScope() == 3) {
                    Grade grade = gradeService.findById(b.getDataId());
                    if (Objects.nonNull(grade)) {
                        b.setDataName(grade.getGrade() + "");
                    }
                } else if (b.getDataScope() == 4) {
                    Organize organize = organizeService.findById(b.getDataId());
                    if (Objects.nonNull(organize)) {
                        b.setDataName(organize.getOrganizeName());
                    }
                }
            }
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(roleApplyService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(roleApplyService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 根据全部权限类型
     *
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/type")
    public ResponseEntity<Map<String, Object>> authorizeTypeData() {
        Select2Data select2Data = Select2Data.of();
        List<AuthorizeType> all = authorizeTypeService.findAll();
        all.forEach(authorizeType -> select2Data.add(authorizeType.getAuthorizeTypeId().toString(), authorizeType.getAuthorizeTypeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 根据全部权限类型
     *
     * @param collegeId 院id
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/role/{id}")
    public ResponseEntity<Map<String, Object>> roleData(@PathVariable("id") int collegeId) {
        Select2Data select2Data = Select2Data.of();
        if (collegeId > 0) {
            Result<Record> all = collegeRoleService.findByCollegeIdRelation(collegeId);
            if (all.isNotEmpty()) {
                List<Role> roles = all.into(Role.class);
                roles.forEach(role -> select2Data.add(role.getRoleId(), role.getRoleName()));
            }
        }
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 检验账号是否符合规则
     *
     * @param username 账号
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/authorize/check/username")
    public ResponseEntity<Map<String, Object>> checkAddUsername(@RequestParam("username") String username, @RequestParam("collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(username);

        int userCollegeId = 0;
        Users users = usersService.findByUsername(param);
        if (Objects.nonNull(users)) {
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {

                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        userCollegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                    if (record.isPresent()) {
                        userCollegeId = record.get().into(College.class).getCollegeId();
                    }
                }

                if (userCollegeId <= 0) {
                    ajaxUtil.fail().msg("未查询到用户所属院信息");
                }
            } else {
                ajaxUtil.fail().msg("未查询到用户类型");
            }
        } else {
            ajaxUtil.fail().msg("未查询到账号信息");
        }

        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            if (userCollegeId > 0) {
                rule1(ajaxUtil, param, userCollegeId);
            }
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            if (userCollegeId > 0) {
                if (userCollegeId == collegeId) {
                    rule1(ajaxUtil, param, userCollegeId);
                } else {
                    ajaxUtil.fail().msg("该账号不在您所属院下，不允许申请操作");
                }
            }
        } else {
            ajaxUtil.fail().msg("您没有权限进行该操作");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param authorizeAddVo 数据
     * @param bindingResult  检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/authorize/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AuthorizeAddVo authorizeAddVo, BindingResult bindingResult, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (StringUtils.isNotBlank(authorizeAddVo.getUsername())) {
                String param = StringUtils.deleteWhitespace(authorizeAddVo.getUsername());
                Users users = usersService.findByUsername(param);
                if (Objects.nonNull(users)) {
                    UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                    if (Objects.nonNull(usersType)) {
                        int collegeId = 0;
                        if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                            StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                            if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                                collegeId = bean.getCollegeId();
                            }
                        } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                            if(record.isPresent()){
                                collegeId = record.get().into(College.class).getCollegeId();
                            }
                        }

                        if (collegeId > 0) {
                            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                                rule1(ajaxUtil, param, collegeId);
                            } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                                if (collegeId == authorizeAddVo.getCollegeId()) {
                                    rule1(ajaxUtil, param, collegeId);
                                } else {
                                    ajaxUtil.fail().msg("该账号不在您所属院下，不允许申请操作");
                                }
                            } else {
                                ajaxUtil.fail().msg("您没有权限进行该操作");
                            }
                        } else {
                            ajaxUtil.fail().msg("未查询到用户所属院信息");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到用户类型");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到账号信息");
                }
            } else {
                if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                        roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                    ajaxUtil.fail().msg("申请账号不能为空");
                } else {
                    ajaxUtil.success();
                }
            }

            if (ajaxUtil.getState()) {
                RoleApply roleApply = new RoleApply();
                roleApply.setRoleApplyId(UUIDUtil.getUUID());
                roleApply.setAuthorizeTypeId(authorizeAddVo.getAuthorizeTypeId());
                roleApply.setRoleId(authorizeAddVo.getRoleId());
                roleApply.setDuration(getDuration(authorizeAddVo.getDuration()));
                roleApply.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(authorizeAddVo.getValidDate()));
                roleApply.setDataScope(authorizeAddVo.getDataScope());
                roleApply.setDataId(authorizeAddVo.getDataId());
                roleApply.setReason(authorizeAddVo.getReason());
                roleApply.setApplyStatus(ByteUtil.toByte(0));
                roleApply.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                Users applyUser;
                if (StringUtils.isNotBlank(authorizeAddVo.getUsername())) {
                    String param = StringUtils.deleteWhitespace(authorizeAddVo.getUsername());
                    applyUser = usersService.findByUsername(param);
                } else {
                    applyUser = usersService.getUserFromSession();
                }

                roleApply.setUsername(applyUser.getUsername());

                // 计算时长
                roleApply.setExpireDate(DateTimeUtil.utilDateToSqlTimestamp(getDuration(authorizeAddVo.getValidDate(), authorizeAddVo.getDuration())));

                roleApplyService.save(roleApply);

                // 查询该申请人所在院所有院管理员
                List<Users> admins = new ArrayList<>();
                List<Users> staffAdmin = staffService.findByAuthorityAndCollegeId(Workbook.authorities.ROLE_ADMIN.name(), authorizeAddVo.getCollegeId());
                if (Objects.nonNull(staffAdmin) && staffAdmin.size() > 0) {
                    admins.addAll(staffAdmin);
                }

                Result<Record> studentAdmin = studentService.findAdmin(Workbook.authorities.ROLE_ADMIN.name(), authorizeAddVo.getCollegeId());
                if (studentAdmin.isNotEmpty()) {
                    admins.addAll(studentAdmin.into(Users.class));
                }

                String notify = "用户【" + applyUser.getRealName() +
                        "-" + applyUser.getUsername() + "】于" +
                        DateTimeUtil.defaultFormatSqlTimestamp(roleApply.getCreateDate()) +
                        "提交了新的权限申请，请及时到平台授权菜单审核。";
                for (Users u : admins) {
                    // 检查邮件推送是否被关闭
                    SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                    if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                        systemMailService.sendNotifyMail(u, RequestUtil.getBaseUrl(request), notify);
                    }

                    UserNotify userNotify = new UserNotify();
                    userNotify.setUserNotifyId(UUIDUtil.getUUID());
                    userNotify.setSendUser(applyUser.getUsername());
                    userNotify.setAcceptUser(u.getUsername());
                    userNotify.setIsSee(BooleanUtil.toByte(false));
                    userNotify.setNotifyType(Workbook.notifyType.info.name());
                    userNotify.setNotifyTitle("平台授权审核提醒");
                    userNotify.setNotifyContent(notify);
                    userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                    userNotifyService.save(userNotify);
                }


                ajaxUtil.success().msg("保存成功");
            }

        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param authorizeEditVo 数据
     * @param bindingResult   检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/authorize/update")
    public ResponseEntity<Map<String, Object>> update(@Valid AuthorizeEditVo authorizeEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            rule2(ajaxUtil, authorizeEditVo.getRoleApplyId());
            if (ajaxUtil.getState()) {
                RoleApply roleApply = roleApplyService.findById(authorizeEditVo.getRoleApplyId());
                if (Objects.nonNull(roleApply)) {
                    roleApply.setAuthorizeTypeId(authorizeEditVo.getAuthorizeTypeId());
                    roleApply.setRoleId(authorizeEditVo.getRoleId());
                    roleApply.setDuration(getDuration(authorizeEditVo.getDuration()));
                    roleApply.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(authorizeEditVo.getValidDate()));
                    roleApply.setDataScope(authorizeEditVo.getDataScope());
                    roleApply.setDataId(authorizeEditVo.getDataId());
                    roleApply.setReason(authorizeEditVo.getReason());

                    // 计算时长
                    roleApply.setExpireDate(DateTimeUtil.utilDateToSqlTimestamp(getDuration(authorizeEditVo.getValidDate(), authorizeEditVo.getDuration())));

                    roleApplyService.update(roleApply);

                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("未查询到申请信息");
                }
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 编辑页面进入前检验
     *
     * @param roleApplyId id
     * @return 条件
     */
    @PostMapping("/web/platform/authorize/check/edit/access")
    public ResponseEntity<Map<String, Object>> checkEditAccess(@RequestParam("roleApplyId") String roleApplyId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        rule2(ajaxUtil, roleApplyId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param roleApplyId 角色id
     * @return true成功
     */
    @PostMapping("/web/platform/authorize/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("roleApplyId") String roleApplyId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        rule3(ajaxUtil, roleApplyId);
        if (ajaxUtil.getState()) {
            roleApplyService.deleteById(roleApplyId);
            ajaxUtil.success().msg("删除成功");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新状态
     *
     * @param roleApplyId id
     * @param applyStatus 状态
     * @return true or false
     */
    @PostMapping("/web/platform/authorize/status")
    public ResponseEntity<Map<String, Object>> status(@RequestParam("roleApplyId") String roleApplyId,
                                                      @RequestParam("applyStatus") Byte applyStatus,
                                                      String refuse, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            RoleApply roleApply = roleApplyService.findById(roleApplyId);
            if (Objects.nonNull(roleApply)) {
                roleApply.setApplyStatus(applyStatus);
                roleApply.setRefuse(refuse);
                roleApplyService.update(roleApply);
                Users applyUser = usersService.findByUsername(roleApply.getUsername());
                Users users = usersService.getUserFromSession();

                String notify = "管理员用户【" + users.getRealName() + "】";
                if (applyStatus == 1) {
                    notify += " 通过了您在" + DateTimeUtil.defaultFormatSqlTimestamp(roleApply.getCreateDate()) + "时创建的平台授权申请。";
                } else if (applyStatus == 2) {
                    notify += " 拒绝了您在" + DateTimeUtil.defaultFormatSqlTimestamp(roleApply.getCreateDate()) + "时创建的平台授权申请。原因：" + refuse;
                }

                // 检查邮件推送是否被关闭
                SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                if (StringUtils.equals("1", mailConfigure.getDataValue())) {
                    systemMailService.sendNotifyMail(applyUser, RequestUtil.getBaseUrl(request), notify);
                }

                UserNotify userNotify = new UserNotify();
                userNotify.setUserNotifyId(UUIDUtil.getUUID());
                userNotify.setSendUser(users.getUsername());
                userNotify.setAcceptUser(applyUser.getUsername());
                userNotify.setIsSee(BooleanUtil.toByte(false));
                userNotify.setNotifyType(Workbook.notifyType.info.name());
                userNotify.setNotifyTitle("平台授权审核结果提醒");
                userNotify.setNotifyContent(notify);
                userNotify.setCreateDate(DateTimeUtil.getNowSqlTimestamp());

                userNotifyService.save(userNotify);

                ajaxUtil.success().msg("更新状态成功");
            } else {
                ajaxUtil.fail().msg("未查询到申请信息");
            }
        } else {
            ajaxUtil.fail().msg("您无权限进行操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    private void rule1(AjaxUtil<Map<String, Object>> ajaxUtil, String param, int userCollegeId) {
        List<Authorities> authorities = authoritiesService.findByUsername(param);
        if (Objects.nonNull(authorities) && !authorities.isEmpty()) {
            boolean canApply = true;
            for (Authorities auth : authorities) {
                if (StringUtils.equals(auth.getAuthority(), Workbook.authorities.ROLE_SYSTEM.name()) ||
                        StringUtils.equals(auth.getAuthority(), Workbook.authorities.ROLE_ADMIN.name()) ||
                        StringUtils.equals(auth.getAuthority(), Workbook.authorities.ROLE_ACTUATOR.name())) {
                    canApply = false;
                    break;
                }
            }

            // 不能为管理员，系统或运维申请权限
            if (canApply) {
                ajaxUtil.success().msg("查询成功").put("collegeId", userCollegeId);
            } else {
                ajaxUtil.fail().msg("不能为高权限用户申请授权");
            }
        } else {
            ajaxUtil.fail().msg("该账号未通过审核");
        }
    }

    private void rule2(AjaxUtil<Map<String, Object>> ajaxUtil, String roleApplyId) {
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleApplyId);
            if (roleApplyRecord.isPresent()) {
                RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                Byte status = roleApplyBean.getApplyStatus();
                if (status != 1) {
                    ajaxUtil.success().msg("可操作");
                } else {
                    ajaxUtil.fail().msg("申请已通过，不可操作");
                }
            } else {
                ajaxUtil.fail().msg("未查询到申请信息");
            }
        } else {
            Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleApplyId);
            if (roleApplyRecord.isPresent()) {
                RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                Users users = usersService.getUserFromSession();
                if (StringUtils.equals(users.getUsername(), roleApplyBean.getUsername())) {
                    Byte status = roleApplyBean.getApplyStatus();
                    if (status != 1) {
                        ajaxUtil.success().msg("可操作");
                    } else {
                        ajaxUtil.fail().msg("申请已通过，不可操作");
                    }
                } else {
                    ajaxUtil.fail().msg("非本人申请，不可操作");
                }
            } else {
                ajaxUtil.fail().msg("未查询到申请信息");
            }
        }
    }

    private void rule3(AjaxUtil<Map<String, Object>> ajaxUtil, String roleApplyId) {
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            ajaxUtil.success().msg("可操作");
        } else {
            Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleApplyId);
            if (roleApplyRecord.isPresent()) {
                RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                Users users = usersService.getUserFromSession();
                if (StringUtils.equals(users.getUsername(), roleApplyBean.getUsername())) {
                    ajaxUtil.success().msg("可操作");
                } else {
                    ajaxUtil.fail().msg("非本人申请，不可操作");
                }
            } else {
                ajaxUtil.fail().msg("未查询到申请信息");
            }
        }
    }

    /**
     * 时长转换
     *
     * @param duration 时长
     * @return 转换
     */
    private String getDuration(int duration) {
        String d = "";
        switch (duration) {
            case 1:
                d = "1天";
                break;
            case 2:
                d = "3天";
                break;
            case 3:
                d = "7天";
                break;
            case 4:
                d = "1个月";
                break;
            case 5:
                d = "3个月";
                break;
            case 6:
                d = "1年";
                break;
            case 7:
                d = "3年";
                break;
        }

        return d;
    }

    /**
     * 时间计算
     *
     * @param validDate 有效期
     * @param duration  时长
     * @return 结果
     */
    private java.util.Date getDuration(String validDate, int duration) {
        java.util.Date date = new Date();

        DateTimeFormatter format = DateTimeFormat.forPattern(DateTimeUtil.STANDARD_FORMAT);
        DateTime dateTime = DateTime.parse(validDate, format);
        switch (duration) {
            case 1:
                date = dateTime.plusDays(1).toDate();
                break;
            case 2:
                date = dateTime.plusDays(3).toDate();
                break;
            case 3:
                date = dateTime.plusDays(7).toDate();
                break;
            case 4:
                date = dateTime.plusMonths(1).toDate();
                break;
            case 5:
                date = dateTime.plusMonths(3).toDate();
                break;
            case 6:
                date = dateTime.plusYears(1).toDate();
                break;
            case 7:
                date = dateTime.plusYears(3).toDate();
                break;
        }

        return date;
    }

}
