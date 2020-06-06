package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zone.domain.tables.pojos.WeiXinDevice;
import top.zbeboy.zone.hystrix.data.WeiXinDeviceHystrixClientFallbackFactory;

@FeignClient(value = "base-server", fallback = WeiXinDeviceHystrixClientFallbackFactory.class)
public interface WeiXinDeviceService {

    /**
     * 获取设备信息
     *
     * @param username 账号
     * @return 数据
     */
    @GetMapping("/base/data/wei_xin_device/username/{username}")
    WeiXinDevice findByUsername(@PathVariable("username") String username);

    /**
     * 保存
     *
     * @param weiXinDevice 数据
     */
    @PostMapping("/base/data/wei_xin_device/save")
    void save(@RequestBody WeiXinDevice weiXinDevice);

    /**
     * 更新
     *
     * @param weiXinDevice 数据
     */
    @PostMapping("/base/data/wei_xin_device/update")
    void update(@RequestBody WeiXinDevice weiXinDevice);
}
