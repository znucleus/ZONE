package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zbase.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.hystrix.system.SystemRoleHystrixClientFallbackFactory;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = SystemRoleHystrixClientFallbackFactory.class)
public interface SystemRoleService {

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/role/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 更新时检验角色名
     *
     * @param roleName 角色名
     * @param roleId   角色id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/system/role/check/edit/name")
    AjaxUtil<Map<String, Object>> checkName(@RequestParam("roleName") String roleName, @RequestParam("roleId") String roleId);

    /**
     * 更新角色
     *
     * @param roleId         角色id
     * @param roleName       角色名
     * @param applicationIds 应用ids
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/system/role/update")
    AjaxUtil<Map<String, Object>> roleUpdate(@RequestParam("roleId") String roleId, @RequestParam("roleName") String roleName, @RequestParam(value = "applicationIds", required = false) String applicationIds);

    /**
     * 获取角色id 下的 应用id
     *
     * @param roleId 角色id
     * @return 应用
     */
    @PostMapping("/base/system/role/application/data")
    List<RoleApplication> roleApplicationData(@RequestParam("roleId") String roleId);

    /**
     * 数据json
     *
     * @return json
     */
    @GetMapping("/base/system/role/application/json")
    List<TreeViewData> applicationJson();
}
