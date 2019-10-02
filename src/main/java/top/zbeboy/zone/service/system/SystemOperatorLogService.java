package top.zbeboy.zone.service.system;

import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;

public interface SystemOperatorLogService {

    /**
     * 保存
     *
     * @param systemOperatorLog 数据
     */
    void save(SystemOperatorLog systemOperatorLog);
}
