package top.zbeboy.zone.service.data;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.NationDao;
import top.zbeboy.zone.domain.tables.pojos.Nation;

import javax.annotation.Resource;
import java.util.List;

@Service("nationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NationServiceImpl implements NationService {

    @Resource
    private NationDao nationDao;

    @Cacheable(cacheNames = CacheBook.NATIONS)
    @Override
    public List<Nation> findAll() {
        return nationDao.findAll();
    }
}
