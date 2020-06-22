package top.zbeboy.zone.api.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.attend.AttendWxDeviceService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
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
    @PostMapping("/api/attend/weixin/device/query")
    public ResponseEntity<Map<String, Object>> query(Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        AjaxUtil<Map<String, Object>> ajaxUtil = attendWxDeviceService.query(users.getUsername());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
