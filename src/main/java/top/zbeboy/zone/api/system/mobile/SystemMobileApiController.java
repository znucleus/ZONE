package top.zbeboy.zone.api.system.mobile;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.system.SystemMobileService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
public class SystemMobileApiController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private SystemMobileService systemMobileService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UsersService usersService;

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return 验证码
     */
    @ApiLoggingRecord(remark = "获取手机验证码", channel = Workbook.channel.API)
    @GetMapping("/overt/send/mobile")
    public ResponseEntity<Map<String, Object>> overtSendMobile(@RequestParam("mobile") String mobile, HttpServletRequest request) {
        String param = StringUtils.trim(mobile);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Pattern.matches(Workbook.MOBILE_REGEX, param)) {
            Optional<SystemConfigure> optionalSystemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MOBILE_SWITCH.name());
            if(optionalSystemConfigure.isPresent()){
                SystemConfigure systemConfigure = optionalSystemConfigure.get();
                if (StringUtils.equals("1", systemConfigure.getDataValue())) {
                    boolean isSend = false;
                    ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                    if (!stringRedisTemplate.hasKey(param + SystemMobileConfig.MOBILE_CODE)) {
                        isSend = true;
                    } else {
                        ajaxUtil.fail().msg("验证码不可重复发送(" + ZoneProperties.getMobile().getValidCodeTime() + "分钟内)");
                    }

                    if (isSend) {
                        String mobileKey = RandomUtil.generateMobileKey();
                        ops.set(param + SystemMobileConfig.MOBILE_CODE, mobileKey,
                                ZoneProperties.getMobile().getValidCodeTime(),
                                TimeUnit.MINUTES);
                        systemMobileService.sendValidMobileShortMessage(param, mobileKey);
                        ajaxUtil.success().msg("短信验证码已发送...");
                    }
                } else {
                    ajaxUtil.fail().msg("管理员已关闭短信发送");
                }
            } else {
                ajaxUtil.fail().msg("查询系统配置错误");
            }
        } else {
            ajaxUtil.fail().msg("手机号不正确");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 验证验证码
     *
     * @param mobile           手机号
     * @param verificationCode 验证码
     * @return 验证结果
     */
    @ApiLoggingRecord(remark = "验证手机验证码", channel = Workbook.channel.API)
    @PostMapping("/overt/check/mobile-code")
    public ResponseEntity<Map<String, Object>> overtCheckMobileCode(@RequestParam("mobile") String mobile,
                                                                    @RequestParam("verificationCode") String verificationCode,
                                                                    HttpServletRequest request) {
        String param = StringUtils.trim(mobile);
        String code = StringUtils.trim(verificationCode);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Pattern.matches(Workbook.MOBILE_REGEX, param)) {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            if (stringRedisTemplate.hasKey(param + SystemMobileConfig.MOBILE_CODE)) {
                String mobileCode = ops.get(param + SystemMobileConfig.MOBILE_CODE);
                boolean isValid = StringUtils.equals(mobileCode, code);
                if (isValid) {
                    ops.set(param + "_" + code + SystemMobileConfig.MOBILE_VALID, "true",
                            CacheBook.EXPIRES_MINUTES,
                            TimeUnit.MINUTES);
                    ajaxUtil.success().msg("验证成功");
                } else {
                    ajaxUtil.fail().msg("验证码错误");
                }
            } else {
                ajaxUtil.fail().msg("验证码已失效");
            }
        } else {
            ajaxUtil.fail().msg("手机号不正确");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 忘记密码手机提交验证
     *
     * @param mobile           手机号
     * @param verificationCode 验证码
     * @return 是否验证通过
     */
    @ApiLoggingRecord(remark = "忘记密码手机号验证", channel = Workbook.channel.API)
    @PostMapping("/overt/forget-password/mobile")
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestParam("mobile") String mobile, @RequestParam("verificationCode") String verificationCode, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(mobile);
        String code = StringUtils.trim(verificationCode);
        if (Pattern.matches(Workbook.MOBILE_REGEX, param)) {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            if (stringRedisTemplate.hasKey(mobile + "_" + code + SystemMobileConfig.MOBILE_VALID)) {
                String validContent = ops.get(mobile + "_" + code + SystemMobileConfig.MOBILE_VALID);
                boolean isValid = BooleanUtils.toBoolean(validContent);
                if (isValid) {
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("mobile", param);
                    Optional<Users> result = usersService.findByCondition(paramMap);
                    if (result.isPresent()) {
                        ajaxUtil.success().msg("验证通过");
                    } else {
                        ajaxUtil.fail().msg("手机号未注册");
                    }
                } else {
                    ajaxUtil.fail().msg("验证码未验证通过");
                }
            } else {
                ajaxUtil.fail().msg("验证码未验证，请先验证");
            }
        } else {
            ajaxUtil.fail().msg("手机号格式不正确");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
