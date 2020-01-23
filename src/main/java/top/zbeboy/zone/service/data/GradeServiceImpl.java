package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.GradeDao;
import top.zbeboy.zone.domain.tables.pojos.Grade;
import top.zbeboy.zone.domain.tables.records.GradeRecord;

import javax.annotation.Resource;

import java.util.Optional;

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

    @Override
    public Optional<GradeRecord> findByScienceIdAndGrade(int scienceId, int grade) {
        return create.selectFrom(GRADE).where(GRADE.SCIENCE_ID.eq(scienceId)
                .and(GRADE.GRADE_.eq(grade))).fetchOptional();
    }

    @CacheEvict(cacheNames = CacheBook.GRADES, allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public GradeRecord save(Grade grade) {
        return create.insertInto(GRADE, GRADE.GRADE_, GRADE.GRADE_IS_DEL, GRADE.SCIENCE_ID)
                .values(grade.getGrade(), grade.getGradeIsDel(),grade.getScienceId())
                .returning(GRADE.GRADE_ID)
                .fetchOne();
    }
}
