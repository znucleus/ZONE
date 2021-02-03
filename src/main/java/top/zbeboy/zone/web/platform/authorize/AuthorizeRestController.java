package top.zbeboy.zone.web.platform.authorize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.notify.UserNotifyService;
import top.zbeboy.zbase.feign.platform.AuthorizeService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.authorize.AuthorizeAddVo;
import top.zbeboy.zbase.vo.platform.authorize.AuthorizeEditVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class AuthorizeRestController {

    @Resource
    private UsersService usersService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private UserNotifyService userNotifyService;

    @Resource
    private RoleService roleService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/paging")
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
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(authorizeService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 根据全部权限类型
     *
     * @return 数据
     */
    @GetMapping("/web/platform/authorize/type")
    public ResponseEntity<Map<String, Object>> authorizeTypeData() {
        Select2Data select2Data = Select2Data.of();
        Optional<List<AuthorizeType>> optionalAuthorizeTypes = authorizeService.authorizeTypeData();
        optionalAuthorizeTypes.ifPresent(authorizeTypes -> authorizeTypes.forEach(authorizeType -> select2Data.add(authorizeType.getAuthorizeTypeId().toString(), authorizeType.getAuthorizeTypeName())));
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
        Optional<List<Role>> optionalRoles = roleService.findCollegeRoleByCollegeIdRelation(collegeId);
        optionalRoles.ifPresent(roles -> roles.forEach(role -> select2Data.add(role.getRoleId(), role.getRoleName())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 检验账号是否符合规则
     *
     * @param targetUsername 账号
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/authorize/check-add-username")
    public ResponseEntity<Map<String, Object>> checkAddUsername(@RequestParam("targetUsername") String targetUsername, @RequestParam("collegeId") int collegeId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = authorizeService.checkAddUsername(users.getUsername(), targetUsername, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param authorizeAddVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/authorize/save")
    public ResponseEntity<Map<String, Object>> save(AuthorizeAddVo authorizeAddVo, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        authorizeAddVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = authorizeService.save(authorizeAddVo);
        if (ajaxUtil.getState()) {
            Users applyUser = null;
            if (StringUtils.isNotBlank(authorizeAddVo.getTargetUsername())) {
                String param = StringUtils.deleteWhitespace(authorizeAddVo.getTargetUsername());
                Optional<Users> result = usersService.findByUsername(param);
                if(result.isPresent()){
                    applyUser = result.get();
                }
            } else {
                applyUser = SessionUtil.getUserFromSession();
            }

            if(Objects.nonNull(applyUser)){
                // 查询该申请人所在院所有院管理员
                List<Users> admins = new ArrayList<>();
                List<Users> staffAdmin = staffService.findByAuthorityAndCollegeId(Workbook.authorities.ROLE_ADMIN.name(), authorizeAddVo.getCollegeId());
                if (Objects.nonNull(staffAdmin) && staffAdmin.size() > 0) {
                    admins.addAll(staffAdmin);
                }

                List<Users> studentAdmin = studentService.findByAuthorityAndCollegeId(Workbook.authorities.ROLE_ADMIN.name(), authorizeAddVo.getCollegeId());
                if (Objects.nonNull(studentAdmin) && studentAdmin.size() > 0) {
                    admins.addAll(studentAdmin);
                }

                String notify = "用户【" + applyUser.getRealName() +
                        "-" + applyUser.getUsername() + "】于" +
                        DateTimeUtil.formatLocalDateTime(DateTimeUtil.STANDARD_FORMAT) +
                        "提交了新的权限申请，请及时到平台授权菜单审核。";
                for (Users u : admins) {
                    // 检查邮件推送是否被关闭
                    Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                    if (optionalSystemConfigure.isPresent() && StringUtils.equals("1", optionalSystemConfigure.get().getDataValue())) {
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
            }

        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param authorizeEditVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/authorize/update")
    public ResponseEntity<Map<String, Object>> update(AuthorizeEditVo authorizeEditVo) {
        Users users = SessionUtil.getUserFromSession();
        authorizeEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = authorizeService.update(authorizeEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 编辑页面进入前检验
     *
     * @param roleApplyId id
     * @return 条件
     */
    @PostMapping("/web/platform/authorize/check-edit-access")
    public ResponseEntity<Map<String, Object>> checkEditAccess(@RequestParam("roleApplyId") String roleApplyId) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = authorizeService.checkEditAccess(users.getUsername(), roleApplyId);
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = authorizeService.delete(users.getUsername(), roleApplyId);
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = authorizeService.status(users.getUsername(), roleApplyId, applyStatus, refuse);
        if (ajaxUtil.getState()) {
            Optional<RoleApply> optionalRoleApply = authorizeService.findRoleApplyById(roleApplyId);
            if (optionalRoleApply.isPresent()) {
                RoleApply roleApply = optionalRoleApply.get();
                Optional<Users> result = usersService.findByUsername(roleApply.getUsername());
                if(result.isPresent()){
                    Users applyUser = result.get();
                    String notify = "管理员用户【" + users.getRealName() + "】";
                    if (applyStatus == 1) {
                        notify += " 通过了您在" + DateTimeUtil.defaultFormatSqlTimestamp(roleApply.getCreateDate()) + "时创建的平台授权申请。";
                    } else if (applyStatus == 2) {
                        notify += " 拒绝了您在" + DateTimeUtil.defaultFormatSqlTimestamp(roleApply.getCreateDate()) + "时创建的平台授权申请。原因：" + refuse;
                    }

                    // 检查邮件推送是否被关闭
                    Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                    if (optionalSystemConfigure.isPresent() && StringUtils.equals("1", optionalSystemConfigure.get().getDataValue())) {
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
                }

            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
