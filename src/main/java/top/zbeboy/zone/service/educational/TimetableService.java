package top.zbeboy.zone.service.educational;

import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TimetableService {

    /**
     * 获取所有学年学期
     *
     * @param username 账号
     * @param password 密码
     * @return 数据
     */
    List<Map<String, Object>> semesters(String username, String password) throws Exception;

    /**
     * 获取所有课程数据
     *
     * @param username 账号
     * @param password 密码
     * @return 数据
     */
    Map<String, Object> data(String username, String password, int semesterId) throws Exception;

    /**
     * 学生同步课表
     *
     * @param username   账号
     * @param password   密码
     * @param semesterId 学期id
     * @throws Exception 异常
     */
    void syncWithStudent(String username, String password, int collegeId, int semesterId) throws Exception;

    /**
     * 生成日历
     *
     * @param timetableCourses 数据
     * @param path             路径
     */
    void generateIcs(List<TimetableCourse> timetableCourses, String path) throws IOException;
}
