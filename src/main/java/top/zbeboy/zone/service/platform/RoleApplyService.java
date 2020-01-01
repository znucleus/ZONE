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
     * 查询数据
     *
     * @param username        账号
     * @param authorizeTypeId 权限类型
     * @param dataScope       数据域
     * @param dataId          数据
     * @param applyStatus     状态
     * @return 数据
     */
    Result<Record> findNormalByUsernameAndAuthorizeTypeIdAndDataScopeAndDataIdAndApplyStatus(String username, int authorizeTypeId, int dataScope, int dataId, Byte applyStatus);

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

    /**
     * 通过角色id删除
     *
     * @param roleId 角色id
     */
    void deleteByRoleId(String roleId);
}
