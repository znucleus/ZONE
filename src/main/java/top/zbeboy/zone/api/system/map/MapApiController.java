package top.zbeboy.zone.api.system.map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.MapKey;
import top.zbeboy.zbase.feign.system.MapKeyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
public class MapApiController {

    @Resource
    private MapKeyService mapKeyService;

    /**
     * KEY获取
     *
     * @param factory  厂家
     * @param business 业务
     * @return 数据
     */
    @ApiLoggingRecord(remark = "系统地图KEY", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/system/map-key/{factory}/{business}")
    public ResponseEntity<Map<String, Object>> mapKey(@PathVariable("factory") String factory, @PathVariable("business") String business,
                                                      Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Optional<MapKey> optionalMapKey = mapKeyService.mapKey(factory, business);
        if (optionalMapKey.isPresent()) {
            ajaxUtil.success().msg("获取数据成功").put("data", optionalMapKey.get());
        } else {
            ajaxUtil.fail().msg("获取数据失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
