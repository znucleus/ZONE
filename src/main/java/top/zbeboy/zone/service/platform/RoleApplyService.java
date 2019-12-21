package top.zbeboy.zone.service.platform;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.RoleApply;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.Optional;

public interface RoleApplyService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 数据
     */
    RoleApply findById(String id);

    /**
     * 通过主键关联查询
     *
     * @param id 主键
     * @return 数据
     */
    Optional<Record> findByIdRelation(String id);

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

    /**
     * 保存
     *
     * @param roleApply 数据
     */
    void save(RoleApply roleApply);

    /**
     * 更新
     *
     * @param roleApply 数据
     */
    void update(RoleApply roleApply);

    /**
     * 删除
     *
     * @param id 主键
     */
    void deleteById(String id);
}
