package top.zbeboy.zone.service.theory;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.config.CacheBook;
import top.zbeboy.zbase.domain.tables.daos.TheoryConfigureDao;
import top.zbeboy.zbase.domain.tables.pojos.TheoryConfigure;

import javax.annotation.Resource;
import java.util.Optional;

import static org.jooq.impl.DSL.currentDate;
import static top.zbeboy.zbase.domain.Tables.*;

@Service("theoryConfigureService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TheoryConfigureServiceImpl implements TheoryConfigureService {

    private final DSLContext create;

    @Resource
    private TheoryConfigureDao theoryConfigureDao;

    @Autowired
    TheoryConfigureServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public TheoryConfigure findById(String id) {
        return theoryConfigureDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.THEORY_CONFIGURE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(String id) {
        return create.select()
                .from(THEORY_CONFIGURE)
                .leftJoin(SCHOOLROOM)
                .on(THEORY_CONFIGURE.SCHOOLROOM_ID.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .where(THEORY_CONFIGURE.THEORY_CONFIGURE_ID.eq(id))
                .fetchOptional();
    }

    @Override
    public Result<Record> findByTheoryReleaseIdRelation(String theoryReleaseId) {
        return create.select()
                .from(THEORY_CONFIGURE)
                .leftJoin(SCHOOLROOM)
                .on(THEORY_CONFIGURE.SCHOOLROOM_ID.eq(SCHOOLROOM.SCHOOLROOM_ID))
                .leftJoin(BUILDING)
                .on(SCHOOLROOM.BUILDING_ID.eq(BUILDING.BUILDING_ID))
                .where(THEORY_CONFIGURE.THEORY_RELEASE_ID.eq(theoryReleaseId))
                .orderBy(THEORY_CONFIGURE.WEEK_DAY)
                .fetch();
    }

    @Override
    public Result<Record> findIsAuto(byte dayOfWeek) {
        return create.select()
                .from(THEORY_CONFIGURE)
                .leftJoin(THEORY_RELEASE)
                .on(THEORY_CONFIGURE.THEORY_RELEASE_ID.eq(THEORY_RELEASE.THEORY_RELEASE_ID))
                .where(THEORY_CONFIGURE.WEEK_DAY.eq(dayOfWeek)
                        .and(THEORY_RELEASE.START_DATE.le(currentDate()))
                        .and(THEORY_RELEASE.END_DATE.ge(currentDate())))
                .fetch();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void save(TheoryConfigure theoryConfigure) {
        theoryConfigureDao.insert(theoryConfigure);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_CONFIGURE, key = "#theoryConfigure.theoryConfigureId")
    @Override
    public void update(TheoryConfigure theoryConfigure) {
        theoryConfigureDao.update(theoryConfigure);
    }

    @CacheEvict(cacheNames = CacheBook.THEORY_CONFIGURE, key = "#id")
    @Override
    public void deleteById(String id) {
        theoryConfigureDao.deleteById(id);
    }
}
