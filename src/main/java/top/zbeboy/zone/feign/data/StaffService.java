package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.hystrix.data.ScienceHystrixClientFallbackFactory;
import top.zbeboy.zone.hystrix.data.StaffHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;
import top.zbeboy.zone.web.vo.data.staff.StaffEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = StaffHystrixClientFallbackFactory.class)
public interface StaffService {

    /**
     * 获取教职工
     *
     * @param username 教职工账号
     * @return 教职工数据
     */
    @GetMapping("/base/staff/username/{username}")
    StaffBean findByUsername(@PathVariable("username") String username);

    /**
     * 获取教职工
     *
     * @param id 教职工主键
     * @return 教职工数据
     */
    @GetMapping("/base/staff/relation/{id}")
    StaffBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取教职工
     *
     * @param username 教职工账号
     * @return 教职工数据
     */
    @GetMapping("/base/staff/username/relation/{username}")
    StaffBean findByUsernameRelation(@PathVariable("username") String username);

    /**
     * 获取教职工
     *
     * @param param 教职工账号或工号
     * @return 教职工数据
     */
    @GetMapping("/base/staff/username/or/staff_number/{param}")
    StaffBean findByUsernameOrStaffNumberRelation(@PathVariable("param") String param);

    /**
     * 根据系id获取正常教职工
     *
     * @param departmentId 教职工主键
     * @return 教职工数据
     */
    @GetMapping("/base/staff/normal/department_id/relation/{departmentId}")
    List<StaffBean> findNormalByDepartmentIdRelation(@PathVariable("departmentId") int departmentId);

    /**
     * 根据系id获取正常教职工
     *
     * @param authority 权限
     * @param collegeId 院id
     * @return 教职工数据
     */
    @GetMapping("/base/staff/authority/college_id/{authority}/{collegeId}")
    List<Users> findByAuthorityAndCollegeId(@PathVariable("authority") String authority, @PathVariable("collegeId") int collegeId);

    /**
     * 检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @PostMapping("/base/anyone/check/staff/number")
    AjaxUtil<Map<String, Object>> anyoneCheckStaffNumber(@RequestParam("staffNumber") String staffNumber);

    /**
     * 更新时检验工号是否被注册
     *
     * @param username    当前用户
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @PostMapping("/base/users/check/staff/number")
    AjaxUtil<Map<String, Object>> userCheckStaffNumber(@RequestParam("username") String username, @RequestParam("staffNumber") String staffNumber);

    /**
     * 教职工注册
     *
     * @param staffAddVo 教职工数据
     * @return 注册
     */
    @PostMapping("/base/anyone/data/register/staff")
    AjaxUtil<Map<String, Object>> anyoneDataRegisterStaff(@RequestBody StaffAddVo staffAddVo);

    /**
     * 教职工信息更新
     *
     * @param staffEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/base/staff/update/school")
    AjaxUtil<Map<String, Object>> userStaffUpdateSchool(@RequestBody StaffEditVo staffEditVo);

    /**
     * 更新信息
     *
     * @param staffEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/base/staff/update/info")
    AjaxUtil<Map<String, Object>> userStaffUpdateInfo(@RequestBody StaffEditVo staffEditVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/staff/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 用户角色数据
     *
     * @param username       当前用户账号
     * @param targetUsername 目标用户
     * @return 数据
     */
    @PostMapping("/base/data/staff/role/data")
    List<Role> roleData(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername);


    /**
     * 角色设置
     *
     * @param username       当前用户账号
     * @param targetUsername 目标账号
     * @param roles          角色
     * @return success or false
     */
    @PostMapping("/base/data/staff/role/save")
    AjaxUtil<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername, @RequestParam("roles") String roles);

    /**
     * 更新状态
     *
     * @param userIds 账号
     * @param enabled 状态
     * @return 是否成功
     */
    @PostMapping("/base/data/staff/update/enabled")
    AjaxUtil<Map<String, Object>> updateEnabled(@RequestParam("username") String username, @RequestParam(value = "userIds", required = false) String userIds, @RequestParam("enabled") Byte enabled);

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/base/data/staff/update/locked")
    AjaxUtil<Map<String, Object>> updateLocked(@RequestParam("username") String username, @RequestParam(value = "userIds", required = false) String userIds, @RequestParam("locked") Byte locked);

    /**
     * 更新密码
     *
     * @param username       当前用户账号
     * @param targetUsername 目标用户
     * @return success or fail
     */
    @PostMapping("/base/data/staff/update/password")
    AjaxUtil<Map<String, Object>> updatePassword(@RequestParam("username") String username, @RequestParam("targetUsername") String targetUsername);

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/base/data/staff/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("username") String username, @RequestParam(value = "userIds", required = false) String userIds);
}
