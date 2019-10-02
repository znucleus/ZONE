package top.zbeboy.zone.service.system;

import top.zbeboy.zone.domain.tables.pojos.SystemMailboxLog;

public interface SystemMailboxLogService {

    /**
     * 保存
     *
     * @param systemMailboxLog 数据
     */
    void save(SystemMailboxLog systemMailboxLog);
}
