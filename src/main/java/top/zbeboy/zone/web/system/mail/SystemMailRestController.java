package top.zbeboy.zone.web.system.mail;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.config.ZoneProperties;
import top.zbeboy.zbase.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.SystemConfigureService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.service.system.SystemMailService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
public class SystemMailRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private UsersService usersService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    /**
     * 忘记密码邮箱提交验证
     *
     * @param email 邮箱
     * @return 是否验证通过
     */
    @PostMapping(value = {"/forget_password/mail", "/overt/forget_password/mail"})
    public ResponseEntity<Map<String, Object>> forgetPassword(@RequestParam("email") String email, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(email);
        if (StringUtils.isNotBlank(param)) {
            if (Pattern.matches(Workbook.MAIL_REGEX, param)) {
                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("email", param);
                Optional<Users>  result = usersService.findByCondition(paramMap);
                if (result.isPresent()) {
                    Users users = result.get();
                    if (Objects.isNull(users.getVerifyMailbox()) || !BooleanUtil.toBoolean(users.getVerifyMailbox())) {
                        ajaxUtil.fail().msg("邮箱未验证通过");
                    } else {
                        // 检查邮件推送是否被关闭
                        SystemConfigure mailConfigure = systemConfigureService.findByDataKey(Workbook.SystemConfigure.MAIL_SWITCH.name());
                        if ("1".equals(mailConfigure.getDataValue())) {
                            DateTime dateTime = DateTime.now();
                            dateTime = dateTime.plusDays(ZoneProperties.getMail().getPasswordResetTime());
                            users.setPasswordResetKey(RandomUtil.generateResetKey());
                            users.setPasswordResetKeyValid(DateTimeUtil.utilDateToSqlTimestamp(dateTime.toDate()));
                            usersService.update(users);
                            systemMailService.sendPasswordResetMail(users, RequestUtil.getBaseUrl(request));
                            ajaxUtil.success().msg("验证通过");
                        } else {
                            ajaxUtil.fail().msg("邮件推送已被管理员关闭");
                        }
                    }
                } else {
                    ajaxUtil.fail().msg("邮箱未注册");
                }
            } else {
                ajaxUtil.fail().msg("邮箱格式不正确");
            }
        } else {
            ajaxUtil.fail().msg("邮箱不能为空");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
