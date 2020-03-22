package top.zbeboy.zone.service.data;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.WeiXinSmallVersionDao;
import top.zbeboy.zone.domain.tables.pojos.WeiXinSmallVersion;

import javax.annotation.Resource;

@Service("weiXinSmallVersionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WeiXinSmallVersionServiceImpl implements WeiXinSmallVersionService {

    @Resource
    private WeiXinSmallVersionDao weiXinSmallVersionDao;

    @Cacheable(cacheNames = CacheBook.WEI_XIN_SMALL_VERSION, key = "#id")
    @Override
    public WeiXinSmallVersion findById(String id) {
        return weiXinSmallVersionDao.findById(id);
    }
}
