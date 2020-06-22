package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.feign.data.WeiXinDeviceService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import java.util.Map;

@Component
public class WeiXinDeviceHystrixClientFallbackFactory implements WeiXinDeviceService {
    @Override
    public WeiXinDevice findByUsername(String username) {
        return new WeiXinDevice();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(WeiXinDevice weiXinDevice) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(WeiXinDevice weiXinDevice) {
        return AjaxUtil.of();
    }
}
