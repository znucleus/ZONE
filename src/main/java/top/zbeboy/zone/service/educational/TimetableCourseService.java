package top.zbeboy.zone.service.educational;

import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;

import java.util.List;

public interface TimetableCourseService {

    /**
     * 批量保存
     *
     * @param timetableCourses 数据
     */
    void batchSave(List<TimetableCourse> timetableCourses);

    /**
     * 通过学年id与班级删除
     *
     * @param timetableSemesterId 学年id
     * @param lessonName          班级
     */
    void deleteTimetableCourseByTimetableSemesterIdAndLessonName(int timetableSemesterId, String lessonName);
}
