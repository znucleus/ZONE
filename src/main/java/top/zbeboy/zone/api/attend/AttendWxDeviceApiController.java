package top.zbeboy.zone.api.attend;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.feign.data.WeiXinDeviceService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class AttendWxDeviceApiController {

    @Resource
    private WeiXinDeviceService weiXinDeviceService;

    /**
     * 查询
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/device/query")
    public ResponseEntity<Map<String, Object>> query(Principal principal) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            WeiXinDevice weiXinDevice = weiXinDeviceService.findByUsername(users.getUsername());
            if (Objects.nonNull(weiXinDevice) && StringUtils.isNotBlank(weiXinDevice.getDeviceId())) {
                ajaxUtil.success().msg("查询信息成功").put("device", weiXinDevice);
            } else {
                ajaxUtil.fail().msg("未查询到设备信息");
            }
        } else {
            ajaxUtil.fail().msg("查询用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
