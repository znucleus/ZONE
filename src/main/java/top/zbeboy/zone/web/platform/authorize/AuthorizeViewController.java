package top.zbeboy.zone.web.platform.authorize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.*;
import top.zbeboy.zbase.feign.platform.AuthorizeService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthorizeViewController {

    @Resource
    private UsersService usersService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScienceService scienceService;

    @Resource
    private GradeService gradeService;

    @Resource
    private OrganizeService organizeService;

    @Resource
    private RoleService roleService;

    /**
     * 平台授权限
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/authorize")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_SYSTEM.name());
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_ADMIN.name());
        }

        modelMap.addAttribute("username", users.getUsername());
        return "web/platform/authorize/authorize_data::#page-wrapper";
    }

    /**
     * 申请
     *
     * @return 页面
     */
    @GetMapping("/web/platform/authorize/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                int collegeId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                    if (optionalStaffBean.isPresent()) {
                        collegeId = optionalStaffBean.get().getCollegeId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (optionalStudentBean.isPresent()) {
                        collegeId = optionalStudentBean.get().getCollegeId();
                    }
                }

                if (collegeId > 0) {
                    modelMap.addAttribute("collegeId", collegeId);
                    page = "web/platform/authorize/authorize_add::#page-wrapper";
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
            modelMap.addAttribute("collegeId", 0);
            page = "web/platform/authorize/authorize_add::#page-wrapper";
        }
        return page;
    }

    /**
     * 编辑
     *
     * @return 页面
     */
    @GetMapping("/web/platform/authorize/edit/{id}")
    public String edit(@PathVariable("id") String roleUsersId, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;

        boolean canEdit = false;
        RoleApplyBean roleApplyBean = null;
        Users own = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(own.getUsername(), Workbook.authorities.ROLE_SYSTEM.name()) ||
                roleService.isCurrentUserInRole(own.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            Optional<RoleApplyBean> optionalRoleApplyBean = authorizeService.findRoleApplyByIdRelation(roleUsersId);
            if (optionalRoleApplyBean.isPresent()) {
                roleApplyBean = optionalRoleApplyBean.get();
                canEdit = true;
            }
        } else {
            Optional<RoleApplyBean> optionalRoleApplyBean = authorizeService.findRoleApplyByIdRelation(roleUsersId);
            if (optionalRoleApplyBean.isPresent()) {
                roleApplyBean = optionalRoleApplyBean.get();
                if (StringUtils.equals(own.getUsername(), roleApplyBean.getUsername())) {
                    canEdit = true;
                }
            }
        }

        if (canEdit) {
            Byte status = roleApplyBean.getApplyStatus();
            if (status != 1) {
                Optional<Users> result = usersService.findByUsername(roleApplyBean.getUsername());
                if (result.isPresent()) {
                    Users users = result.get();
                    int collegeId = 0;
                    Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                    if (optionalUsersType.isPresent()) {
                        UsersType usersType = optionalUsersType.get();
                        if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<StaffBean> optionalStaffBean = staffService.findByUsernameRelation(users.getUsername());
                            if (optionalStaffBean.isPresent()) {
                                collegeId = optionalStaffBean.get().getCollegeId();
                            }
                        } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
                            if (optionalStudentBean.isPresent()) {
                                collegeId = optionalStudentBean.get().getCollegeId();
                            }
                        }
                    }

                    if (collegeId > 0) {
                        modelMap.addAttribute("collegeId", collegeId);
                        roleApplyBean.setDurationInt(getDuration(roleApplyBean.getDuration()));
                        if (roleApplyBean.getDataScope() == 1) {
                            Optional<Department> optionalDepartment = departmentService.findById(roleApplyBean.getDataId());
                            if (optionalDepartment.isPresent()) {
                                roleApplyBean.setDataName(optionalDepartment.get().getDepartmentName());
                            }
                        } else if (roleApplyBean.getDataScope() == 2) {
                            Optional<Science> optionalScience = scienceService.findById(roleApplyBean.getDataId());
                            if (optionalScience.isPresent()) {
                                roleApplyBean.setDataName(optionalScience.get().getScienceName());
                            }
                        } else if (roleApplyBean.getDataScope() == 3) {
                            Optional<Grade> optionalGrade = gradeService.findById(roleApplyBean.getDataId());
                            if (optionalGrade.isPresent()) {
                                roleApplyBean.setDataName(optionalGrade.get().getGrade() + "");
                            }
                        } else if (roleApplyBean.getDataScope() == 4) {
                            Optional<Organize> optionalOrganize = organizeService.findById(roleApplyBean.getDataId());
                            if (optionalOrganize.isPresent()) {
                                roleApplyBean.setDataName(optionalOrganize.get().getOrganizeName());
                            }
                        }
                        modelMap.addAttribute("roleApply", roleApplyBean);
                        page = "web/platform/authorize/authorize_edit::#page-wrapper";
                    } else {
                        config.buildDangerTip("查询错误", "未查询到申请账号所属院信息");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("查询错误", "未查询到申请账号信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }

            } else {
                config.buildDangerTip("操作错误", "申请已通过，不可操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("操作错误", "您无权限进行操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 时长转换
     *
     * @param duration 时长
     * @return 转换
     */
    private int getDuration(String duration) {
        int d = 0;
        switch (duration) {
            case "1天":
                d = 1;
                break;
            case "3天":
                d = 2;
                break;
            case "7天":
                d = 3;
                break;
            case "1个月":
                d = 4;
                break;
            case "3个月":
                d = 5;
                break;
            case "1年":
                d = 6;
                break;
            case "3年":
                d = 7;
                break;
        }

        return d;
    }
}
