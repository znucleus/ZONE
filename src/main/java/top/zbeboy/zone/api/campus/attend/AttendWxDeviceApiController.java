package top.zbeboy.zone.api.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.attend.AttendWxDeviceService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendWxDeviceApiController {

    @Resource
    private AttendWxDeviceService attendWxDeviceService;

    /**
     * 查询
     *
     * @param principal 当前用户信息
     * @return true or false
     */
    @ApiLoggingRecord(remark = "校园签到设备查询", channel = Workbook.channel.API, needLogin = true)
    @PostMapping("/api/attend/weixin/device/query")
    public ResponseEntity<Map<String, Object>> query(Principal principal, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxDeviceService.query(users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
