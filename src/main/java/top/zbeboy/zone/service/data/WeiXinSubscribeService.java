package top.zbeboy.zone.service.data;

import top.zbeboy.zone.domain.tables.pojos.WeiXinSubscribe;
import top.zbeboy.zone.domain.tables.records.WeiXinSubscribeRecord;

import java.util.Optional;

public interface WeiXinSubscribeService {

    /**
     * 通过账号和模板id查询
     *
     * @param username   账号
     * @param templateId 模板id
     * @return 数据
     */
    Optional<WeiXinSubscribeRecord> findByUsernameAndTemplateId(String username, String templateId);

    /**
     * 保存
     *
     * @param weiXinSubscribe 数据
     */
    void save(WeiXinSubscribe weiXinSubscribe);

    /**
     * 通过账号和模板id删除
     *
     * @param username   账号
     * @param templateId 模板id
     */
    void deleteByByUsernameAndTemplateId(String username, String templateId);
}
