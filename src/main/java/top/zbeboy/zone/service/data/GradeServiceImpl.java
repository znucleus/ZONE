package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.GradeDao;
import top.zbeboy.zone.domain.tables.pojos.Grade;
import top.zbeboy.zone.domain.tables.records.GradeRecord;

import javax.annotation.Resource;

import static top.zbeboy.zone.domain.Tables.GRADE;

@Service("gradeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GradeServiceImpl implements GradeService {

    private final DSLContext create;

    @Resource
    private GradeDao gradeDao;

    @Autowired
    GradeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Grade findById(int id) {
        return gradeDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.GRADES, key = "#scienceId + '_' + #gradeIsDel")
    @Override
    public Result<GradeRecord> findByScienceIdAndGradeIsDel(int scienceId, Byte gradeIsDel) {
        return create.selectFrom(GRADE).where(GRADE.SCIENCE_ID.eq(scienceId)
                .and(GRADE.GRADE_IS_DEL.eq(gradeIsDel))).fetch();
    }
}
