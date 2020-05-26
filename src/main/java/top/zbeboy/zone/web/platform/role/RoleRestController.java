package top.zbeboy.zone.web.platform.role;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.CollegeApplicationService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.*;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.service.util.RandomUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.plugin.treeview.TreeViewData;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.role.RoleAddVo;
import top.zbeboy.zone.web.vo.platform.role.RoleEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class RoleRestController {

    @Resource
    private CollegeRoleService collegeRoleService;

    @Resource
    private CollegeApplicationService collegeApplicationService;

    @Resource
    private RoleService roleService;

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private RoleApplicationService roleApplicationService;

    @Resource
    private AuthoritiesService authoritiesService;

    @Resource
    private RoleApplyService roleApplyService;

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/platform/role/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("roleName");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("roleEnName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        JSONObject otherCondition = dataTablesUtil.getSearch();
        otherCondition.put("roleType", 2);
        Result<Record> records = collegeRoleService.findAllByPage(dataTablesUtil);
        List<RoleBean> roleBean = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            roleBean = records.into(RoleBean.class);
        }
        dataTablesUtil.setData(roleBean);
        dataTablesUtil.setiTotalRecords(collegeRoleService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(collegeRoleService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/role/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("roleName") String roleName, int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(roleName);

        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                    if(record.isPresent()){
                        collegeId = record.get().into(College.class).getCollegeId();
                    }
                }
            }

            if (collegeId > 0) {
                Result<Record> records = collegeRoleService.findByRoleNameAndCollegeId(param, collegeId);

                if (records.isEmpty()) {
                    ajaxUtil.success().msg("角色名不重复");
                } else {
                    ajaxUtil.fail().msg("角色名重复");
                }
            } else {
                ajaxUtil.fail().msg("未查询到用户院ID或未选择院");
            }
        } else {
            ajaxUtil.fail().msg("您无权限进行操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新时检验角色是否重复
     *
     * @param roleName  角色名
     * @param collegeId 院id
     * @param roleId    角色id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/platform/role/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("roleName") String roleName, int collegeId,
                                                             @RequestParam("roleId") String roleId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(roleName);
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                    if(record.isPresent()){
                        collegeId = record.get().into(College.class).getCollegeId();
                    }
                }
            } else {
                ajaxUtil.fail().msg("未查询到当前用户类型");
            }

            if (collegeId > 0) {
                Result<Record> records = collegeRoleService.findByRoleNameAndCollegeIdNeRoleId(param, collegeId, roleId);
                if (records.isEmpty()) {
                    ajaxUtil.success().msg("角色名不重复");
                } else {
                    ajaxUtil.fail().msg("角色名重复");
                }
            } else {
                ajaxUtil.fail().msg("未查询到用户院ID或未选择院");
            }
        } else {
            ajaxUtil.fail().msg("您无权进行操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存角色
     *
     * @param roleAddVo     数据
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/platform/role/save")
    public ResponseEntity<Map<String, Object>> save(@Valid RoleAddVo roleAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                    roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                Role role = new Role();
                role.setRoleId(UUIDUtil.getUUID());
                role.setRoleName(StringUtils.deleteWhitespace(roleAddVo.getRoleName()));
                role.setRoleEnName(Workbook.ROLE_PREFIX + RandomUtil.generateRoleEnName().toUpperCase());
                role.setRoleType(2);
                roleService.save(role);

                List<String> ids = SmallPropsUtil.StringIdsToStringList(roleAddVo.getApplicationIds());
                List<RoleApplication> roleApplications = new ArrayList<>();
                ids.forEach(id -> roleApplications.add(new RoleApplication(role.getRoleId(), id)));
                roleApplicationService.batchSave(roleApplications);

                int collegeId = roleAddVo.getCollegeId();
                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                            collegeId = bean.getCollegeId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if (record.isPresent()) {
                            collegeId = record.get().into(College.class).getCollegeId();
                        }
                    }
                }

                if (collegeId > 0) {
                    CollegeRole collegeRole = new CollegeRole(role.getRoleId(), collegeId);
                    collegeRoleService.save(collegeRole);

                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到用户院ID或未选择院");
                }
            } else {
                ajaxUtil.fail().msg("您无权限进行操作");
            }

        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param roleEditVo    数据
     * @param bindingResult 检验
     * @return true 更新成功 false 更新失败
     */
    @PostMapping("/web/platform/role/update")
    public ResponseEntity<Map<String, Object>> update(@Valid RoleEditVo roleEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                    roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
                Role role = roleService.findById(roleEditVo.getRoleId());
                if (Objects.nonNull(role)) {
                    role.setRoleName(StringUtils.deleteWhitespace(roleEditVo.getRoleName()));
                    roleService.update(role);

                    // 用户可能同时更改菜单
                    roleApplicationService.deleteByRoleId(role.getRoleId());
                    // 不论是系统角色还是其它角色，不应该能变动角色到其它院下，造成跨院问题
                    List<String> ids = SmallPropsUtil.StringIdsToStringList(roleEditVo.getApplicationIds());
                    List<RoleApplication> roleApplications = new ArrayList<>();
                    ids.forEach(id -> roleApplications.add(new RoleApplication(role.getRoleId(), id)));
                    roleApplicationService.batchSave(roleApplications);

                    ajaxUtil.success().msg("更新成功");
                } else {
                    ajaxUtil.fail().msg("根据角色ID未查询到角色数据");
                }
            } else {
                ajaxUtil.fail().msg("您无权限进行操作");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色id
     * @return true成功
     */
    @PostMapping("/web/platform/role/delete")
    public ResponseEntity<Map<String, Object>> delete(@RequestParam("roleId") String roleId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            Role role = roleService.findById(roleId);
            if (Objects.nonNull(role)) {
                collegeRoleService.deleteByRoleId(roleId);
                roleApplicationService.deleteByRoleId(roleId);
                roleApplyService.deleteByRoleId(roleId);
                authoritiesService.deleteByAuthorities(role.getRoleEnName());
                roleService.deleteById(roleId);
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("根据角色ID未查询到角色数据");
            }
        } else {
            ajaxUtil.fail().msg("您无权限进行操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据json
     *
     * @param collegeId 院id
     * @return json
     */
    @GetMapping("/web/platform/role/application/json")
    public ResponseEntity<Map<String, Object>> applicationJson(@RequestParam("collegeId") int collegeId, Byte isSee) {
        AjaxUtil<TreeViewData> ajaxUtil = AjaxUtil.of();
        ajaxUtil.success().list(toJson("0", collegeId)).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 获取角色id 下的 应用id
     *
     * @param roleId 角色id
     * @return 应用
     */
    @PostMapping("/web/platform/role/application/data")
    public ResponseEntity<Map<String, Object>> roleApplicationData(@RequestParam("roleId") String roleId) {
        AjaxUtil<RoleApplication> ajaxUtil = AjaxUtil.of();
        List<RoleApplication> roleApplications = roleApplicationService.findByRoleId(roleId);
        ajaxUtil.success().list(roleApplications).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据转换为json
     *
     * @param id 父id
     * @return json 数据
     */
    private List<TreeViewData> toJson(String id, int collegeId) {
        List<Application> applications = collegeApplicationService.findByPidAndCollegeId(id, collegeId);
        List<TreeViewData> trees = new ArrayList<>();
        if (Objects.nonNull(applications)) {
            applications.forEach(a -> trees.add(new TreeViewData(a.getApplicationName(), toJson(a.getApplicationId(), collegeId), a.getApplicationId())));
        }
        return trees;
    }
}
