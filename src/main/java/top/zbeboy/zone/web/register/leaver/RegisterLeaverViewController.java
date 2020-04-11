package top.zbeboy.zone.web.register.leaver;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterOptionRecord;
import top.zbeboy.zone.domain.tables.records.LeaverRegisterScopeRecord;
import top.zbeboy.zone.service.data.*;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.service.register.LeaverRegisterOptionService;
import top.zbeboy.zone.service.register.LeaverRegisterReleaseService;
import top.zbeboy.zone.service.register.LeaverRegisterScopeService;
import top.zbeboy.zone.web.bean.register.leaver.LeaverRegisterScopeBean;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
    private CollegeService collegeService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScienceService scienceService;

    @Resource
    private GradeService gradeService;

    @Resource
    private OrganizeService organizeService;

    @Resource
    private RegisterConditionCommon registerConditionCommon;

    @Resource
    private LeaverRegisterReleaseService leaverRegisterReleaseService;

    @Resource
    private LeaverRegisterOptionService leaverRegisterOptionService;

    @Resource
    private LeaverRegisterScopeService leaverRegisterScopeService;

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
                Optional<Record> record = Optional.empty();
                if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = staffService.findByUsernameRelation(users.getUsername());
                } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    record = studentService.findByUsernameRelation(users.getUsername());
                }

                if (record.isPresent()) {
                    modelMap.addAttribute("schoolId", record.get().into(School.class).getSchoolId());
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
        if (registerConditionCommon.leaverOperator(id)) {
            boolean canAccess = false;
            if (!roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
                Users users = usersService.getUserFromSession();
                UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
                if (Objects.nonNull(usersType)) {
                    Optional<Record> record = Optional.empty();
                    if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                        record = staffService.findByUsernameRelation(users.getUsername());
                    } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                        record = studentService.findByUsernameRelation(users.getUsername());
                    }

                    if (record.isPresent()) {
                        canAccess = true;
                        modelMap.addAttribute("schoolId", record.get().into(School.class).getSchoolId());
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

                    List<LeaverRegisterOption> leaverRegisterOptions = new ArrayList<>();
                    Result<LeaverRegisterOptionRecord> leaverRegisterOptionRecords =
                            leaverRegisterOptionService.findByLeaverRegisterReleaseId(id);
                    if (leaverRegisterOptionRecords.isNotEmpty()) {
                        leaverRegisterOptions = leaverRegisterOptionRecords.into(LeaverRegisterOption.class);
                    }
                    modelMap.addAttribute("leaverRegisterOptions", leaverRegisterOptions);

                    List<LeaverRegisterScopeBean> leaverRegisterScopes = new ArrayList<>();
                    Result<LeaverRegisterScopeRecord> leaverRegisterScopeRecords =
                            leaverRegisterScopeService.findByLeaverRegisterReleaseId(id);
                    if (leaverRegisterScopeRecords.isNotEmpty()) {
                        leaverRegisterScopes = leaverRegisterScopeRecords.into(LeaverRegisterScopeBean.class);
                        for (LeaverRegisterScopeBean bean : leaverRegisterScopes) {
                            switch (leaverRegisterRelease.getDataScope()) {
                                case 1:
                                    // 院
                                    College college = collegeService.findById(bean.getDataId());
                                    if (Objects.nonNull(college)) {
                                        bean.setDataName(college.getCollegeName());
                                    }
                                    break;
                                case 2:
                                    // 系
                                    Department department = departmentService.findById(bean.getDataId());
                                    if (Objects.nonNull(department)) {
                                        bean.setDataName(department.getDepartmentName());
                                    }
                                    break;
                                case 3:
                                    // 专业
                                    Science science = scienceService.findById(bean.getDataId());
                                    if (Objects.nonNull(science)) {
                                        bean.setDataName(science.getScienceName());
                                    }
                                    break;
                                case 4:
                                    // 年级
                                    Grade grade = gradeService.findById(bean.getDataId());
                                    if (Objects.nonNull(grade)) {
                                        bean.setDataName(grade.getGrade() + "");
                                    }
                                    break;
                                case 5:
                                    // 班级
                                    Organize organize = organizeService.findById(bean.getDataId());
                                    if (Objects.nonNull(organize)) {
                                        bean.setDataName(organize.getOrganizeName());
                                    }
                                    break;
                            }
                        }
                    }

                    modelMap.addAttribute("leaverRegisterScopes", leaverRegisterScopes);
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
        if (registerConditionCommon.leaverRegister(id)) {
            List<LeaverRegisterOption> leaverRegisterOptions = new ArrayList<>();
            Result<LeaverRegisterOptionRecord> leaverRegisterOptionRecords =
                    leaverRegisterOptionService.findByLeaverRegisterReleaseId(id);
            if (leaverRegisterOptionRecords.isNotEmpty()) {
                leaverRegisterOptions = leaverRegisterOptionRecords.into(LeaverRegisterOption.class);
            }
            modelMap.addAttribute("leaverRegisterOptions", leaverRegisterOptions);
            modelMap.addAttribute("leaverRegisterReleaseId", id);
            page = "web/register/leaver/leaver_data_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您不满足登记条件");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
