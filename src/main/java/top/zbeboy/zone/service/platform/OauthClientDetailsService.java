package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.OauthClientDetails;

public interface OauthClientDetailsService {

    /**
     * 保存
     *
     * @param oauthClientDetails 数据
     */
    void save(OauthClientDetails oauthClientDetails);
}
