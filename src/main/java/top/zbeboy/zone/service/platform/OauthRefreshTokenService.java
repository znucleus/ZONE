package top.zbeboy.zone.service.platform;

public interface OauthRefreshTokenService {

    /**
     * 通过id删除
     *
     * @param tokenId id
     */
    void deleteByTokenId(String tokenId);
}
