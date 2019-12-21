package top.zbeboy.zone.web.platform.authorize;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.RoleApplyService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.platform.authorize.RoleApplyBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AuthorizeViewController {

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
    private RoleApplyService roleApplyService;

    /**
     * 平台授权限
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/authorize")
    public String index(ModelMap modelMap) {
        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_SYSTEM.name());
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_ADMIN.name());
        }

        Users users = usersService.getUserFromSession();
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
                    modelMap.addAttribute("collegeId", record.get().into(College.class).getCollegeId());
                    page = "web/platform/authorize/authorize_add::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到您的院ID或暂不支持您的注册类型");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到用户类型");
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

        if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_SYSTEM.name())) {
            Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleUsersId);
            if (roleApplyRecord.isPresent()) {
                RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                Byte status = roleApplyBean.getApplyStatus();
                if (status != 1) {
                    roleApplyBean.setDurationInt(getDuration(roleApplyBean.getDuration()));
                    modelMap.addAttribute("roleApply", roleApplyBean);
                    page = "web/platform/authorize/authorize_edit::#page-wrapper";
                } else {
                    config.buildDangerTip("操作错误", "申请已通过，不可操作");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }

            } else {
                config.buildDangerTip("查询错误", "未查询到该申请信息");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else if (roleService.isCurrentUserInRole(Workbook.authorities.ROLE_ADMIN.name())) {
            // 判断是否同一个院
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
                    int collegeId = record.get().into(College.class).getCollegeId();
                    Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleUsersId);
                    if (roleApplyRecord.isPresent()) {
                        RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                        if (collegeId == roleApplyBean.getCollegeId()) {
                            Byte status = roleApplyBean.getApplyStatus();
                            if (status != 1) {
                                roleApplyBean.setDurationInt(getDuration(roleApplyBean.getDuration()));
                                modelMap.addAttribute("roleApply", roleApplyBean);
                                page = "web/platform/authorize/authorize_edit::#page-wrapper";
                            } else {
                                config.buildDangerTip("操作错误", "申请已通过，不可操作");
                                config.dataMerging(modelMap);
                                page = "inline_tip::#page-wrapper";
                            }
                        } else {
                            config.buildDangerTip("操作错误", "该账号不在您所属院下，不允许操作");
                            config.dataMerging(modelMap);
                            page = "inline_tip::#page-wrapper";
                        }
                    } else {
                        config.buildDangerTip("查询错误", "未查询到该申请信息");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("查询错误", "未查询到该用户所属院信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到该用户类型");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            Optional<Record> roleApplyRecord = roleApplyService.findByIdRelation(roleUsersId);
            if (roleApplyRecord.isPresent()) {
                RoleApplyBean roleApplyBean = roleApplyRecord.get().into(RoleApplyBean.class);
                Users users = usersService.getUserFromSession();
                if (StringUtils.equals(users.getUsername(), roleApplyBean.getUsername())) {
                    Byte status = roleApplyBean.getApplyStatus();
                    if (status != 1) {
                        roleApplyBean.setDurationInt(getDuration(roleApplyBean.getDuration()));
                        modelMap.addAttribute("roleApply", roleApplyBean);
                        page = "web/platform/authorize/authorize_edit::#page-wrapper";
                    } else {
                        config.buildDangerTip("操作错误", "申请已通过，不可操作");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("操作错误", "非本人申请，不可操作");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到该申请信息");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
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
