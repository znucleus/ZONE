package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Channel;
import top.zbeboy.zone.feign.data.ChannelService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelHystrixClientFallbackFactory implements ChannelService {
    @Override
    public Channel findById(int id) {
        return new Channel();
    }

    @Override
    public Channel findByChannelName(String name) {
        return new Channel();
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>();
    }
}
