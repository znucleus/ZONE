package top.zbeboy.zone.service.system;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface SystemConfigureService {

    /**
     * 通过Key查询
     *
     * @param dataKey key
     * @return 数据 value
     */
    SystemConfigure findByDataKey(String dataKey);

    /**
     * 分页查询
     *
     * @param dataTablesUtil 工具类
     * @return 分页数据
     */
    Result<Record> findAllByPage(DataTablesUtil dataTablesUtil);

    /**
     * 应用 总数
     *
     * @return 总数
     */
    int countAll();

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);

    /**
     * 更新
     *
     * @param systemConfigure 数据
     */
    void update(SystemConfigure systemConfigure);
}
