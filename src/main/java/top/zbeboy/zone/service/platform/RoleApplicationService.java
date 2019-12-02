package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.RoleApplication;

public interface RoleApplicationService {

    /**
     * 保存
     *
     * @param roleApplication 角色与应用
     */
    void save(RoleApplication roleApplication);

    /**
     * 通过应用id删除
     *
     * @param applicationId 应用id
     */
    void deleteByApplicationId(String applicationId);
}
