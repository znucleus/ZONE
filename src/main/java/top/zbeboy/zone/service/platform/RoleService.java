package top.zbeboy.zone.service.platform;

import org.jooq.Record;
import org.jooq.Result;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.List;

public interface RoleService {

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return 角色
     */
    Role findById(String id);

    /**
     * 通过角色名查询
     *
     * @param roleEnName 角色名
     * @return 角色
     */
    Role findByRoleEnName(String roleEnName);

    /**
     * 通过角色英文名查询菜单
     * 缓存:是
     *
     * @param roleEnName 英文名
     * @param username   用户账号 用于缓存使用
     * @return 全部菜单应用
     */
    List<Application> findInRoleEnNamesRelation(List<String> roleEnName, String username);

    /**
     * 通过角色英文名与应用父id查询菜单
     * 缓存:是
     *
     * @param roleEnName     英文名
     * @param applicationPid 应用父id
     * @return 全部菜单应用
     */
    List<Application> findInRoleEnNamesAndApplicationPidRelation(List<String> roleEnName, String applicationPid);

    /**
     * 通过账号查询角色
     *
     * @param username 账号
     * @return 角色数据
     */
    List<Role> findByUsername(String username);

    /**
     * 通过角色名与角色类型查询 注：不等于角色id
     *
     * @param roleName 角色名
     * @param roleType 角色类型
     * @param roleId   角色id
     * @return 数据
     */
    Result<Record> findByRoleNameAndRoleTypeNeRoleId(String roleName, int roleType, String roleId);

    /**
     * 检查当前用户是否有此权限
     *
     * @param role 权限
     * @return true 有 false 无
     */
    Boolean isCurrentUserInRole(String role);

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
     * 更新
     *
     * @param role 角色
     */
    void update(Role role);

    /**
     * 保存
     *
     * @param role 角色
     */
    void save(Role role);
}
