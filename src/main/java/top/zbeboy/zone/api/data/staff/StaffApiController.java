package top.zbeboy.zone.api.data.staff;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.WeiXinService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zbase.vo.data.staff.StaffAddVo;
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
public class StaffApiController {

    @Resource
    private StaffService staffService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UsersService usersService;

    @Resource
    private WeiXinService weiXinService;

    /**
     * API:获取教职工信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "教职工数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/staff")
    public ResponseEntity<Map<String, Object>> users(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
            if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                Map<String, Object> outPut = new HashMap<>();
                outPut.put("staffId", bean.getStaffId());
                outPut.put("staffNumber", bean.getStaffNumber());
                outPut.put("departmentId", bean.getDepartmentId());
                outPut.put("schoolId", bean.getSchoolId());
                outPut.put("realName", bean.getRealName());
                ajaxUtil.success().msg("获取用户信息成功").map(outPut);
            } else {
                ajaxUtil.fail().msg("未查询到教职工信息");
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教职工注册
     *
     * @param staffAddVo 教职工数据
     * @return 注册
     */
    @PostMapping("/overt/register/staff")
    public ResponseEntity<Map<String, Object>> overtDataRegisterStaff(StaffAddVo staffAddVo, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        boolean canRegister = false;
        // step 1.手机号是否已验证
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        if (stringRedisTemplate.hasKey(staffAddVo.getMobile() + "_" + staffAddVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID)) {
            String validContent = ops.get(staffAddVo.getMobile() + "_" + staffAddVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID);
            boolean isValid = BooleanUtils.toBoolean(validContent);
            if (isValid) {
                // step 2.检验账号
                ajaxUtil = usersService.anyoneCheckUsername(staffAddVo.getUsername());
                if (ajaxUtil.getState()) {
                    // step 3.检验邮箱
                    ajaxUtil = usersService.anyoneCheckEmail(staffAddVo.getEmail());
                    if (ajaxUtil.getState()) {
                        // step 4.检验工号
                        ajaxUtil = staffService.anyoneCheckStaffNumber(staffAddVo.getStaffNumber());
                        if (ajaxUtil.getState()) {
                            // step 5.检验手机号
                            ajaxUtil = usersService.anyoneCheckMobile(staffAddVo.getMobile());
                            if (ajaxUtil.getState()) {
                                // step 6.检验密码是否一致
                                if (staffAddVo.getPassword().equals(staffAddVo.getOkPassword())) {
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
            staffAddVo.setJoinDate(DateTimeUtil.getNowSqlDate());
            staffAddVo.setLangKey(request.getLocale().toLanguageTag());
            staffAddVo.setBaseUrl(RequestUtil.getBaseUrl(request));
            ajaxUtil = staffService.save(staffAddVo);

            if (ajaxUtil.getState()) {
                // 注册微信
                if(StringUtils.isNotBlank(staffAddVo.getResCode()) && StringUtils.isNotBlank(staffAddVo.getAppId())
                        &&StringUtils.isNotBlank(staffAddVo.getSecret())){
                    weiXinService.save(staffAddVo.getResCode(), staffAddVo.getAppId(), staffAddVo.getSecret(), staffAddVo.getUsername());
                }

                Users users = new Users();
                users.setUsername(staffAddVo.getUsername());
                users.setLangKey(staffAddVo.getLangKey());
                users.setMailboxVerifyCode(staffAddVo.getMailboxVerifyCode());
                users.setMailboxVerifyValid(staffAddVo.getMailboxVerifyValid());
                users.setEmail(staffAddVo.getEmail());
                users.setRealName(staffAddVo.getRealName());
                systemMailService.sendValidEmailMail(users, staffAddVo.getBaseUrl());
            }
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
