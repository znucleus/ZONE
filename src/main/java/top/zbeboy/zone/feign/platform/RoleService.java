package top.zbeboy.zone.feign.platform;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.Application;
import top.zbeboy.zbase.domain.tables.pojos.Role;
import top.zbeboy.zbase.domain.tables.pojos.RoleApplication;
import top.zbeboy.zone.hystrix.platform.RoleHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.role.RoleAddVo;
import top.zbeboy.zbase.vo.platform.role.RoleEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = RoleHystrixClientFallbackFactory.class)
public interface RoleService {

    /**
     * 获取角色
     *
     * @param id 角色主键
     * @return 数据
     */
    @GetMapping("/base/platform/role/{id}")
    Role findById(@PathVariable("id") String id);

    /**
     * 获取角色
     *
     * @param roleEnName 角色英文名
     * @return 数据
     */
    @GetMapping("/base/platform/role_role_en_name/{roleEnName}")
    Role findByRoleEnName(@PathVariable("roleEnName") String roleEnName);

    /**
     * 获取角色
     *
     * @param username 账号
     * @return 数据
     */
    @GetMapping("/base/platform/role_username/{username}")
    List<Role> findByUsername(@PathVariable("username") String username);

    /**
     * 根据角色英文名与父id查询应用
     *
     * @param roles          角色
     * @param applicationPid 父id
     * @return 数据
     */
    @PostMapping("/base/platform/role_in_role_en_names_and_application_pid_relation")
    List<Application> findInRoleEnNamesAndApplicationPidRelation(@RequestBody List<String> roles, @RequestParam("applicationPid") String applicationPid);

    /**
     * 根据角色英文名与账号查询应用
     *
     * @param roles    角色
     * @param username 账号
     * @return 数据
     */
    @PostMapping("/base/platform/role_role_en_names_relation")
    List<Application> findInRoleEnNamesRelation(@RequestBody List<String> roles, @RequestParam("username") String username);

    /**
     * 院与角色关联数据
     *
     * @param roleId 角色id
     * @return 数据
     */
    @GetMapping("/base/platform/role/college_role_role_id/{roleId}")
    RoleBean findCollegeRoleByRoleIdRelation(@PathVariable("roleId") String roleId);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/platform/role/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/platform/role/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("username") String username, @RequestParam("roleName") String roleName, @RequestParam(value = "collegeId", required = false) int collegeId);

    /**
     * 更新时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @param roleId    角色id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/platform/role/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("username") String username, @RequestParam("roleName") String roleName, @RequestParam(value = "collegeId", required = false) int collegeId, @RequestParam("roleId") String roleId);

    /**
     * 保存角色
     *
     * @param roleAddVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/platform/role/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody RoleAddVo roleAddVo);

    /**
     * 更新
     *
     * @param roleEditVo 数据
     * @return true 更新成功 false 更新失败
     */
    @PostMapping("/base/platform/role/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody RoleEditVo roleEditVo);

    /**
     * 删除角色
     *
     * @param roleId 角色id
     * @return true成功
     */
    @PostMapping("/base/platform/role/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("username") String username, @RequestParam("roleId") String roleId);

    /**
     * 数据json
     *
     * @param collegeId 院id
     * @return json
     */
    @GetMapping("/base/platform/role/application/json")
    List<TreeViewData> applicationJson(@RequestParam("collegeId") int collegeId);

    /**
     * 获取角色id 下的 应用id
     *
     * @param roleId 角色id
     * @return 应用
     */
    @PostMapping("/base/platform/role/application/data")
    List<RoleApplication> roleApplicationData(@RequestParam("roleId") String roleId);
}
