package top.zbeboy.zone.web.platform.role;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.College;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.pojos.UsersType;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.CollegeRoleService;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.platform.role.RoleBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class RoleViewController {

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
    private CollegeRoleService collegeRoleService;

    /**
     * 平台角色
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/role")
    public String index() {
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
                    page = "web/platform/role/role_add::#page-wrapper";
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
            page = "web/platform/role/role_add::#page-wrapper";
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
        Optional<Record> record = roleService.findCollegeByRoleId(id);
        if (record.isPresent()) {
            RoleBean role = record.get().into(RoleBean.class);
            modelMap.addAttribute("role", role);
            page = "web/platform/role/role_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到角色数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
