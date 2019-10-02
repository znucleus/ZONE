package top.zbeboy.zone.service.data;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.PoliticalLandscapeDao;
import top.zbeboy.zone.domain.tables.pojos.PoliticalLandscape;

import javax.annotation.Resource;
import java.util.List;

@Service("politicalLandscapeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PoliticalLandscapeServiceImpl implements PoliticalLandscapeService {

    @Resource
    private PoliticalLandscapeDao politicalLandscapeDao;

    @Cacheable(cacheNames = CacheBook.POLITICAL_LANDSCAPES)
    @Override
    public List<PoliticalLandscape> findAll() {
        return politicalLandscapeDao.findAll();
    }
}
