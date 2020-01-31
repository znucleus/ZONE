package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.OauthAccessToken;

import java.util.List;

public interface OauthAccessTokenService {

    /**
     * 通过客户端id查询
     *
     * @param clientId 客户端id
     * @return 数据
     */
    List<OauthAccessToken> findByClientId(String clientId);

    /**
     * 通过客户端id删除
     *
     * @param clientId 客户端id
     */
    void deleteByClientId(String clientId);
}
