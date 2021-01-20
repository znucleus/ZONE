package top.zbeboy.zone.api.system.mobile;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.system.SystemMobileService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
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

    /**
     * 发送验证码
     *
     * @param mobile  手机号
     * @return 验证码
     */
    @ApiLoggingRecord(remark = "获取手机验证码", channel = Workbook.channel.API)
    @GetMapping("/overt/send/mobile")
    public ResponseEntity<Map<String, Object>> overtSendMobile(@RequestParam("mobile") String mobile) {
        String param = StringUtils.trim(mobile);
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (Pattern.matches(Workbook.MOBILE_REGEX, param)) {
            SystemConfigure systemConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MOBILE_SWITCH.name());
            if (StringUtils.equals("1", systemConfigure.getDataValue())) {
                boolean isSend = false;
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                if(!stringRedisTemplate.hasKey(param + SystemMobileConfig.MOBILE_CODE)){
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
            ajaxUtil.fail().msg("手机号不正确");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}