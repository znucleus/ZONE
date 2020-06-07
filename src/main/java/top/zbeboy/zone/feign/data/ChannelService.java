package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.Channel;
import top.zbeboy.zone.hystrix.data.ChannelHystrixClientFallbackFactory;

import java.util.List;

@FeignClient(value = "base-server", fallback = ChannelHystrixClientFallbackFactory.class)
public interface ChannelService {
    /**
     * 获取渠道
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/base/data/channel/{id}")
    Channel findById(@PathVariable("id") int id);

    /**
     * 获取渠道
     *
     * @param name 名
     * @return 数据
     */
    @GetMapping("/base/data/channel_name/{name}")
    Channel findByChannelName(@PathVariable("name") String name);

    /**
     * 全部渠道
     *
     * @return 渠道数据
     */
    @GetMapping("/base/data/channels")
    List<Channel> findAll();
}
