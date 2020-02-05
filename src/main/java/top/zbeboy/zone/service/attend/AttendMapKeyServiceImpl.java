package top.zbeboy.zone.service.attend;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.AttendMapKeyDao;
import top.zbeboy.zone.domain.tables.pojos.AttendMapKey;

import javax.annotation.Resource;
import java.util.List;

@Service("attendMapKeyService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendMapKeyServiceImpl implements AttendMapKeyService {

    @Resource
    private AttendMapKeyDao attendMapKeyDao;

    @Cacheable(cacheNames = CacheBook.ATTEND_MAP_KEYS)
    @Override
    public List<AttendMapKey> findAll() {
        return attendMapKeyDao.findAll();
    }
}
