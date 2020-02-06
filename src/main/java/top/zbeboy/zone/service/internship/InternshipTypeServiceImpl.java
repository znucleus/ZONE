package top.zbeboy.zone.service.internship;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.InternshipTypeDao;
import top.zbeboy.zone.domain.tables.pojos.InternshipType;

import javax.annotation.Resource;
import java.util.List;

@Service("internshipTypeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class InternshipTypeServiceImpl implements InternshipTypeService {

    @Resource
    private InternshipTypeDao internshipTypeDao;

    @Override
    public InternshipType findById(int id) {
        return internshipTypeDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.INTERNSHIP_TYPES)
    @Override
    public List<InternshipType> findAll() {
        return internshipTypeDao.findAll();
    }
}
