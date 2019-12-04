package top.zbeboy.zone.web.system.role;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.Role;
import top.zbeboy.zone.service.platform.RoleService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class SystemRoleViewController {

    @Resource
    private RoleService roleService;

    /**
     * 系统角色页面
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/role")
    public String index() {
        return "web/system/role/system_role::#page-wrapper";
    }

    /**
     * 角色数据编辑
     *
     * @return 编辑页面
     */
    @GetMapping("/web/system/role/edit/{id}")
    public String roleEdit(@PathVariable("id") String roleId, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Role role = roleService.findById(roleId);
        if (Objects.nonNull(role)) {
            modelMap.addAttribute("role", role);
            page = "web/system/role/system_role_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询角色数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }
}
