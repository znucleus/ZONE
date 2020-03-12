package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.GoogleOauth;
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

    /**
     * 保存
     *
     * @param googleOauth 数据
     */
    void save(GoogleOauth googleOauth);

    /**
     * 通过账号删除
     *
     * @param username 账号
     */
    void deleteByUsername(String username);
}
