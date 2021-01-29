package top.zbeboy.zone.web.register.leaver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.feign.register.RegisterLeaverService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class RegisterLeaverViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private RegisterLeaverService registerLeaverService;

    @Resource
    private RoleService roleService;

    /**
     * 离校登记
     *
     * @return 离校登记页面
     */
    @GetMapping("/web/menu/register/leaver")
    public String index() {
        return "web/register/leaver/leaver_release::#page-wrapper";
    }

    /**
     * 离校登记发布添加页面
     *
     * @return 离校登记发布添加页面
     */
    @GetMapping("/web/register/leaver/release/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
            if (optionalUsersType.isPresent()) {
                UsersType usersType = optionalUsersType.get();
                int schoolId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                        schoolId = bean.getSchoolId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                        schoolId = studentBean.getSchoolId();
                    }
                }

                if (schoolId > 0) {
                    modelMap.addAttribute("schoolId", schoolId);
                    page = "web/register/leaver/leaver_release_add::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到您的学校ID或暂不支持您的注册类型");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到当前用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            modelMap.addAttribute("schoolId", 0);
            page = "web/register/leaver/leaver_release_add::#page-wrapper";
        }
        return page;
    }

    /**
     * 离校登记发布编辑页面
     *
     * @return 离校登记发布编辑页面
     */
    @GetMapping("/web/register/leaver/release/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (registerLeaverService.leaverOperator(users.getUsername(), id)) {
            boolean canAccess = false;
            if (!roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
                Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
                if (optionalUsersType.isPresent()) {
                    UsersType usersType = optionalUsersType.get();
                    int schoolId = 0;
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                            schoolId = bean.getSchoolId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                            schoolId = studentBean.getSchoolId();
                        }
                    }

                    if (schoolId > 0) {
                        canAccess = true;
                        modelMap.addAttribute("schoolId", schoolId);
                        page = "web/register/leaver/leaver_release_edit::#page-wrapper";
                    } else {
                        config.buildDangerTip("查询错误", "未查询到您的学校ID或暂不支持您的注册类型");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("查询错误", "未查询到当前用户类型");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                canAccess = true;
                modelMap.addAttribute("schoolId", 0);
                page = "web/register/leaver/leaver_release_edit::#page-wrapper";
            }

            if (canAccess) {
                LeaverRegisterRelease leaverRegisterRelease = registerLeaverService.release(id);
                if (Objects.nonNull(leaverRegisterRelease) && StringUtils.isNotBlank(leaverRegisterRelease.getLeaverRegisterReleaseId())) {
                    modelMap.addAttribute("leaverRegisterRelease", leaverRegisterRelease);
                    modelMap.addAttribute("leaverRegisterOptions", registerLeaverService.leaverRegisterOptions(id));
                    modelMap.addAttribute("leaverRegisterScopes",
                            registerLeaverService.leaverRegisterScopes(id));
                } else {
                    config.buildDangerTip("查询错误", "未查询到离校登记发布数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }

    /**
     * 登记页面
     *
     * @return 登记页面
     */
    @GetMapping("/web/register/leaver/data/add/{id}")
    public String dataAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (registerLeaverService.leaverRegister(users.getUsername(), id)) {
            modelMap.addAttribute("leaverRegisterOptions", registerLeaverService.leaverRegisterOptions(id));
            modelMap.addAttribute("leaverRegisterReleaseId", id);
            page = "web/register/leaver/leaver_data_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您不满足登记条件");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 统计
     *
     * @param id       发布id
     * @param modelMap 页面对象
     * @return 登记页面
     */
    @GetMapping("/web/register/leaver/review/{id}")
    public String dataReview(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (registerLeaverService.leaverReview(users.getUsername(), id)) {
            modelMap.addAttribute("leaverRegisterReleaseId", id);
            page = "web/register/leaver/leaver_data_review::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
