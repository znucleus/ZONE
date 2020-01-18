package top.zbeboy.zone.api.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Files;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.FilesService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UsersApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private FilesService filesService;

    /**
     * API:获取用户信息
     *
     * @param principal 用户
     * @return 数据
     */
    @GetMapping("/api/users")
    public ResponseEntity<Map<String, Object>> users(Principal principal) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
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
                if (Objects.nonNull(files)) {
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
