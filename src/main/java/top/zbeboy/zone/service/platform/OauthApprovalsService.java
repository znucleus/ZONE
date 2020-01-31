package top.zbeboy.zone.service.platform;

public interface OauthApprovalsService {

    /**
     * 通过客户端id删除
     *
     * @param clientId 客户端id
     */
    void deleteByClientId(String clientId);
}
