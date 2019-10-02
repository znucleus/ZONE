package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.Application;

import java.util.List;

public interface RoleService {

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
     * 检查当前用户是否有此权限
     *
     * @param role 权限
     * @return true 有 false 无
     */
    Boolean isCurrentUserInRole(String role);
}
