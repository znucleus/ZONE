package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendMapKeyService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import java.util.Map;

@Component
public class AttendMapKeyHystrixClientFallbackFactory implements AttendMapKeyService {
    @Override
    public AjaxUtil<Map<String, Object>> mapKey() {
        return AjaxUtil.of();
    }
}
