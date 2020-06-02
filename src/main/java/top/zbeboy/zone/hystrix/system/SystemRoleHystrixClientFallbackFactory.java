package top.zbeboy.zone.hystrix.system;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.feign.system.SystemRoleService;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SystemRoleHystrixClientFallbackFactory implements SystemRoleService {
    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> checkName(String roleName, String roleId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> roleUpdate(String roleId, String roleName, String applicationIds) {
        return AjaxUtil.of();
    }

    @Override
    public List<RoleApplication> roleApplicationData(String roleId) {
        return new ArrayList<>();
    }

    @Override
    public List<TreeViewData> applicationJson() {
        return new ArrayList<>();
    }
}
