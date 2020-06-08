package top.zbeboy.zone.feign.platform;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Authorities;
import top.zbeboy.zone.domain.tables.pojos.AuthorizeType;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.RoleApply;
import top.zbeboy.zone.hystrix.platform.AuthorizeHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.authorize.AuthorizeAddVo;
import top.zbeboy.zone.web.vo.platform.authorize.AuthorizeEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = AuthorizeHystrixClientFallbackFactory.class)
public interface AuthorizeService {

    /**
     * 获取角色申请信息
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/base/platform/authorize/role_apply/{id}")
    RoleApply findRoleApplyById(@PathVariable("id") String id);

    /**
     * 获取角色申请信息
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/base/platform/authorize/role_apply_relation/{id}")
    RoleApplyBean findRoleApplyByIdRelation(@PathVariable("id") String id);

    /**
     * 通过账号和权限查询
     *
     * @param username    账号
     * @param authorities 权限
     * @return 数据
     */
    @PostMapping("/base/platform/authorize/username_and_in_authorities")
    List<Authorities> findByUsernameAndInAuthorities(@RequestParam("username") String username, @RequestBody List<String> authorities);

    /**
     * 根据用户账号查询权限
     *
     * @param username 账号
     * @return 用户权限
     */
    @GetMapping("/base/platform/authorize_username/{username}")
    List<Authorities> findByUsername(@PathVariable("username") String username);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/platform/authorize/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 根据全部权限类型
     *
     * @return 数据
     */
    @GetMapping("/base/platform/authorize_type")
    List<AuthorizeType> authorizeTypeData();

    /**
     * 根据全部权限类型
     *
     * @param collegeId 院id
     * @return 数据
     */
    @GetMapping("/base/platform/authorize/college_role_college_id/{id}")
    List<Role> findCollegeRoleByCollegeIdRelation(@PathVariable("id") int collegeId);

    /**
     * 检验账号是否符合规则
     *
     * @param username       账号
     * @param targetUsername 目标账号
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/platform/authorize/check/username")
    AjaxUtil<Map<String, Object>> checkAddUsername(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername, @RequestParam("collegeId") int collegeId);

    /**
     * 保存
     *
     * @param authorizeAddVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/platform/authorize/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody AuthorizeAddVo authorizeAddVo);

    /**
     * 更新
     *
     * @param authorizeEditVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/platform/authorize/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody AuthorizeEditVo authorizeEditVo);

    /**
     * 编辑页面进入前检验
     *
     * @param username    当前用户账号
     * @param roleApplyId id
     * @return 条件
     */
    @PostMapping("/base/platform/authorize/check/edit/access")
    AjaxUtil<Map<String, Object>> checkEditAccess(@RequestParam("username") String username, @RequestParam("roleApplyId") String roleApplyId);

    /**
     * 删除
     *
     * @param username    当前用户账号
     * @param roleApplyId 角色id
     * @return true成功
     */
    @PostMapping("/base/platform/authorize/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("username") String username, @RequestParam("roleApplyId") String roleApplyId);

    /**
     * 更新状态
     *
     * @param username    当前用户
     * @param roleApplyId id
     * @param applyStatus 状态
     * @return true or false
     */
    @PostMapping("/base/platform/authorize/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam("username") String username, @RequestParam("roleApplyId") String roleApplyId, @RequestParam("applyStatus") Byte applyStatus, @RequestParam(value = "refuse", required = false) String refuse);
}
