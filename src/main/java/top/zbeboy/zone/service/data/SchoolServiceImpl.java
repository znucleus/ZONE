package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.SchoolDao;
import top.zbeboy.zone.domain.tables.pojos.School;

import javax.annotation.Resource;
import java.util.List;

@Service("schoolService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SchoolServiceImpl implements SchoolService {

    private final DSLContext create;

    @Resource
    private SchoolDao schoolDao;

    @Autowired
    SchoolServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.SCHOOLS)
    @Override
    public List<School> findBySchoolIsDel(Byte schoolIsDel) {
        return schoolDao.fetchBySchoolIsDel(schoolIsDel);
    }
}
