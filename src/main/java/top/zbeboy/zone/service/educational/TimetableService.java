package top.zbeboy.zone.service.educational;

public interface TimetableService {

    /**
     * 学生同步课表
     *
     * @param username 账号
     * @param password 密码
     * @throws Exception 异常
     */
    void syncWithStudent(String username, String password) throws Exception;
}
