package top.zbeboy.zone.service.notify;

import org.jooq.Result;
import top.zbeboy.zone.domain.tables.records.SystemNotifyRecord;

public interface SystemNotifyService {

    /**
     * 查询有效数据
     *
     * @return 数据
     */
    Result<SystemNotifyRecord> findByEffective();
}
