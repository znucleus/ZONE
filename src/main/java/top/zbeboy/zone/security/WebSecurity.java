package top.zbeboy.zone.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Application;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Spring security 路径权限控制器
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
public class WebSecurity {

    @Resource
    private RoleService roleService;

    /**
     * 权限控制检查
     *
     * @param authentication 权限对象
     * @param request        请求
     * @return true可访问 false 不可访问该路径
     */
    public boolean check(Authentication authentication, HttpServletRequest request) {
        Users users = SessionUtil.getUserFromSession();
        if (Objects.isNull(users)) {
            return false;
        }

        String uri = StringUtils.trim(request.getRequestURI());

        // 欢迎页
        if (uri.endsWith(Workbook.WEB_BACKSTAGE)) {
            return true;
        }

        boolean hasRole = false;

        List<String> roles = new ArrayList<>();
        Optional<List<Role>> optionalRoles = roleService.findByUsername(users.getUsername());
        optionalRoles.ifPresent(roleList -> roleList.forEach(r -> roles.add(r.getRoleEnName())));

        if (!roles.isEmpty()) {
            Optional<List<Application>> optionalApplications = roleService.findInRoleEnNamesRelation(roles, users.getUsername());
            if(optionalApplications.isPresent()){
                List<Application> applications = optionalApplications.get();
                for (Application application : applications) {
                    if (uri.endsWith(application.getApplicationUrl())) {
                        hasRole = true;
                        break;
                    }

                    if (StringUtils.isNotBlank(application.getApplicationDataUrlStartWith()) &&
                            uri.startsWith(application.getApplicationDataUrlStartWith())) {
                        hasRole = true;
                        break;
                    }
                }
            }

        }

        return hasRole;
    }
}
