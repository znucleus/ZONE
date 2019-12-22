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
import top.zbeboy.zone.domain.tables.daos.DepartmentDao;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;

import java.util.Optional;

import static top.zbeboy.zone.domain.Tables.*;

@Service("departmentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DSLContext create;

    private DepartmentDao departmentDao;

    @Autowired
    DepartmentServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Department findById(int id) {
        return departmentDao.findById(id);
    }

    @Cacheable(cacheNames = CacheBook.DEPARTMENT, key = "#id")
    @Override
    public Optional<Record> findByIdRelation(int id) {
        return create.select()
                .from(DEPARTMENT)
                .join(COLLEGE)
                .on(DEPARTMENT.COLLEGE_ID.eq(COLLEGE.COLLEGE_ID))
                .join(SCHOOL)
                .on(COLLEGE.SCHOOL_ID.eq(SCHOOL.SCHOOL_ID))
                .where(DEPARTMENT.DEPARTMENT_ID.eq(id))
                .fetchOptional();
    }

    @Cacheable(cacheNames = CacheBook.DEPARTMENTS, key = "#collegeId + '_' + #departmentIsDel")
    @Override
    public Result<DepartmentRecord> findByCollegeIdAndDepartmentIsDel(int collegeId, Byte departmentIsDel) {
        return create.selectFrom(DEPARTMENT).where(DEPARTMENT.COLLEGE_ID.eq(collegeId)
                .and(DEPARTMENT.DEPARTMENT_IS_DEL.eq(departmentIsDel))).fetch();
    }
}
