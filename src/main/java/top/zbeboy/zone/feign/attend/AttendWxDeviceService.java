package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.hystrix.attend.AttendWxDeviceHystrixClientFallbackFactory;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendWxDeviceHystrixClientFallbackFactory.class)
public interface AttendWxDeviceService {

    /**
     * 查询
     *
     * @param username 当前用户
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/device/query")
    AjaxUtil<Map<String, Object>> query(@RequestParam("username") String username);
}
