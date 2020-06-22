package top.zbeboy.zone.web.platform.authorize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zone.feign.data.*;
import top.zbeboy.zone.feign.platform.AuthorizeService;
import top.zbeboy.zone.feign.platform.UsersService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;

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

    /**
     * 平台授权限
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/authorize")
    public String index(ModelMap modelMap) {
        if (SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_SYSTEM.name());
        } else if (SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_ADMIN.name());
        }

        Users users = SessionUtil.getUserFromSession();
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
        if (!SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
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
        if (SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name()) ||
                SessionUtil.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            roleApplyBean = authorizeService.findRoleApplyByIdRelation(roleUsersId);
            if (Objects.nonNull(roleApplyBean) && StringUtils.isNotBlank(roleApplyBean.getRoleApplyId())) {
                canEdit = true;
            }
        } else {
            roleApplyBean = authorizeService.findRoleApplyByIdRelation(roleUsersId);
            if (Objects.nonNull(roleApplyBean) && StringUtils.isNotBlank(roleApplyBean.getRoleApplyId())) {
                Users users = SessionUtil.getUserFromSession();
                if (StringUtils.equals(users.getUsername(), roleApplyBean.getUsername())) {
                    canEdit = true;
                }
            }
        }

        if (canEdit) {
            Byte status = roleApplyBean.getApplyStatus();
            if (status != 1) {
                Users users = usersService.findByUsername(roleApplyBean.getUsername());
                if (Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername())) {
                    int collegeId = 0;
                    UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                    if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
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
                    }

                    if (collegeId > 0) {
                        modelMap.addAttribute("collegeId", collegeId);
                        roleApplyBean.setDurationInt(getDuration(roleApplyBean.getDuration()));
                        if (roleApplyBean.getDataScope() == 1) {
                            Department department = departmentService.findById(roleApplyBean.getDataId());
                            if (Objects.nonNull(department.getDepartmentId())) {
                                roleApplyBean.setDataName(department.getDepartmentName());
                            }
                        } else if (roleApplyBean.getDataScope() == 2) {
                            Science science = scienceService.findById(roleApplyBean.getDataId());
                            if (Objects.nonNull(science.getScienceId())) {
                                roleApplyBean.setDataName(science.getScienceName());
                            }
                        } else if (roleApplyBean.getDataScope() == 3) {
                            Grade grade = gradeService.findById(roleApplyBean.getDataId());
                            if (Objects.nonNull(grade.getGradeId())) {
                                roleApplyBean.setDataName(grade.getGrade() + "");
                            }
                        } else if (roleApplyBean.getDataScope() == 4) {
                            Organize organize = organizeService.findById(roleApplyBean.getDataId());
                            if (Objects.nonNull(organize.getOrganizeId())) {
                                roleApplyBean.setDataName(organize.getOrganizeName());
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
