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
import top.zbeboy.zone.domain.tables.records.CollegeRecord;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.COLLEGE;
import static top.zbeboy.zone.domain.Tables.SCHOOL;

@Service("collegeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CollegeServiceImpl implements CollegeService {

    private final DSLContext create;

    @Autowired
    CollegeServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.COLLEGE, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(COLLEGE)
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(COLLEGE.COLLEGE_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.COLLEGES, key = "#schoolId + '_' + #collegeIsDel")
    @Override
    public Result<CollegeRecord> findBySchoolIdAndCollegeIsDel(int schoolId, Byte collegeIsDel) {
        return create.selectFrom(COLLEGE).where(COLLEGE.SCHOOL_ID.eq(schoolId)
                .and(COLLEGE.COLLEGE_IS_DEL.eq(collegeIsDel))).fetch();
    }
}
