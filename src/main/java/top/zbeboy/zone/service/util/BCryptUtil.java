package top.zbeboy.zone.service.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    /**
     * BCryptPassword 方式生成密码
     *
     * @param password 需要加密的密码
     * @return 加密后的密码
     */
    public static String bCryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * BCryptPassword 密码比对
     *
     * @param password         未加密的密码
     * @param databasePassword 加密码后的密码或数据库中保存的密码
     * @return 比较结果
     */
    public static Boolean bCryptPasswordMatches(String password, String databasePassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, databasePassword);
    }

    /*$2a$10$HKXHRhnhlC1aZQ4hukD0S.zYep/T5A7FULBo7S2UrJsqQCThUxdo2  123456*/
}
