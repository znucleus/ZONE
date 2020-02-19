package top.zbeboy.zone.service.cache.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.service.util.HttpClientUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static top.zbeboy.zone.domain.Tables.ATTEND_RELEASE_SUB;

@Service("weiXinCacheService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WeiXinCacheServiceImpl implements WeiXinCacheService {

    @Autowired
    private ZoneProperties zoneProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getAccessToken() throws IOException {
        String accessToken = "";
        final String cacheKey = CacheBook.WEI_XIN_ACCESS_TOKEN + zoneProperties.getWeiXin().getAppId();
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(cacheKey))) {
            accessToken = ops.get(cacheKey);
        } else {
            // 获取token
            Map<String, String> map = new HashMap<>();
            map.put("grant_type", "client_credential");
            map.put("appid", zoneProperties.getWeiXin().getAppId());
            map.put("secret", zoneProperties.getWeiXin().getSecret());
            String result = HttpClientUtil.sendGet("https://api.weixin.qq.com/cgi-bin/token", map);
            if (StringUtils.isNotBlank(result)) {
                JSONObject params = JSON.parseObject(result);
                ops.set(cacheKey, params.getString("access_token"), params.getInteger("expires_in"), TimeUnit.SECONDS);
                accessToken = params.getString("access_token");
            }
        }

        return accessToken;
    }

    @Override
    public void sendAttendWxSubscribe(Result<Record> records) {
        if (records.isNotEmpty()) {
            final String cacheKey = CacheBook.WEI_XIN_SUBSCRIBE;
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            for (Record r : records) {
                String id = r.get(ATTEND_RELEASE_SUB.ATTEND_RELEASE_ID);
                DateTime now = DateTime.now();
                DateTime startTime = new DateTime(r.get(ATTEND_RELEASE_SUB.ATTEND_START_TIME));
                ops.set(cacheKey + id, id, Minutes.minutesBetween(now, startTime).getMinutes(), TimeUnit.MINUTES);
            }
        }
    }

    @Override
    public void updateAttendWxSubscribe(AttendRelease attendRelease) {
        final String cacheKey = CacheBook.WEI_XIN_SUBSCRIBE + attendRelease.getAttendReleaseId();
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(cacheKey))) {
            DateTime now = DateTime.now();
            DateTime startTime = new DateTime(attendRelease.getAttendStartTime());
            ops.getOperations().expire(cacheKey, Minutes.minutesBetween(now, startTime).getMinutes(), TimeUnit.MINUTES);
        }
    }

    @Override
    public void deleteAttendWxSubscribe(String attendReleaseId) {
        final String cacheKey = CacheBook.WEI_XIN_SUBSCRIBE + attendReleaseId;
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(cacheKey);
    }
}
