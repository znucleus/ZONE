package top.zbeboy.zone.web.system.mobile;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMobileService;
import top.zbeboy.zone.service.util.RandomUtil;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
public class SystemMobileRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private SystemMobileService systemMobileService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private UsersService usersService;

    /**
     * 发送验证码
     *
     * @param mobile  手机号
     * @param session 用户session
     * @return 验证码
     */
    @GetMapping("/anyone/send/mobile")
    public ResponseEntity<Map<String, Object>> anyoneSendMobile(@RequestParam("mobile") String mobile, HttpSession session) {
        String param = StringUtils.trim(mobile);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Pattern.matches(SystemMobileConfig.MOBILE_REGEX, param)) {
            SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MOBILE_SWITCH.name());
            if (StringUtils.equals("1", systemConfigure.getDataValue())) {
                boolean isSend = false;
                if (!ObjectUtils.isEmpty(session.getAttribute(SystemMobileConfig.MOBILE))) {
                    String tempMobile = (String) session.getAttribute(SystemMobileConfig.MOBILE);
                    if (!StringUtils.equals(tempMobile, param)) {
                        isSend = true;
                    } else {
                        if (!ObjectUtils.isEmpty(session.getAttribute(param + SystemMobileConfig.MOBILE_EXPIRE))) {
                            Date mobileExpiry = (Date) session.getAttribute(param + SystemMobileConfig.MOBILE_EXPIRE);
                            if (new Date().before(mobileExpiry)) {
                                ajaxUtil.fail().msg("验证码不可重复发送(" + ZoneProperties.getMobile().getValidCodeTime() + "分钟内)");
                            } else {
                                isSend = true;
                            }
                        } else {
                            isSend = true;
                        }
                    }
                } else {
                    isSend = true;
                }

                if (isSend) {
                    DateTime dateTime = DateTime.now();
                    dateTime = dateTime.plusMinutes(ZoneProperties.getMobile().getValidCodeTime());
                    String mobileKey = RandomUtil.generateMobileKey();
                    session.setAttribute(SystemMobileConfig.MOBILE, param);
                    session.setAttribute(param + SystemMobileConfig.MOBILE_EXPIRE, dateTime.toDate());
                    session.setAttribute(param + SystemMobileConfig.MOBILE_CODE, mobileKey);
                    systemMobileService.sendValidMobileShortMessage(param, mobileKey);
                    ajaxUtil.success().msg("短信验证码已发送...");
                }
            } else {
                ajaxUtil.fail().msg("管理员已关闭短信发送");
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
     * @param session          用户session
     * @return 验证结果
     */
    @PostMapping("/anyone/check/mobile/code")
    public ResponseEntity<Map<String, Object>> anyoneCheckMobileCode(@RequestParam("mobile") String mobile,
                                                                     @RequestParam("verificationCode") String verificationCode, HttpSession session) {
        String param = StringUtils.trim(mobile);
        String code = StringUtils.trim(verificationCode);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Pattern.matches(SystemMobileConfig.MOBILE_REGEX, param)) {
            boolean hasError = isHasError(session, param, ajaxUtil);

            if (!hasError) {
                if (!ObjectUtils.isEmpty(session.getAttribute(param + SystemMobileConfig.MOBILE_EXPIRE))) {
                    Date mobileExpiry = (Date) session.getAttribute(param + SystemMobileConfig.MOBILE_EXPIRE);
                    if (!new Date().before(mobileExpiry)) {
                        ajaxUtil.fail().msg("验证码已失效(" + ZoneProperties.getMobile().getValidCodeTime() + "分钟内有效)");
                        hasError = true;
                    }
                } else {
                    ajaxUtil.fail().msg("获取有效时间失败");
                    hasError = true;
                }
            }

            if (!hasError) {
                if (!ObjectUtils.isEmpty(session.getAttribute(param + SystemMobileConfig.MOBILE_CODE))) {
                    String mobileCode = (String) session.getAttribute(param + SystemMobileConfig.MOBILE_CODE);
                    if (!StringUtils.equals(mobileCode, code)) {
                        ajaxUtil.fail().msg("验证码错误");
                        hasError = true;
                    }
                } else {
                    ajaxUtil.fail().msg("获取验证码失败");
                    hasError = true;
                }
            }

            if (!hasError) {
                session.setAttribute(param + SystemMobileConfig.MOBILE_VALID, true);
                ajaxUtil.success().msg("验证成功");
            }
        } else {
            ajaxUtil.fail().msg("手机号不正确");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取验证码有效性
     *
     * @param mobile  手机号
     * @param session 用户session
     * @return 有效性结果
     */
    @PostMapping("/anyone/data/mobile/code")
    public ResponseEntity<Map<String, Object>> anyoneDataMobileCode(@RequestParam("mobile") String mobile, HttpSession session) {
        String param = StringUtils.trim(mobile);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Pattern.matches(SystemMobileConfig.MOBILE_REGEX, param)) {
            boolean hasError = isHasError(session, param, ajaxUtil);

            if (!hasError) {
                if (!ObjectUtils.isEmpty(session.getAttribute(param + SystemMobileConfig.MOBILE_VALID))) {
                    boolean isValid = (boolean) session.getAttribute(param + SystemMobileConfig.MOBILE_VALID);
                    if (isValid) {
                        ajaxUtil.success().msg("验证成功");
                    } else {
                        ajaxUtil.fail().msg("验证失败");
                    }
                } else {
                    ajaxUtil.fail().msg("验证失败");
                }
            }
        } else {
            ajaxUtil.fail().msg("手机号不正确");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 忘记密码手机提交验证
     *
     * @param mobile 手机号
     * @return 是否验证通过
     */
    @PostMapping("/forget_password/mobile")
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestParam("mobile") String mobile, HttpSession session) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = org.springframework.util.StringUtils.trimWhitespace(mobile);
        if (org.springframework.util.StringUtils.hasLength(param)) {
            if (Pattern.matches(SystemMobileConfig.MOBILE_REGEX, param)) {
                if (!ObjectUtils.isEmpty(session.getAttribute(param + SystemMobileConfig.MOBILE_VALID))) {
                    boolean isValid = (boolean) session.getAttribute(param + SystemMobileConfig.MOBILE_VALID);
                    if (isValid) {
                        Users users = usersService.findByMobile(param);
                        if (Objects.nonNull(users)) {
                            ajaxUtil.success().msg("验证通过");
                        } else {
                            ajaxUtil.fail().msg("手机号未注册");
                        }
                    } else {
                        ajaxUtil.fail().msg("验证码无效");
                    }
                } else {
                    ajaxUtil.fail().msg("手机号未验证");
                }
            } else {
                ajaxUtil.fail().msg("手机号格式不正确");
            }
        } else {
            ajaxUtil.fail().msg("手机号不能为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取手机号
     *
     * @param session  用户会话
     * @param param    手机号
     * @param ajaxUtil 参数对象
     * @return 是否正常
     */
    private boolean isHasError(HttpSession session, String param, AjaxUtil ajaxUtil) {
        boolean hasError = false;
        if (!ObjectUtils.isEmpty(session.getAttribute(SystemMobileConfig.MOBILE))) {
            String tempMobile = (String) session.getAttribute(SystemMobileConfig.MOBILE);
            if (!StringUtils.equals(tempMobile, param)) {
                ajaxUtil.fail().msg("请重新获取验证码");
                hasError = true;
            }
        } else {
            ajaxUtil.fail().msg("获取当前用户手机号失败");
            hasError = true;
        }
        return hasError;
    }
}
