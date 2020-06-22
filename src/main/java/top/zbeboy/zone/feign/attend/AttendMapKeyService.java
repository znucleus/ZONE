package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.hystrix.attend.AttendMapKeyHystrixClientFallbackFactory;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendMapKeyHystrixClientFallbackFactory.class)
public interface AttendMapKeyService {

    /**
     * 签到定位KEY获取
     *
     * @return 数据
     */
    @GetMapping("/api/attend/map_key")
    AjaxUtil<Map<String, Object>> mapKey();
}
