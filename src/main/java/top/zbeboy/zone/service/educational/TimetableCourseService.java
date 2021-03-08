package top.zbeboy.zone.service.educational;

import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;

import java.util.List;

public interface TimetableCourseService {

    /**
     * 通过学年查询教室
     *
     * @param timetableSemesterId 学年
     * @return 数据
     */
    List<TimetableCourse> findByTimetableSemesterIdDistinctRoom(int timetableSemesterId);

    /**
     * 通过学年查询班级
     *
     * @param timetableSemesterId 学年
     * @return 数据
     */
    List<TimetableCourse> findByTimetableSemesterIdDistinctLessonName(int timetableSemesterId);

    /**
     * 通过学年查询课程
     *
     * @param timetableSemesterId 学年
     * @return 数据
     */
    List<TimetableCourse> findByTimetableSemesterIdDistinctCourseName(int timetableSemesterId);

    /**
     * 通过学年查询教师
     *
     * @param timetableSemesterId 学年
     * @return 数据
     */
    List<TimetableCourse> findByTimetableSemesterIdDistinctTeachers(int timetableSemesterId);

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
