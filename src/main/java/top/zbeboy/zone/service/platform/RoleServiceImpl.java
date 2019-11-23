package top.zbeboy.zone.service.platform;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record9;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.zbeboy.zone.config.CacheBook;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.*;

@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final DSLContext create;

    @Autowired
    RoleServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Cacheable(cacheNames = CacheBook.ROLES_APPLICATION, key = "#username")
    @Override
    public List<Application> findInRoleEnNamesRelation(List<String> roleEnName, String username) {
        List<Application> applicationList = new ArrayList<>();
        Result<Record> records = create.select()
                .from(ROLE)
                .join(ROLE_APPLICATION)
                .on(ROLE.ROLE_ID.eq(ROLE_APPLICATION.ROLE_ID))
                .join(APPLICATION)
                .on(ROLE_APPLICATION.APPLICATION_ID.eq(APPLICATION.APPLICATION_ID))
                .where(ROLE.ROLE_EN_NAME.in(roleEnName))
                .fetch();
        if (records.isNotEmpty()) {
            applicationList = records.into(Application.class);
        }
        return applicationList;
    }

    @Override
    public List<Application> findInRoleEnNamesAndApplicationPidRelation(List<String> roleEnName, String applicationPid) {
        List<Application> applicationList = new ArrayList<>();
        Result<Record9<String, String, String, String, Integer, String, String, String, String>> records = create.selectDistinct(
                APPLICATION.APPLICATION_ID,
                APPLICATION.APPLICATION_PID,
                APPLICATION.APPLICATION_NAME,
                APPLICATION.APPLICATION_URL,
                APPLICATION.APPLICATION_SORT,
                APPLICATION.APPLICATION_DATA_URL_START_WITH,
                APPLICATION.ICON,
                APPLICATION.APPLICATION_CODE,
                APPLICATION.APPLICATION_EN_NAME)
                .from(ROLE)
                .join(ROLE_APPLICATION)
                .on(ROLE.ROLE_ID.eq(ROLE_APPLICATION.ROLE_ID))
                .join(APPLICATION)
                .on(ROLE_APPLICATION.APPLICATION_ID.eq(APPLICATION.APPLICATION_ID))
                .where(ROLE.ROLE_EN_NAME.in(roleEnName).and(APPLICATION.APPLICATION_PID.eq(applicationPid)))
                .orderBy(APPLICATION.APPLICATION_SORT)
                .fetch();
        if (records.isNotEmpty()) {
            applicationList = records.into(Application.class);
        }
        return applicationList;
    }

    @Cacheable(cacheNames = CacheBook.ROLES, key = "#username")
    @Override
    public List<Role> findByUsername(String username) {
        List<Role> roleList = new ArrayList<>();
        Result<Record> records = create.select()
                .from(AUTHORITIES)
                .leftJoin(ROLE)
                .on(AUTHORITIES.AUTHORITY.eq(ROLE.ROLE_EN_NAME))
                .where(AUTHORITIES.USERNAME.eq(username))
                .fetch();
        if (records.isNotEmpty()) {
            roleList = records.into(Role.class);
        }
        return roleList;
    }

    @Override
    public Boolean isCurrentUserInRole(String role) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.nonNull(authentication)) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(role));
            }
        }
        return false;
    }
}
