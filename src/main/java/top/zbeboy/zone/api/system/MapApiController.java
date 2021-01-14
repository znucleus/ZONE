package top.zbeboy.zone.api.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.feign.system.MapKeyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@RestController
public class MapApiController {

    @Resource
    private MapKeyService mapKeyService;

    /**
     * KEY获取
     *
     * @return 数据
     */
    @ApiLoggingRecord(remark = "系统地图KEY", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/map_key_factory/{factory}")
    public ResponseEntity<Map<String, Object>> mapKeys(@PathVariable("factory") String factory, Principal principal, HttpServletRequest request) {
        AjaxUtil<Map<String, Object>> ajaxUtil = mapKeyService.mapKeys(factory);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
