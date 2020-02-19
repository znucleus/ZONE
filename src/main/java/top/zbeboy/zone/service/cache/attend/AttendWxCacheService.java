package top.zbeboy.zone.service.cache.attend;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.AttendRelease;

import java.io.IOException;

public interface AttendWxCacheService {

    /**
     * 保存签到订阅
     *
     * @param records 发布数据
     */
    void saveAttendWxSubscribe(Result<Record> records);

    /**
     * 下发签到订阅
     *
     * @param cacheKey 缓存key
     */
    void sendAttendWxSubscribe(String cacheKey) throws IOException;

    /**
     * 更新下发签到订单
     *
     * @param attendRelease 发布数据
     */
    void updateAttendWxSubscribe(AttendRelease attendRelease);

    /**
     * 删除单个订阅
     *
     * @param attendReleaseId 发布id
     */
    void deleteAttendWxSubscribe(String attendReleaseId);
}
