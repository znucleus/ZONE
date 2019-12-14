package top.zbeboy.zone.service.platform;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jooq.*;
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
import top.zbeboy.zone.domain.tables.daos.RoleDao;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.service.plugin.PaginationPlugin;
import top.zbeboy.zone.service.util.SQLQueryUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.zbeboy.zone.domain.Tables.*;

@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleServiceImpl implements RoleService, PaginationPlugin<DataTablesUtil> {

    private final DSLContext create;

    @Resource
    private RoleDao roleDao;

    @Autowired
    RoleServiceImpl(DSLContext dslContext) {
        create = dslContext;
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public Role findByRoleEnName(String roleEnName) {
        return roleDao.fetchOneByRoleEnName(roleEnName);
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
    public Result<Record> findByRoleNameAndRoleTypeNeRoleId(String roleName, int roleType, String roleId) {
        return create.select()
                .from(ROLE)
                .where(ROLE.ROLE_NAME.eq(roleName).and(ROLE.ROLE_TYPE.eq(roleType)).and(ROLE.ROLE_ID.ne(roleId)))
                .fetch();
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

    @Override
    public Result<Record> findAllByPage(DataTablesUtil dataTablesUtil) {
        return queryAllByPage(create, ROLE, dataTablesUtil, false);
    }

    @Override
    public int countAll(DataTablesUtil dataTablesUtil) {
        return countAll(create, ROLE, dataTablesUtil, true);
    }

    @Override
    public int countByCondition(DataTablesUtil dataTablesUtil) {
        return countAll(create, ROLE, dataTablesUtil, false);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public Condition searchCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String roleName = StringUtils.trim(search.getString("roleName"));
            if (StringUtils.isNotBlank(roleName)) {
                a = ROLE.ROLE_NAME.like(SQLQueryUtil.likeAllParam(roleName));
            }
        }
        return a;
    }

    @Override
    public void sortCondition(SelectConnectByStep<Record> step, DataTablesUtil paginationUtil) {
        String orderColumnName = paginationUtil.getOrderColumnName();
        String orderDir = paginationUtil.getOrderDir();
        boolean isAsc = StringUtils.equalsIgnoreCase("asc", orderDir);
        SortField[] sortField = null;
        if (StringUtils.isNotBlank(orderColumnName)) {
            if (StringUtils.equals("roleName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE.ROLE_NAME.asc();
                    sortField[1] = ROLE.ROLE_ID.asc();
                } else {
                    sortField[0] = ROLE.ROLE_NAME.desc();
                    sortField[1] = ROLE.ROLE_ID.desc();
                }
            }

            if (StringUtils.equals("roleEnName", orderColumnName)) {
                sortField = new SortField[2];
                if (isAsc) {
                    sortField[0] = ROLE.ROLE_EN_NAME.asc();
                    sortField[1] = ROLE.ROLE_ID.asc();
                } else {
                    sortField[0] = ROLE.ROLE_EN_NAME.desc();
                    sortField[1] = ROLE.ROLE_ID.desc();
                }
            }
        }
        sortToFinish(step, sortField);
    }

    @Override
    public Condition extraCondition(DataTablesUtil paginationUtil) {
        Condition a = null;
        JSONObject search = paginationUtil.getSearch();
        if (Objects.nonNull(search)) {
            String roleType = StringUtils.trim(search.getString("roleType"));
            if (StringUtils.isNotBlank(roleType)) {
                int roleTypeInt = NumberUtils.toInt(roleType);
                a = ROLE.ROLE_TYPE.eq(roleTypeInt);
            }
        }
        return a;
    }
}
