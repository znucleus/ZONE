package top.zbeboy.zone.web.data.staff;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.WeiXinAppBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.WeiXinSubscribeService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.staff.StaffAddVo;
import top.zbeboy.zbase.vo.data.staff.StaffEditVo;
import top.zbeboy.zbase.vo.data.weixin.WeiXinSubscribeSendVo;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StaffRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private StaffService staffService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;

    /**
     * 检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @GetMapping("/anyone/check-staff-number")
    public ResponseEntity<Map<String, Object>> anyoneCheckStaffNumber(@RequestParam("staffNumber") String staffNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.anyoneCheckStaffNumber(staffNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @GetMapping("/users/check-staff-number")
    public ResponseEntity<Map<String, Object>> userCheckStaffNumber(@RequestParam("staffNumber") String staffNumber) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.userCheckStaffNumber(users.getUsername(), staffNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教职工注册
     *
     * @param staffAddVo 教职工数据
     * @return 注册
     */
    @PostMapping("/anyone/data/register/staff")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterStaff(StaffAddVo staffAddVo, HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 手机号是否已验证
        if (!ObjectUtils.isEmpty(session.getAttribute(staffAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
            boolean isValid = (boolean) session.getAttribute(staffAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
            if (isValid) {
                Optional<UsersType> optionalUsersType = usersTypeService.findByUsersTypeName(Workbook.STAFF_USERS_TYPE);
                if(optionalUsersType.isPresent()){
                    UsersType usersType = optionalUsersType.get();
                    staffAddVo.setEnabled(BooleanUtil.toByte(true));
                    staffAddVo.setAccountNonExpired(BooleanUtil.toByte(true));
                    staffAddVo.setCredentialsNonExpired(BooleanUtil.toByte(true));
                    staffAddVo.setAccountNonLocked(BooleanUtil.toByte(true));
                    staffAddVo.setUsersTypeId(usersType.getUsersTypeId());
                    staffAddVo.setAvatar(Workbook.USERS_AVATAR);
                    DateTime dateTime = DateTime.now();
                    dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                    staffAddVo.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                    staffAddVo.setMailboxVerifyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                    staffAddVo.setJoinDate(DateTimeUtil.getNowSqlDate());
                    staffAddVo.setLangKey(request.getLocale().toLanguageTag());
                    staffAddVo.setBaseUrl(RequestUtil.getBaseUrl(request));
                    ajaxUtil = staffService.save(staffAddVo);

                    if (ajaxUtil.getState()) {
                        Users users = new Users();
                        users.setUsername(staffAddVo.getUsername());
                        users.setLangKey(staffAddVo.getLangKey());
                        users.setMailboxVerifyCode(staffAddVo.getMailboxVerifyCode());
                        users.setMailboxVerifyValid(staffAddVo.getMailboxVerifyValid());
                        users.setEmail(staffAddVo.getEmail());
                        users.setRealName(staffAddVo.getRealName());
                        systemMailService.sendValidEmailMail(users, staffAddVo.getBaseUrl());
                    }
                } else {
                    ajaxUtil.fail().msg("未查询到用户类型信息");
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
     * 教职工信息更新
     *
     * @param staffEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/users/staff/update/school")
    public ResponseEntity<Map<String, Object>> userStaffUpdateSchool(StaffEditVo staffEditVo) {
        Users users = SessionUtil.getUserFromSession();
        staffEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.userStaffUpdateSchool(staffEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param staffEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/users/staff/update/info")
    public ResponseEntity<Map<String, Object>> userStaffUpdateInfo(StaffEditVo staffEditVo) {
        Users users = SessionUtil.getUserFromSession();
        staffEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.userStaffUpdateInfo(staffEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/staff/paging")
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
        Users users = SessionUtil.getUserFromSession();
        dataTablesUtil.setUsername(users.getUsername());
        return new ResponseEntity<>(staffService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 用户角色数据
     *
     * @param username 用户账号
     * @return 数据
     */
    @PostMapping("/web/data/staff/role/data")
    public ResponseEntity<Map<String, Object>> roleData(@RequestParam("username") String username) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Role> ajaxUtil = AjaxUtil.of();
        List<Role> roles = new ArrayList<>();
        Optional<List<Role>> optionalRoles = staffService.roleData(users.getUsername(), username);
        if(optionalRoles.isPresent()){
            roles = optionalRoles.get();
        }
        ajaxUtil.success().list(roles).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @return success or false
     */
    @PostMapping("/web/data/staff/role/save")
    public ResponseEntity<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("roles") String roles, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.roleSave(users.getUsername(), username, roles);

        if (ajaxUtil.getState()) {
            Optional<Users> result = usersService.findByUsername(username);
            String notify = "您的权限已发生变更，请登录查看。";

            // 检查邮件推送是否被关闭
            Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
            if (optionalSystemConfigure.isPresent() && StringUtils.equals("1", optionalSystemConfigure.get().getDataValue())) {
                result.ifPresent(value -> systemMailService.sendNotifyMail(value, RequestUtil.getBaseUrl(request), notify));
            }

            // 微信订阅通知
            if (result.isPresent()) {
                WeiXinSubscribeSendVo weiXinSubscribeSendVo = new WeiXinSubscribeSendVo();
                weiXinSubscribeSendVo.setUsername(username);
                weiXinSubscribeSendVo.setBusiness(WeiXinAppBook.subscribeBusiness.REGISTRATION_REVIEW_RESULT.name());
                weiXinSubscribeSendVo.setThing1("审核通过");
                weiXinSubscribeSendVo.setName4(result.get().getRealName());
                weiXinSubscribeSendVo.setDate2(DateTimeUtil.getNowLocalDateTime(DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                weiXinSubscribeSendVo.setThing3(notify);
                weiXinSubscribeSendVo.setStartTime(DateTimeUtil.getNowSqlTimestamp());
                weiXinSubscribeService.sendByBusinessAndUsername(weiXinSubscribeSendVo);
            }
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.updateEnabled(users.getUsername(), userIds, enabled);
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.updateLocked(users.getUsername(), userIds, locked);
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.updatePassword(users.getUsername(), username);
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
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.delete(users.getUsername(), userIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
