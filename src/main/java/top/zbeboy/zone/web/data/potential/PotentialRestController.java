package top.zbeboy.zone.web.data.potential;

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
import top.zbeboy.zbase.config.system.mobile.SystemMobileConfig;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.PotentialService;
import top.zbeboy.zbase.feign.data.WeiXinSubscribeService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.feign.system.SystemMailService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.FilesUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.tools.web.util.MD5Util;
import top.zbeboy.zbase.tools.web.util.SmallPropsUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.potential.PotentialAddVo;
import top.zbeboy.zbase.vo.data.potential.PotentialEditVo;
import top.zbeboy.zbase.vo.data.potential.PotentialUpgradeStaffVo;
import top.zbeboy.zbase.vo.data.potential.PotentialUpgradeStudentVo;
import top.zbeboy.zbase.vo.data.weixin.WeiXinSubscribeSendVo;
import top.zbeboy.zone.web.platform.common.PlatformControllerCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PotentialRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private PotentialService potentialService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private WeiXinSubscribeService weiXinSubscribeService;

    @Resource
    private PlatformControllerCommon platformControllerCommon;

    /**
     * 临时用户注册
     *
     * @param potentialAddVo 教职工数据
     * @return 注册
     */
    @PostMapping("/anyone/data/register/potential")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterPotential(PotentialAddVo potentialAddVo, HttpSession session, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 手机号是否已验证
        if (!ObjectUtils.isEmpty(session.getAttribute(potentialAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
            boolean isValid = (boolean) session.getAttribute(potentialAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
            if (isValid) {
                Optional<UsersType> optionalUsersType = usersTypeService.findByUsersTypeName(Workbook.POTENTIAL_USERS_TYPE);
                if (optionalUsersType.isPresent()) {
                    UsersType usersType = optionalUsersType.get();
                    potentialAddVo.setEnabled(BooleanUtil.toByte(true));
                    potentialAddVo.setAccountNonExpired(BooleanUtil.toByte(true));
                    potentialAddVo.setCredentialsNonExpired(BooleanUtil.toByte(true));
                    potentialAddVo.setAccountNonLocked(BooleanUtil.toByte(true));
                    potentialAddVo.setUsersTypeId(usersType.getUsersTypeId());
                    potentialAddVo.setAvatar(Workbook.USERS_AVATAR);
                    DateTime dateTime = DateTime.now();
                    dateTime = dateTime.plusDays(ZoneProperties.getMail().getValidCodeTime());
                    potentialAddVo.setMailboxVerifyCode(RandomUtil.generateEmailCheckKey());
                    potentialAddVo.setMailboxVerifyValid(DateTimeUtil.utilDateToLocalDateTime(dateTime.toDate()));
                    potentialAddVo.setJoinDate(DateTimeUtil.getNowLocalDate());
                    potentialAddVo.setLangKey(request.getLocale().toLanguageTag());
                    potentialAddVo.setBaseUrl(RequestUtil.getBaseUrl(request));
                    ajaxUtil = potentialService.save(potentialAddVo);

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
     * 临时用户信息更新
     *
     * @param potentialEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/users/potential/update/school")
    public ResponseEntity<Map<String, Object>> userPotentialUpdateSchool(PotentialEditVo potentialEditVo) {
        Users users = SessionUtil.getUserFromSession();
        potentialEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.userPotentialUpdateSchool(potentialEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param potentialEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/users/potential/update/info")
    public ResponseEntity<Map<String, Object>> userPotentialUpdateInfo(PotentialEditVo potentialEditVo) {
        Users users = SessionUtil.getUserFromSession();
        potentialEditVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.userPotentialUpdateInfo(potentialEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 类型升级
     *
     * @param potentialUpgradeStudentVo 数据
     * @return 升级信息
     */
    @PostMapping("/users/type/upgrade/student")
    public ResponseEntity<Map<String, Object>> upgradeStudent(PotentialUpgradeStudentVo potentialUpgradeStudentVo) {
        Users users = SessionUtil.getUserFromSession();
        potentialUpgradeStudentVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.upgradeStudent(potentialUpgradeStudentVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 类型升级
     *
     * @param potentialUpgradeStaffVo 数据
     * @return 升级信息
     */
    @PostMapping("/users/type/upgrade/staff")
    public ResponseEntity<Map<String, Object>> upgradeStaff(PotentialUpgradeStaffVo potentialUpgradeStaffVo) {
        Users users = SessionUtil.getUserFromSession();
        potentialUpgradeStaffVo.setUsername(users.getUsername());
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.upgradeStaff(potentialUpgradeStaffVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/potential/paging")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("username");
        headers.add("email");
        headers.add("mobile");
        headers.add("idCard");
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
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
        return new ResponseEntity<>(potentialService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 用户角色数据
     *
     * @param username 用户账号
     * @return 数据
     */
    @PostMapping("/web/data/potential/role/data")
    public ResponseEntity<Map<String, Object>> roleData(@RequestParam("username") String username) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Role> ajaxUtil = AjaxUtil.of();
        List<Role> list = new ArrayList<>();
        Optional<List<Role>> optionalRoles = potentialService.roleData(users.getUsername(), username);
        if (optionalRoles.isPresent()) {
            list = optionalRoles.get();
        }
        ajaxUtil.success().list(list).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @return success or false
     */
    @PostMapping("/web/data/potential/role/save")
    public ResponseEntity<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("roles") String roles, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.roleSave(users.getUsername(), username, roles);

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
                weiXinSubscribeSendVo.setStartTime(DateTimeUtil.getNowLocalDateTime());
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
    @PostMapping("/web/data/potential/update/enabled")
    public ResponseEntity<Map<String, Object>> updateEnabled(String userIds, Byte enabled) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.updateEnabled(users.getUsername(), userIds, enabled);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/web/data/potential/update/locked")
    public ResponseEntity<Map<String, Object>> updateLocked(String userIds, Byte locked) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.updateLocked(users.getUsername(), userIds, locked);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新密码
     *
     * @param username 账号
     * @return success or fail
     */
    @PostMapping("/web/data/potential/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestParam("username") String username) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.updatePassword(users.getUsername(), username);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/web/data/potential/delete")
    public ResponseEntity<Map<String, Object>> delete(String userIds, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = potentialService.delete(users.getUsername(), userIds);
        if (StringUtils.isNotBlank(userIds)) {
            List<String> usersList = SmallPropsUtil.StringIdsToStringList(userIds);
            String realPath = RequestUtil.getRealPath(request);
            for (String user : usersList) {
                Optional<Users> result = usersService.findByUsername(user);
                String notify = "您因不满足审核条件，注册信息已被删除。";
                // 微信订阅通知
                if (result.isPresent()) {
                    Users deleteUsers = result.get();
                    WeiXinSubscribeSendVo weiXinSubscribeSendVo = new WeiXinSubscribeSendVo();
                    weiXinSubscribeSendVo.setUsername(deleteUsers.getUsername());
                    weiXinSubscribeSendVo.setBusiness(WeiXinAppBook.subscribeBusiness.REGISTRATION_REVIEW_RESULT.name());
                    weiXinSubscribeSendVo.setThing1("审核未通过");
                    weiXinSubscribeSendVo.setName4(result.get().getRealName());
                    weiXinSubscribeSendVo.setDate2(DateTimeUtil.getNowLocalDateTime(DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                    weiXinSubscribeSendVo.setThing3(notify);
                    weiXinSubscribeSendVo.setStartTime(DateTimeUtil.getNowLocalDateTime());
                    weiXinSubscribeService.sendByBusinessAndUsername(weiXinSubscribeSendVo);

                    String path = Workbook.qrCodePath() + MD5Util.getMD5(users.getUsername() + users.getAvatar()) + ".jpg";
                    FilesUtil.deleteFile(realPath + path);
                }
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
