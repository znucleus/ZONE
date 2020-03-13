package top.zbeboy.zone.service.cache.attend;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.config.WeiXinAppBook;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;
import top.zbeboy.zone.domain.tables.pojos.AttendSubscribeLog;
import top.zbeboy.zone.service.attend.AttendSubscribeLogService;
import top.zbeboy.zone.service.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zone.service.cache.weixin.WeiXinCacheService;
import top.zbeboy.zone.service.data.WeiXinService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.HttpClientUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.bean.data.weixin.WeiXinBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static top.zbeboy.zone.domain.Tables.ATTEND_RELEASE_SUB;

@Service("attendWxCacheService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttendWxCacheServiceImpl implements AttendWxCacheService {

    private final Logger log = LoggerFactory.getLogger(AttendWxCacheServiceImpl.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WeiXinService weiXinService;

    @Resource
    private WeiXinCacheService weiXinCacheService;

    @Resource
    private AttendWxStudentSubscribeService attendWxStudentSubscribeService;

    @Resource
    private AttendSubscribeLogService attendSubscribeLogService;

    @Override
    public void saveAttendWxSubscribe(Result<Record> records) {
        if (records.isNotEmpty()) {
            final String cacheKey = CacheBook.WEI_XIN_SUBSCRIBE;
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            for (Record r : records) {
                String id = r.get(ATTEND_RELEASE_SUB.ATTEND_RELEASE_ID);
                DateTime now = DateTime.now();
                DateTime startTime = new DateTime(r.get(ATTEND_RELEASE_SUB.ATTEND_START_TIME));
                int minutes = Minutes.minutesBetween(now, startTime).getMinutes();
                ops.set(cacheKey + id, id, minutes > 0 ? minutes : 1, TimeUnit.MINUTES);
            }
        }
    }

    @Override
    public void saveAttendWxSubscribe(AttendRelease attendRelease) {
        if (Objects.nonNull(attendRelease)) {
            final String cacheKey = CacheBook.WEI_XIN_SUBSCRIBE;
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            DateTime now = DateTime.now();
            DateTime startTime = new DateTime(attendRelease.getAttendStartTime());
            int minutes = Minutes.minutesBetween(now, startTime).getMinutes();
            ops.set(cacheKey + attendRelease.getAttendReleaseId(), attendRelease.getAttendReleaseId(), minutes > 0 ? minutes : 1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void sendAttendWxSubscribe(String cacheKey) throws IOException {
        String id = cacheKey.replace(CacheBook.WEI_XIN_SUBSCRIBE, "");
        final String accessToken = weiXinCacheService.getAccessToken();

        Result<Record> subRecord = attendWxStudentSubscribeService.findByAttendReleaseIdWithSubscribe(id);
        if (subRecord.isNotEmpty()) {
            List<AttendReleaseSubBean> beans = subRecord.into(AttendReleaseSubBean.class);
            for (AttendReleaseSubBean bean : beans) {
                Optional<Record> weiXinRecord = weiXinService.findByStudentIdAndAppId(bean.getStudentId(), WeiXinAppBook.ATTEND_APP_ID);
                if (weiXinRecord.isPresent()) {
                    WeiXinBean weiXinBean = weiXinRecord.get().into(WeiXinBean.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("touser", weiXinBean.getOpenId());
                    map.put("template_id", bean.getTemplateId());
                    map.put("page", bean.getPage());
                    map.put("miniprogram_state", bean.getMiniProgramState());
                    map.put("lang", bean.getLang());

                    Map<String, Object> data = new HashMap<>();

                    Map<String, Object> phrase1 = new HashMap<>();
                    phrase1.put("value", "待签到");
                    data.put("phrase1", phrase1);

                    Map<String, Object> name2 = new HashMap<>();
                    name2.put("value", weiXinBean.getRealName());
                    data.put("name2", name2);

                    Map<String, Object> date3 = new HashMap<>();
                    date3.put("value", DateTimeUtil.formatSqlTimestamp(DateTimeUtil.getNowSqlTimestamp(), DateTimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_FORMAT));
                    data.put("date3", date3);

                    Map<String, Object> thing5 = new HashMap<>();
                    thing5.put("value", "校内");
                    data.put("thing5", thing5);

                    map.put("data", data);
                    String json = JSON.toJSONString(map);

                    String result = HttpClientUtil.sendJsonPost("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken, json);
                    log.debug("Send attend weixin subscriber username:{}, release_id:{}, result:{}", weiXinBean.getUsername(), id, result);
                    AttendSubscribeLog log = new AttendSubscribeLog();
                    log.setLogId(UUIDUtil.getUUID());
                    log.setAttendReleaseId(id);
                    log.setUsername(weiXinBean.getUsername());
                    log.setOpenId(weiXinBean.getOpenId());
                    log.setTemplateId(bean.getTemplateId());
                    log.setRealName(weiXinBean.getRealName());
                    log.setRequest(json);
                    log.setResult(result);
                    log.setCreateDate(DateTimeUtil.getNowSqlTimestamp());
                    attendSubscribeLogService.save(log);
                    // 下发后得删除记录
                    attendWxStudentSubscribeService.deleteByAttendReleaseIdAndStudentId(id, bean.getStudentId());
                }
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
            int minutes = Minutes.minutesBetween(now, startTime).getMinutes();
            ops.getOperations().expire(cacheKey, minutes > 0 ? minutes : 1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void deleteAttendWxSubscribe(String attendReleaseId) {
        final String cacheKey = CacheBook.WEI_XIN_SUBSCRIBE + attendReleaseId;
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(cacheKey);
    }
}
