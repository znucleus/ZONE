package top.zbeboy.zone.api.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.feign.attend.AttendMapKeyService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
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
    @GetMapping("/api/attend/map_key")
    public ResponseEntity<Map<String, Object>> mapKey() {
        AjaxUtil<Map<String, Object>> ajaxUtil = attendMapKeyService.mapKey();
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
