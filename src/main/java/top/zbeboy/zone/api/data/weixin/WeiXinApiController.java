package top.zbeboy.zone.api.data.weixin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.WeiXinService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class WeiXinApiController {

    @Resource
    private WeiXinService weiXinService;

    /**
     * 保存
     *
     * @param resCode   code
     * @param principal 当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到微信保存", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/weixin/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("resCode") String resCode,
                                                    @RequestParam("appId") String appId,
                                                    @RequestParam("secret") String secret,
                                                    Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = weiXinService.save(resCode, appId, secret, users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
