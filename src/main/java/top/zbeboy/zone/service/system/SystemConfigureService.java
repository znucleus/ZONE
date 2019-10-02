package top.zbeboy.zone.service.system;

import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;

public interface SystemConfigureService {

    /**
     * 通过Key查询
     *
     * @param dataKey key
     * @return 数据 value
     */
    SystemConfigure findByDataKey(String dataKey);
}
