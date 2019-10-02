package top.zbeboy.zone.service.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.daos.CourseDao;
import top.zbeboy.zone.domain.tables.records.CourseRecord;

import javax.annotation.Resource;

import static top.zbeboy.zone.domain.Tables.COURSE;

@Service("courseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final DSLContext create;

    @Resource
    private CourseDao courseDao;

    @Autowired
    CourseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.COURSES, key = "#schoolYear + '_' + #term + '_' + #collegeId + '_' + #courseIsDel")
    @Override
    public Result<CourseRecord> findBySchoolYearAndTermAndCollegeIdCourseIsDel(Byte schoolYear, Byte term, int collegeId, Byte courseIsDel) {
        return create.selectFrom(COURSE)
                .where(COURSE.SCHOOL_YEAR.eq(schoolYear).and(COURSE.TERM.eq(term)).and(COURSE.COURSE_ID.eq(collegeId)).and(COURSE.COURSE_IS_DEL.eq(courseIsDel)))
                .fetch();
    }
}
