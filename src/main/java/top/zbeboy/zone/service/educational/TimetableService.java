package top.zbeboy.zone.service.educational;

import net.fortuna.ical4j.model.WeekDay;
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

    /**
     * 教务数据
     *
     * @param username        账号
     * @param password        密码
     * @param getAllSemesters 是否获取学期
     * @param semesterId      学期id
     * @return 数据
     * @throws Exception 异常
     */
    Map<String, Object> eduData(String username, String password, boolean getAllSemesters, int semesterId) throws Exception;

    /**
     * 计算时间偏移
     *
     * @param date    开始日期
     * @param week    偏移周
     * @param weekDay 那一周的周几
     * @return 时间
     */
    java.util.Date calcDeviationDate(java.sql.Date date, int week, int weekDay);

    /**
     * 获取日历星期
     *
     * @param weekday 星期
     * @return 数据
     */
    WeekDay getWeekday(int weekday);

    /**
     * 开始时间转换
     *
     * @param startUnit 单元
     * @return 数据
     */
    String getStartTime(String startUnit);

    /**
     * 结束时间转换
     *
     * @param endUnit 单元
     * @return 数据
     */
    String getEndTime(String endUnit);
}
