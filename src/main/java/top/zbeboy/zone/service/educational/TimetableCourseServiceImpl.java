package top.zbeboy.zone.service.educational;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.TimetableCourseDao;
import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;

import javax.annotation.Resource;
import java.util.List;

import static top.zbeboy.zbase.domain.Tables.TIMETABLE_COURSE;

@Service("timetableCourseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TimetableCourseServiceImpl implements TimetableCourseService{

    private final DSLContext create;

    @Resource
    private TimetableCourseDao timetableCourseDao;

    @Autowired
    TimetableCourseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public void batchSave(List<TimetableCourse> timetableCourses) {
        timetableCourseDao.insert(timetableCourses);
    }

    @Override
    public void deleteTimetableCourseByTimetableSemesterIdAndLessonName(int timetableSemesterId, String lessonName) {
        create.deleteFrom(TIMETABLE_COURSE)
                .where(TIMETABLE_COURSE.LESSON_NAME.eq(lessonName)
                        .and(TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId)))
                .execute();
    }
}
