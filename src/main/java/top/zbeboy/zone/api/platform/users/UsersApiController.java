package top.zbeboy.zone.api.platform.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UsersApiController {

    @Resource
    private FilesService filesService;

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
}
