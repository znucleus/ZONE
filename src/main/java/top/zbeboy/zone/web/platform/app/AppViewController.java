package top.zbeboy.zone.web.platform.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.service.util.RandomUtil;
import top.zbeboy.zone.service.util.UUIDUtil;

@Controller
public class AppViewController {

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
}
