package top.zbeboy.zone.service.educational;

import top.zbeboy.zbase.domain.tables.pojos.TimetableCourse;

import java.io.IOException;
import java.util.List;

public interface TimetableService {

    /**
     * 学生同步课表
     *
     * @param username 账号
     * @param password 密码
     * @throws Exception 异常
     */
    void syncWithStudent(String username, String password) throws Exception;

    /**
     * 生成日历
     *
     * @param timetableCourses 数据
     * @param path             路径
     */
    void generateIcs(List<TimetableCourse> timetableCourses, String path) throws IOException;
}
