package top.zbeboy.zone.service.campus;

import top.zbeboy.zbase.domain.tables.pojos.CampusCourseData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface CampusTimetableEduService {

    /**
     * 同步课表
     *
     * @param username       账号
     * @param password       密码
     * @param targetUsername 当前用户
     * @param semesterId     学期id
     * @throws Exception 异常
     */
    void sync(String username, String password, String targetUsername, int semesterId, HttpServletRequest request) throws Exception;

    /**
     * 生成日历
     *
     * @param campusCourseDataList 数据
     * @param path                 ics路径
     */
    void generateIcs(List<CampusCourseData> campusCourseDataList, String path) throws IOException;
}
