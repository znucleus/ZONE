package top.zbeboy.zone.service.platform;

import top.zbeboy.zone.domain.tables.pojos.UsersType;

public interface UsersTypeService {

    /**
     * 根据用户类型ID查询
     *
     * @param usersTypeId 类型ID
     * @return 用户类型
     */
    UsersType findById(int usersTypeId);

    /**
     * 根据用户类型查询
     *
     * @param usersTypeName 类型名
     * @return 用户类型
     */
    UsersType findByUsersTypeName(String usersTypeName);
}
