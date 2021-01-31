package top.zbeboy.zone.api.data.weixin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zbase.feign.data.WeiXinDeviceService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@RestController
public class WeiXinDeviceApiController {

    @Resource
    private WeiXinDeviceService weiXinDeviceService;

    /**
     * 查询
     *
     * @param appId     APP_ID
     * @param principal 当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到设备查询", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/data/wei-xin/device/query-with-app-id")
    public AjaxUtil<Map<String, Object>> query(@RequestParam("appId") String appId, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            WeiXinDevice weiXinDevice = weiXinDeviceService.findByUsernameAndAppId(users.getUsername(), appId);
            if (Objects.nonNull(weiXinDevice) && StringUtils.isNotBlank(weiXinDevice.getDeviceId())) {
                ajaxUtil.success().msg("查询信息成功").put("device", weiXinDevice);
            } else {
                ajaxUtil.fail().msg("未查询到设备信息");
            }
        } else {
            ajaxUtil.fail().msg("查询用户信息失败");
        }
        return ajaxUtil;
    }
}
