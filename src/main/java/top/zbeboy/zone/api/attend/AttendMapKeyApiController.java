package top.zbeboy.zone.api.attend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AttendMapKey;
import top.zbeboy.zone.service.attend.AttendMapKeyService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        List<AttendMapKey> keys = attendMapKeyService.findAll();
        if (Objects.nonNull(keys) && !keys.isEmpty()) {
            int second = DateTimeUtil.getNowSecond();
            AttendMapKey attendMapKey = keys.get(second % keys.size());
            ajaxUtil.success().msg("获取数据成功").put("attendMapKey", attendMapKey);
        } else {
            ajaxUtil.fail().msg("获取数据失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
