package top.zbeboy.zone.api.platform.users;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.platform.user.ResetPasswordApiVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.service.util.BCryptUtil;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class UsersApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * API:获取用户信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "用户数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/users")
    public ResponseEntity<Map<String, Object>> users(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Map<String, Object> outPut = new HashMap<>();
            outPut.put("realName", users.getRealName());
            outPut.put("username", users.getUsername());
            outPut.put("usersTypeId", users.getUsersTypeId());
            outPut.put("verifyMailbox", users.getVerifyMailbox());
            outPut.put("enabled", users.getEnabled());
            outPut.put("accountNonLocked", users.getAccountNonLocked());
            if (StringUtils.isNotBlank(users.getAvatar())) {
                Files files = filesService.findById(users.getAvatar());
                if (Objects.nonNull(files) && StringUtils.isNotBlank(files.getFileId())) {
                    outPut.put("avatar", Workbook.DIRECTORY_SPLIT + files.getRelativePath());
                }
            }
            outPut.put("authorities", ((OAuth2Authentication) principal).getUserAuthentication().getAuthorities());
            ajaxUtil.success().msg("获取用户信息成功").map(outPut);
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 重置密码
     *
     * @param resetPasswordApiVo 数据
     * @param bindingResult      检验
     * @return 重置
     */
    @PostMapping("/overt/reset_password")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid ResetPasswordApiVo resetPasswordApiVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (resetPasswordApiVo.getPassword().equals(resetPasswordApiVo.getOkPassword())) {
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                if (stringRedisTemplate.hasKey(resetPasswordApiVo.getMobile() + "_" + resetPasswordApiVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID)) {
                    String validContent = ops.get(resetPasswordApiVo.getMobile() + "_" + resetPasswordApiVo.getVerificationCode() + SystemMobileConfig.MOBILE_VALID);
                    boolean isValid = BooleanUtils.toBoolean(validContent);
                    if (isValid) {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("mobile", resetPasswordApiVo.getMobile());
                        Optional<Users>  result = usersService.findByCondition(paramMap);
                        if (result.isPresent()) {
                            Users users = result.get();
                            users.setPassword(BCryptUtil.bCryptPassword(resetPasswordApiVo.getPassword()));
                            usersService.update(users);
                            ajaxUtil.success().msg("重置密码成功");
                        } else {
                            ajaxUtil.fail().msg("查询用户注册信息失败");
                        }
                    } else {
                        ajaxUtil.fail().msg("验证码未验证通过");
                    }
                } else {
                    ajaxUtil.fail().msg("验证码未验证，请先验证");
                }
            } else {
                ajaxUtil.fail().msg("密码不一致");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
