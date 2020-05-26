package top.zbeboy.zone.web.register.leaver;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.LeaverRegisterRelease;
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.feign.data.StaffService;
import top.zbeboy.zone.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.register.common.RegisterControllerCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class RegisterLeaverViewController {

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
    private RegisterConditionCommon registerConditionCommon;

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private RegisterControllerCommon registerControllerCommon;

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
        if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Users users = usersService.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType)) {
                int schoolId = 0;
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                    if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                        schoolId = bean.getSchoolId();
                    }
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    Optional<Record> record =  studentService.findByUsernameRelation(users.getUsername());
                    if(record.isPresent()){
                        schoolId = record.get().into(School.class).getSchoolId();
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
        String channel = Workbook.channel.WEB.name();
        if (registerConditionCommon.leaverOperator(id, channel, null)) {
            boolean canAccess = false;
            if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    int schoolId = 0;
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                        if (Objects.nonNull(bean) && bean.getStaffId() > 0) {
                            schoolId = bean.getSchoolId();
                        }
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
                        if(record.isPresent()){
                            schoolId = record.get().into(School.class).getSchoolId();
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
                LeaverRegisterRelease leaverRegisterRelease = leaverRegisterReleaseService.findById(id);
                if (Objects.nonNull(leaverRegisterRelease)) {
                    modelMap.addAttribute("leaverRegisterRelease", leaverRegisterRelease);
                    modelMap.addAttribute("leaverRegisterOptions", registerControllerCommon.leaverRegisterOptions(id));
                    modelMap.addAttribute("leaverRegisterScopes",
                            registerControllerCommon.leaverRegisterScopes(id, leaverRegisterRelease.getDataScope()));
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
        String channel = Workbook.channel.WEB.name();
        if (registerConditionCommon.leaverRegister(id, channel, null)) {
            modelMap.addAttribute("leaverRegisterOptions", registerControllerCommon.leaverRegisterOptions(id));
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
        String channel = Workbook.channel.WEB.name();
        if (registerConditionCommon.leaverReview(id, channel, null)) {
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
