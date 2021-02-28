package top.zbeboy.zone.web.platform.app;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.platform.app.OauthClientUsersBean;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.AppService;
import top.zbeboy.zbase.tools.service.util.RandomUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class AppViewController {

    @Resource
    private AppService appService;

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
        Users users = SessionUtil.getUserFromSession();
        Optional<OauthClientUsersBean> optionalOauthClientUsersBean = appService.findOauthClientUsersByIdAndUsernameRelation(id, users.getUsername());
        if (optionalOauthClientUsersBean.isPresent()) {
            modelMap.addAttribute("oauthClientUsers", optionalOauthClientUsersBean.get());
            page = "web/platform/app/app_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到应用数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 创建app文章
     *
     * @return 页面
     */
    @GetMapping("/web/platform/app/articles/create")
    public String articlesCreate() {
        return "web/platform/app/articles/create::#page-wrapper";
    }

    /**
     * API 文档
     *
     * @return 页面
     */
    @GetMapping("/web/platform/app/articles/api")
    public String articlesApi() {
        return "web/platform/app/articles/api::#page-wrapper";
    }
}
