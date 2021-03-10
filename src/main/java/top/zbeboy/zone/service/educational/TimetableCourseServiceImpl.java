package top.zbeboy.zone.service.educational;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zbase.domain.tables.daos.TimetableCourseDao;
import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;
import top.zbeboy.zbase.domain.tables.records.TimetableCourseRecord;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static top.zbeboy.zbase.domain.Tables.TIMETABLE_COURSE;

@Service("timetableCourseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TimetableCourseServiceImpl implements TimetableCourseService {

    private final DSLContext create;

    @Resource
    private TimetableCourseDao timetableCourseDao;

    @Autowired
    TimetableCourseServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public List<TimetableCourse> findByTimetableSemesterIdDistinctRoom(String timetableSemesterId) {
        Result<Record1<String>> result = create.selectDistinct(TIMETABLE_COURSE.ROOM)
                .from(TIMETABLE_COURSE)
                .where(TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId))
                .fetch();
        List<TimetableCourse> timetableCourses = new ArrayList<>();
        if (result.isNotEmpty()) {
            timetableCourses = result.into(TimetableCourse.class);
        }
        return timetableCourses;
    }

    @Override
    public List<TimetableCourse> findByTimetableSemesterIdDistinctLessonName(String timetableSemesterId) {
        Result<Record1<String>> result = create.selectDistinct(TIMETABLE_COURSE.LESSON_NAME)
                .from(TIMETABLE_COURSE)
                .where(TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId))
                .fetch();
        List<TimetableCourse> timetableCourses = new ArrayList<>();
        if (result.isNotEmpty()) {
            timetableCourses = result.into(TimetableCourse.class);
        }
        return timetableCourses;
    }

    @Override
    public List<TimetableCourse> findByTimetableSemesterIdDistinctCourseName(String timetableSemesterId) {
        Result<Record1<String>> result = create.selectDistinct(TIMETABLE_COURSE.COURSE_NAME)
                .from(TIMETABLE_COURSE)
                .where(TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId))
                .fetch();
        List<TimetableCourse> timetableCourses = new ArrayList<>();
        if (result.isNotEmpty()) {
            timetableCourses = result.into(TimetableCourse.class);
        }
        return timetableCourses;
    }

    @Override
    public List<TimetableCourse> findByTimetableSemesterIdDistinctTeachers(String timetableSemesterId) {
        Result<Record1<String>> result = create.selectDistinct(TIMETABLE_COURSE.TEACHERS)
                .from(TIMETABLE_COURSE)
                .where(TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId))
                .fetch();
        List<TimetableCourse> timetableCourses = new ArrayList<>();
        if (result.isNotEmpty()) {
            timetableCourses = result.into(TimetableCourse.class);
        }
        return timetableCourses;
    }

    @Override
    public Result<TimetableCourseRecord> search(TimetableCourse timetableCourse) {
        String courseName = timetableCourse.getCourseName();
        String lessonName = timetableCourse.getLessonName();
        String room = timetableCourse.getRoom();
        String teachers = timetableCourse.getTeachers();
        String timetableSemesterId = timetableCourse.getTimetableSemesterId();
        Condition a = TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId);

        if (StringUtils.isNotBlank(courseName)) {
            a = a.and(TIMETABLE_COURSE.COURSE_NAME.eq(courseName));
        }

        if (StringUtils.isNotBlank(lessonName)) {
            a = a.and(TIMETABLE_COURSE.LESSON_NAME.eq(lessonName));
        }

        if (StringUtils.isNotBlank(room)) {
            a = a.and(TIMETABLE_COURSE.ROOM.eq(room));
        }

        if (StringUtils.isNotBlank(teachers)) {
            a = a.and(TIMETABLE_COURSE.TEACHERS.eq(teachers));
        }
        return create.selectFrom(TIMETABLE_COURSE)
                .where(a)
                .fetch();
    }

    @Override
    public void batchSave(List<TimetableCourse> timetableCourses) {
        timetableCourseDao.insert(timetableCourses);
    }

    @Override
    public void deleteTimetableCourseByTimetableSemesterIdAndLessonName(String timetableSemesterId, String lessonName) {
        create.deleteFrom(TIMETABLE_COURSE)
                .where(TIMETABLE_COURSE.LESSON_NAME.eq(lessonName)
                        .and(TIMETABLE_COURSE.TIMETABLE_SEMESTER_ID.eq(timetableSemesterId)))
                .execute();
    }
}
