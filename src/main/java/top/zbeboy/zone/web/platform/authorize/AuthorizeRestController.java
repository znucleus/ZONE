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
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
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
        headers.add("organizeName");
        headers.add("roleName");
        headers.add("duration");
        headers.add("validDate");
        headers.add("expireDate");
        headers.add("applyStatus");
        headers.add("createDate");
        headers.add("reason");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = roleApplyService.findAllByPage(dataTablesUtil);
        List<RoleApplyBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(RoleApplyBean.class);
            beans.forEach(b -> {
                b.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(b.getValidDate()));
                b.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(b.getExpireDate()));
                b.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(b.getCreateDate()));
            });
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
                Optional<Record> record = Optional.empty();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = staffService.findByUsernameRelation(users.getUsername());
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = studentService.findByUsernameRelation(users.getUsername());
                }

                if (record.isPresent()) {
                    userCollegeId = record.get().into(College.class).getCollegeId();
                } else {
                    ajaxUtil.fail().msg("未查询到该用户所属院信息");
                }
            } else {
                ajaxUtil.fail().msg("未查询到该用户类型");
            }
        } else {
            ajaxUtil.fail().msg("未查询到该账号信息");
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
    public ResponseEntity<Map<String, Object>> save(@Valid AuthorizeAddVo authorizeAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (StringUtils.isNotBlank(authorizeAddVo.getUsername())) {
                String param = StringUtils.deleteWhitespace(authorizeAddVo.getUsername());
                Users users = usersService.findByUsername(param);
                if (Objects.nonNull(users)) {
                    UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                    if (Objects.nonNull(usersType)) {
                        Optional<Record> record = Optional.empty();
                        if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                            record = staffService.findByUsernameRelation(users.getUsername());
                        } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                            record = studentService.findByUsernameRelation(users.getUsername());
                        }

                        if (record.isPresent()) {
                            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                                rule1(ajaxUtil, param, record.get().into(College.class).getCollegeId());
                            } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                                if (record.get().into(College.class).getCollegeId() == authorizeAddVo.getCollegeId()) {
                                    rule1(ajaxUtil, param, record.get().into(College.class).getCollegeId());
                                } else {
                                    ajaxUtil.fail().msg("该账号不在您所属院下，不允许申请操作");
                                }
                            } else {
                                ajaxUtil.fail().msg("您没有权限进行该操作");
                            }
                        } else {
                            ajaxUtil.fail().msg("未查询到该用户所属院信息");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到该用户类型");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到该账号信息");
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
                roleApply.setOrganizeId(authorizeAddVo.getOrganizeId());
                roleApply.setReason(authorizeAddVo.getReason());
                roleApply.setApplyStatus(ByteUtil.toByte(0));
                roleApply.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                if (StringUtils.isNotBlank(authorizeAddVo.getUsername())) {
                    String param = StringUtils.deleteWhitespace(authorizeAddVo.getUsername());
                    roleApply.setUsername(param);
                } else {
                    Users users = usersService.getUserFromSession();
                    roleApply.setUsername(users.getUsername());
                }

                // 计算时长
                roleApply.setExpireDate(DateTimeUtil.utilDateToSqlTimestamp(getDuration(authorizeAddVo.getValidDate(), authorizeAddVo.getDuration())));

                roleApplyService.save(roleApply);

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
            if(ajaxUtil.getState()){
                RoleApply roleApply = roleApplyService.findById(authorizeEditVo.getRoleApplyId());
                if(Objects.nonNull(roleApply)){
                    roleApply.setAuthorizeTypeId(authorizeEditVo.getAuthorizeTypeId());
                    roleApply.setRoleId(authorizeEditVo.getRoleId());
                    roleApply.setDuration(getDuration(authorizeEditVo.getDuration()));
                    roleApply.setValidDate(DateTimeUtil.defaultParseSqlTimestamp(authorizeEditVo.getValidDate()));
                    roleApply.setOrganizeId(authorizeEditVo.getOrganizeId());
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
        rule3(ajaxUtil,roleApplyId);
        if(ajaxUtil.getState()){
            roleApplyService.deleteById(roleApplyId);
            ajaxUtil.success().msg("删除成功");
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
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
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
                ajaxUtil.fail().msg("未查询到该申请信息");
            }
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            // 判断是否同一个院
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                Optional<Record> record = Optional.empty();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = staffService.findByUsernameRelation(users.getUsername());
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = studentService.findByUsernameRelation(users.getUsername());
                }

                if (record.isPresent()) {
                    int collegeId = record.get().into(College.class).getCollegeId();
                    Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleApplyId);
                    if (roleApplyRecord.isPresent()) {
                        RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                        if (collegeId == roleApplyBean.getCollegeId()) {
                            Byte status = roleApplyBean.getApplyStatus();
                            if (status != 1) {
                                ajaxUtil.success().msg("可操作");
                            } else {
                                ajaxUtil.fail().msg("申请已通过，不可操作");
                            }
                        } else {
                            ajaxUtil.fail().msg("该账号不在您所属院下，不允许操作");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到该申请信息");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到该用户所属院信息");
                }
            } else {
                ajaxUtil.fail().msg("未查询到该用户类型");
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
                ajaxUtil.fail().msg("未查询到该申请信息");
            }
        }
    }

    private void rule3(AjaxUtil<Map<String, Object>> ajaxUtil, String roleApplyId) {
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            ajaxUtil.success().msg("可操作");
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            // 判断是否同一个院
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                Optional<Record> record = Optional.empty();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = staffService.findByUsernameRelation(users.getUsername());
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = studentService.findByUsernameRelation(users.getUsername());
                }

                if (record.isPresent()) {
                    int collegeId = record.get().into(College.class).getCollegeId();
                    Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleApplyId);
                    if (roleApplyRecord.isPresent()) {
                        RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                        if (collegeId == roleApplyBean.getCollegeId()) {
                            ajaxUtil.success().msg("可操作");
                        } else {
                            ajaxUtil.fail().msg("该账号不在您所属院下，不允许操作");
                        }
                    } else {
                        ajaxUtil.fail().msg("未查询到该申请信息");
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到该用户所属院信息");
                }
            } else {
                ajaxUtil.fail().msg("未查询到该用户类型");
            }
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
                ajaxUtil.fail().msg("未查询到该申请信息");
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
