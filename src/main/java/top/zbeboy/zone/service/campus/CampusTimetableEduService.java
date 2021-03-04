package top.zbeboy.zone.service.campus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface CampusTimetableEduService {

    /**
     * 根据账号密码获取新教务系统课表数据
     *
     * @param username 账号
     * @param password 密码
     * @return 数据
     */
    Map<String, Object> data(String username, String password) throws Exception;

    /**
     * 生成日历
     *
     * @param campusCourseReleaseId 课表发布id
     * @param calendarId            校历id
     * @param path                  ics路径
     */
    void generateIcs(String campusCourseReleaseId, String calendarId, String path) throws IOException;
}
