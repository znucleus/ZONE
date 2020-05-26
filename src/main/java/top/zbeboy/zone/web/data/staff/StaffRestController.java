package top.zbeboy.zone.web.data.staff;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.ZoneProperties;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.notify.UserNotifyService;
import top.zbeboy.zone.service.platform.CollegeRoleService;
import top.zbeboy.zone.service.platform.RoleApplyService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.service.system.SystemMailService;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.staff.StaffAddVo;
import top.zbeboy.zone.web.vo.data.staff.StaffEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class StaffRestController {

    @Resource
    private ZoneProperties ZoneProperties;

    @Resource
    private StaffService staffService;

    @Resource
    private SystemConfigureService systemConfigureService;

    @Resource
    private SystemMailService systemMailService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private UsersService usersService;

    @Resource
    private RoleService roleService;

    @Resource
    private CollegeRoleService collegeRoleService;

    @Resource
    private RoleApplyService roleApplyService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private UserNotifyService userNotifyService;

    /**
     * 检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @PostMapping("/anyone/check/staff/number")
    public ResponseEntity<Map<String, Object>> anyoneCheckStaffNumber(@RequestParam("staffNumber") String staffNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.anyoneCheckStaffNumber(staffNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验工号是否被注册
     *
     * @param staffNumber 工号
     * @return 是否被注册
     */
    @PostMapping("/users/check/staff/number")
    public ResponseEntity<Map<String, Object>> userCheckStaffNumber(@RequestParam("staffNumber") String staffNumber) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.userCheckStaffNumber(users.getUsername(), staffNumber);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教职工注册
     *
     * @param staffAddVo 教职工数据
     * @return 注册
     */
    @PostMapping("/anyone/data/register/staff")
    public ResponseEntity<Map<String, Object>> anyoneDataRegisterStaff(StaffAddVo staffAddVo, HttpSession session) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        // 手机号是否已验证
        if (!ObjectUtils.isEmpty(session.getAttribute(staffAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID))) {
            boolean isValid = (boolean) session.getAttribute(staffAddVo.getMobile() + SystemMobileConfig.MOBILE_VALID);
            if (isValid) {
                ajaxUtil = staffService.anyoneDataRegisterStaff(staffAddVo);
            } else {
                ajaxUtil.fail().msg("验证手机号失败");
            }
        } else {
            ajaxUtil.fail().msg("请重新验证手机号");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 教职工信息更新
     *
     * @param staffEditVo 数据
     * @return 成功与否
     */
    @PostMapping("/users/staff/update/school")
    public ResponseEntity<Map<String, Object>> userStaffUpdateSchool(StaffEditVo staffEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.userStaffUpdateSchool(staffEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新信息
     *
     * @param staffEditVo 数据
     * @return 更新信息
     */
    @PostMapping("/users/staff/update/info")
    public ResponseEntity<Map<String, Object>> userStaffUpdateInfo(StaffEditVo staffEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.userStaffUpdateInfo(staffEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/staff/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("realName");
        headers.add("username");
        headers.add("staffNumber");
        headers.add("email");
        headers.add("mobile");
        headers.add("idCard");
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("academicTitleName");
        headers.add("post");
        headers.add("sex");
        headers.add("birthday");
        headers.add("nationName");
        headers.add("politicalLandscapeName");
        headers.add("familyResidence");
        headers.add("enabled");
        headers.add("accountNonLocked");
        headers.add("langKey");
        headers.add("joinDate");
        headers.add("operator");

        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);

        return new ResponseEntity<>(staffService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 用户角色数据
     *
     * @param username 用户账号
     * @return 数据
     */
    @PostMapping("/web/data/staff/role/data")
    public ResponseEntity<Map<String, Object>> roleData(@RequestParam("username") String username) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Role> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(staffService.roleData(users.getUsername(), username)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 角色设置
     *
     * @param username 账号
     * @param roles    角色
     * @return success or false
     */
    @PostMapping("/web/data/staff/role/save")
    public ResponseEntity<Map<String, Object>> roleSave(@RequestParam("username") String username, @RequestParam("roles") String roles) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.roleSave(users.getUsername(), username, roles);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新状态
     *
     * @param userIds 账号
     * @param enabled 状态
     * @return 是否成功
     */
    @PostMapping("/web/data/staff/update/enabled")
    public ResponseEntity<Map<String, Object>> updateEnabled(String userIds, Byte enabled) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.updateEnabled(users.getUsername(), userIds, enabled);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新锁定
     *
     * @param userIds 账号
     * @param locked  锁定
     * @return 是否成功
     */
    @PostMapping("/web/data/staff/update/locked")
    public ResponseEntity<Map<String, Object>> updateLocked(String userIds, Byte locked) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.updateLocked(users.getUsername(), userIds, locked);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新密码
     *
     * @param username 账号
     * @return success or fail
     */
    @PostMapping("/web/data/staff/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestParam("username") String username) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.updatePassword(users.getUsername(), username);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除无角色关联的用户
     *
     * @param userIds 用户账号
     * @return true 成功 false 失败
     */
    @PostMapping("/web/data/staff/delete")
    public ResponseEntity<Map<String, Object>> delete(String userIds) {
        Users users = SessionUtil.getUserFromSession();
        AjaxUtil<Map<String, Object>> ajaxUtil = staffService.delete(users.getUsername(), userIds);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
