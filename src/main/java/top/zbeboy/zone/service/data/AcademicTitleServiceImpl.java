package top.zbeboy.zone.service.data;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.AcademicTitleDao;
import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;

import javax.annotation.Resource;
import java.util.List;

@Service("academicTitleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AcademicTitleServiceImpl implements AcademicTitleService {

    @Resource
    private AcademicTitleDao academicTitleDao;

    @Cacheable(cacheNames = CacheBook.ACADEMIC_TITLES)
    @Override
    public List<AcademicTitle> findAll() {
        return academicTitleDao.findAll();
    }
}
