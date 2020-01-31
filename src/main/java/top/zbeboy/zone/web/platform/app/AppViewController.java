package top.zbeboy.zone.web.platform.app;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.OauthClientUsersService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.util.RandomUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.bean.platform.app.OauthClientUsersBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class AppViewController {

    @Resource
    private OauthClientUsersService oauthClientUsersService;

    @Resource
    private UsersService usersService;

    /**
     * 平台应用
     *
     * @return 页面
     */
    @GetMapping("/web/menu/platform/app")
    public String index() {
        return "web/platform/app/app_data::#page-wrapper";
    }

    /**
     * 平台应用添加
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/platform/app/add")
    public String add(ModelMap modelMap) {
        modelMap.addAttribute("clientId", UUIDUtil.getUUID());
        modelMap.addAttribute("secret", RandomUtil.generatePassword());
        return "web/platform/app/app_add::#page-wrapper";
    }

    /**
     * 平台应用编辑
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/platform/app/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = usersService.getUserFromSession();
        Optional<Record> record = oauthClientUsersService.findByIdAndUsernameRelation(id, users.getUsername());
        if (record.isPresent()) {
            OauthClientUsersBean oauthClientUsersBean = record.get().into(OauthClientUsersBean.class);
            modelMap.addAttribute("oauthClientUsers", oauthClientUsersBean);
            page = "web/platform/app/app_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到应用数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
