package top.zbeboy.zone.service.notify;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.SystemNotify;
import top.zbeboy.zone.domain.tables.records.SystemNotifyRecord;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface SystemNotifyService {

    /**
     * 查询有效数据
     *
     * @return 数据
     */
    Result<SystemNotifyRecord> findByEffective();

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
     * 保存
     *
     * @param systemNotify 数据
     */
    void save(SystemNotify systemNotify);

    /**
     * 更新
     *
     * @param systemNotify 数据
     */
    void update(SystemNotify systemNotify);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
