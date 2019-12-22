package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.OrganizeDao;
import top.zbeboy.zone.domain.tables.pojos.Organize;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;

import javax.annotation.Resource;
import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("organizeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrganizeServiceImpl implements OrganizeService {

    private final DSLContext create;

    @Resource
    private OrganizeDao organizeDao;

    @Autowired
    OrganizeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Organize findById(int id) {
        return organizeDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.ORGANIZE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(ORGANIZE)
                .join(GRADE)
                .on(ORGANIZE.GRADE_ID.eq(GRADE.GRADE_ID))
                .join(SCIENCE)
                .on(GRADE.SCIENCE_ID.eq(SCIENCE.SCIENCE_ID))
                .join(DEPARTMENT)
                .on(SCIENCE.DEPARTMENT_ID.eq(DEPARTMENT.DEPARTMENT_ID))
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(ORGANIZE.ORGANIZE_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.ORGANIZES, key = "#gradeId + '_' + #organizeIsDel")
    @Override
    public Result<OrganizeRecord> findByGradeIdAndOrganizeIsDel(int gradeId, Byte organizeIsDel) {
        return create.selectFrom(ORGANIZE).where(ORGANIZE.GRADE_ID.eq(gradeId)
                .and(ORGANIZE.ORGANIZE_IS_DEL.eq(organizeIsDel))).fetch();
    }
}
