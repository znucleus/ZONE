package top.zbeboy.zone.feign.data;

import top.zbeboy.zone.domain.tables.pojos.Channel;

import java.util.List;

public interface ChannelService {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     */
    Channel findById(int id);

    /**
     * 根据渠道名查询
     *
     * @param channelName 渠道名
     * @return 用户类型
     */
    Channel findByChannelName(String channelName);

    /**
     * 查询全部
     *
     * @return 数据
     */
    List<Channel> findAll();
}
