package top.zbeboy.zone.service.cache.weixin;

import java.io.IOException;

public interface WeiXinCacheService {

    /**
     * 获取token
     *
     * @return token
     */
    String getAccessToken() throws IOException;
}
