package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.feign.data.WeiXinDeviceService;

@Component
public class WeiXinDeviceHystrixClientFallbackFactory implements WeiXinDeviceService {
    @Override
    public WeiXinDevice findByUsername(String username) {
        return new WeiXinDevice();
    }

    @Override
    public void save(WeiXinDevice weiXinDevice) {

    }

    @Override
    public void update(WeiXinDevice weiXinDevice) {

    }
}
