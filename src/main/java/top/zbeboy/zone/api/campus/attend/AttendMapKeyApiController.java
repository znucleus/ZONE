package top.zbeboy.zone.api.campus.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.feign.campus.attend.AttendMapKeyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class AttendMapKeyApiController {

    @Resource
    private AttendMapKeyService attendMapKeyService;

    /**
     * 签到定位KEY获取
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "校园签到地图KEY", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/attend/map_key")
    public ResponseEntity<Map<String, Object>> mapKey(Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = attendMapKeyService.mapKey();
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
