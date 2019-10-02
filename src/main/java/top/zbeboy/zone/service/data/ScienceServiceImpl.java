package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;

import static top.zbeboy.zone.domain.Tables.SCIENCE;

@Service("scienceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ScienceServiceImpl implements ScienceService {

    private final DSLContext create;

    @Autowired
    ScienceServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.SCIENCES, key = "#departmentId + '_' + #scienceIsDel")
    @Override
    public Result<ScienceRecord> findByDepartmentIdAndScienceIsDel(int departmentId, Byte scienceIsDel) {
        return create.selectFrom(SCIENCE).where(SCIENCE.DEPARTMENT_ID.eq(departmentId)
                .and(SCIENCE.SCIENCE_IS_DEL.eq(scienceIsDel))).fetch();
    }
}
