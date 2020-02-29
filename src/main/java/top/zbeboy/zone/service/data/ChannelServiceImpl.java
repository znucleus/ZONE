package top.zbeboy.zone.service.data;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.ChannelDao;
import top.zbeboy.zone.domain.tables.pojos.Channel;

import javax.annotation.Resource;
import java.util.List;

@Service("channelService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelDao channelDao;

    @Cacheable(cacheNames = CacheBook.CHANNEL_BY_ID, key = "#id")
    @Override
    public Channel findById(int id) {
        return channelDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.CHANNEL_BY_NAME, key = "#channelName")
    @Override
    public Channel findByChannelName(String channelName) {
        return channelDao.fetchOneByChannelName(channelName);
    }

    @Override
    public List<Channel> findAll() {
        return channelDao.findAll();
    }
}
