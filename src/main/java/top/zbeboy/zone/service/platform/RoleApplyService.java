package top.zbeboy.zone.service.platform;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

public interface RoleApplyService {

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
     * @param dataTablesUtil 工具类
     * @return 总数
     */
    int countAll(DataTablesUtil dataTablesUtil);

    /**
     * 根据条件查询总数
     *
     * @return 条件查询总数
     */
    int countByCondition(DataTablesUtil dataTablesUtil);
}
