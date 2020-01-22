package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.RoleApplication;

import java.util.List;

public interface RoleApplicationService {

    /**
     * 根据角色id查询
     *
     * @param roleId 角色id
     * @return 数据
     */
    List<RoleApplication> findByRoleId(String roleId);

    /**
     * 保存
     *
     * @param roleApplication 角色与应用
     */
    void save(RoleApplication roleApplication);

    /**
     * 批量保存或更新角色
     *
     * @param roleApplicationList 数据
     */
    void batchSave(List<RoleApplication> roleApplicationList);

    /**
     * 通过应用id删除
     *
     * @param applicationId 应用id
     */
    void deleteByApplicationId(String applicationId);

    /**
     * 通过角色id删除
     *
     * @param roleId 角色id
     */
    void deleteByRoleId(String roleId);
}
