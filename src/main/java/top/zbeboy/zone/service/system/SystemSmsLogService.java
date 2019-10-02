package top.zbeboy.zone.service.system;

import top.zbeboy.zone.domain.tables.pojos.SystemSmsLog;

public interface SystemSmsLogService {

    /**
     * 保存
     *
     * @param systemSmsLog 数据
     */
    void save(SystemSmsLog systemSmsLog);
}
