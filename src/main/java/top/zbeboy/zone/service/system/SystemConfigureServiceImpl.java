package top.zbeboy.zone.service.system;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.SystemConfigureDao;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;

import javax.annotation.Resource;

@Service("systemConfigureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystemConfigureServiceImpl implements SystemConfigureService {

    @Resource
    private SystemConfigureDao systemConfigureDao;

    @Cacheable(cacheNames = CacheBook.SYSTEM_CONFIGURE, key = "#dataKey")
    @Override
    public SystemConfigure findByDataKey(String dataKey) {
        return systemConfigureDao.findById(dataKey);
    }
}
