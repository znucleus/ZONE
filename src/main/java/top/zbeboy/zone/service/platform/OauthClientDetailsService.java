package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.OauthClientDetails;

public interface OauthClientDetailsService {

    /**
     * 保存
     *
     * @param oauthClientDetails 数据
     */
    void save(OauthClientDetails oauthClientDetails);

    /**
     * 更新
     *
     * @param oauthClientDetails 数据
     */
    void update(OauthClientDetails oauthClientDetails);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
