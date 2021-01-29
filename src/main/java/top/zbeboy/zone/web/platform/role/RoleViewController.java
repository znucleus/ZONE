package top.zbeboy.zone.web.platform.role;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.platform.role.RoleBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.DefaultRole;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class RoleViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private RoleService roleService;

    /**
     * 平台角色
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/role")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_SYSTEM.name());
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_ADMIN.name());
        }
        return "web/platform/role/role_data::#page-wrapper";
    }


    /**
     * 角色数据添加
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/platform/role/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("collegeId", 0);
            page = "web/platform/role/role_add::#page-wrapper";
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }

                if (collegeId > 0) {
                    modelMap.addAttribute("collegeId", collegeId);
                    page = "web/platform/role/role_add::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到您的院ID或暂不支持您的注册类型");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到当前用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("权限错误", "您无权限进行操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }

    /**
     * 角色数据编辑
     *
     * @param id       角色id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/platform/role/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            RoleBean role = roleService.findCollegeRoleByRoleIdRelation(id);
            if (Objects.nonNull(role) && StringUtils.isNotBlank(role.getRoleId())) {
                modelMap.addAttribute("role", role);
                page = "web/platform/role/role_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到角色数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            // 判断是否同一个院
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }
                if (collegeId > 0) {
                    RoleBean role = roleService.findCollegeRoleByRoleIdRelation(id);
                    if (Objects.nonNull(role) && StringUtils.isNotBlank(role.getRoleId())) {
                        if (collegeId == role.getCollegeId()) {
                            modelMap.addAttribute("role", role);
                            page = "web/platform/role/role_edit::#page-wrapper";
                        } else {
                            config.buildDangerTip("操作错误", "该角色不在您所属院下，不允许操作");
                            config.dataMerging(modelMap);
                            page = "inline_tip::#page-wrapper";
                        }
                    } else {
                        config.buildDangerTip("查询错误", "未查询到角色数据");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("查询错误", "未查询到当前用户所属院信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到当前用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("权限错误", "您无权限进行操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 角色自动化
     *
     * @param id       角色id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/platform/role/auto/{id}")
    public String auto(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            RoleBean role = roleService.findCollegeRoleByRoleIdRelation(id);
            if (Objects.nonNull(role) && StringUtils.isNotBlank(role.getRoleId())) {
                List<DefaultRole> defaultRoles = roleService.findDefaultRoleByRoleId(role.getRoleId());
                List<Integer> usersTypeIds = new ArrayList<>();
                defaultRoles.forEach(defaultRole -> {
                    usersTypeIds.add(defaultRole.getUsersTypeId());
                });
                modelMap.addAttribute("usersTypeIds", StringUtils.join(usersTypeIds, ","));
                modelMap.addAttribute("role", role);
                page = "web/platform/role/role_auto::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到角色数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            // 判断是否同一个院
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                        collegeId = bean.getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        collegeId = studentBean.getCollegeId();
                    }
                }
                if (collegeId > 0) {
                    RoleBean role = roleService.findCollegeRoleByRoleIdRelation(id);
                    if (Objects.nonNull(role) && StringUtils.isNotBlank(role.getRoleId())) {
                        if (collegeId == role.getCollegeId()) {
                            List<DefaultRole> defaultRoles = roleService.findDefaultRoleByRoleId(role.getRoleId());
                            List<Integer> usersTypeIds = new ArrayList<>();
                            defaultRoles.forEach(defaultRole -> {
                                usersTypeIds.add(defaultRole.getUsersTypeId());
                            });
                            modelMap.addAttribute("usersTypeIds", StringUtils.join(usersTypeIds, ","));
                            modelMap.addAttribute("role", role);
                            page = "web/platform/role/role_auto::#page-wrapper";
                        } else {
                            config.buildDangerTip("操作错误", "该角色不在您所属院下，不允许操作");
                            config.dataMerging(modelMap);
                            page = "inline_tip::#page-wrapper";
                        }
                    } else {
                        config.buildDangerTip("查询错误", "未查询到角色数据");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("查询错误", "未查询到当前用户所属院信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到当前用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("权限错误", "您无权限进行操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 角色数据查看
     *
     * @param id       角色id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/platform/role/see/{id}")
    public String see(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        RoleBean role = roleService.findCollegeRoleByRoleIdRelation(id);
        if (Objects.nonNull(role) && StringUtils.isNotBlank(role.getRoleId())) {
            modelMap.addAttribute("role", role);
            page = "web/platform/role/role_see::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到角色数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
