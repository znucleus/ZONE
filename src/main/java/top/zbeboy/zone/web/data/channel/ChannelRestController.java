package top.zbeboy.zone.web.data.channel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Channel;
import top.zbeboy.zbase.feign.data.ChannelService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ChannelRestController {

    @Resource
    private ChannelService channelService;

    /**
     * 全部渠道
     *
     * @return 渠道数据
     */
    @GetMapping("/users/data/channel")
    public ResponseEntity<Map<String, Object>> usersData() {
        Select2Data select2Data = Select2Data.of();
        Optional<List<Channel>> optionalChannels = channelService.findAll();
        optionalChannels.ifPresent(channels -> channels.forEach(channel -> select2Data.add(channel.getChannelId().toString(), channel.getChannelName())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
