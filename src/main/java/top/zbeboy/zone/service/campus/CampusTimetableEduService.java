package top.zbeboy.zone.service.campus;

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
}
