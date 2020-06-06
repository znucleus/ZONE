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
        return null;
    }

    @Override
    public Channel findByChannelName(String name) {
        return null;
    }

    @Override
    public List<Channel> usersData() {
        return new ArrayList<>();
    }
}
