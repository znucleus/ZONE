package top.zbeboy.zone.web.register.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class RegisterConditionCommon {

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    /**
     * 是否可操作
     *
     * @return true or false
     */
    public boolean epidemicOperator() {
        return roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name());
    }

    /**
     * 是否可统计
     *
     * @return true or false
     */
    public boolean epidemicReview() {
        boolean canOperator = false;
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            canOperator = true;
        } else {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                canOperator = StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName());
            }
        }
        return canOperator;
    }
}
