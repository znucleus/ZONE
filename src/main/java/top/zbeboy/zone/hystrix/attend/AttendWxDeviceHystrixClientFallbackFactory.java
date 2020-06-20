package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendWxDeviceService;
import top.zbeboy.zone.web.util.AjaxUtil;

import java.util.Map;

@Component
public class AttendWxDeviceHystrixClientFallbackFactory implements AttendWxDeviceService {
    @Override
    public AjaxUtil<Map<String, Object>> query(String username) {
        return AjaxUtil.of();
    }
}
