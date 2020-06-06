package top.zbeboy.zone.hystrix.platform;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.feign.platform.RoleService;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.role.RoleAddVo;
import top.zbeboy.zone.web.vo.platform.role.RoleEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RoleHystrixClientFallbackFactory implements RoleService
{
    @Override
    public Role findById(String id) {
        return new Role();
    }

    @Override
    public Role findByRoleEnName(String roleEnName) {
        return new Role();
    }

    @Override
    public List<Role> findByUsername(String username) {
        return new ArrayList<>();
    }

    @Override
    public List<Application> findInRoleEnNamesAndApplicationPidRelation(List<String> roles, String applicationPid) {
        return new ArrayList<>();
    }

    @Override
    public List<Application> findInRoleEnNamesRelation(List<String> roles, String username) {
        return new ArrayList<>();
    }

    @Override
    public RoleBean findByRoleIdRelation(String roleId) {
        return new RoleBean();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkAddName(String username, String roleName, int collegeId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkEditName(String username, String roleName, int collegeId, String roleId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(RoleAddVo roleAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(RoleEditVo roleEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String username, String roleId) {
        return AjaxUtil.of();
    }

    @Override
    public List<TreeViewData> applicationJson(int collegeId) {
        return new ArrayList<>();
    }

    @Override
    public List<RoleApplication> roleApplicationData(String roleId) {
        return new ArrayList<>();
    }
}
