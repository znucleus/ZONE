package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.records.GoogleOauthRecord;

import java.util.Optional;

public interface GoogleOauthService {

    /**
     * 通过账号查询
     *
     * @param username 账号
     * @return 数据
     */
    Optional<GoogleOauthRecord> findByUsername(String username);
}
