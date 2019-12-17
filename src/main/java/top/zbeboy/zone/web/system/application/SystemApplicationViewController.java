package top.zbeboy.zone.web.system.application;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.service.system.ApplicationService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;

@Controller
public class SystemApplicationViewController {

    @Resource
    private ApplicationService applicationService;

    /**
     * 系统应用
     *
     * @return 页面
     */
    @GetMapping("/web/menu/system/application")
    public String index() {
        return "web/system/application/system_application::#page-wrapper";
    }

    /**
     * 添加
     *
     * @return 页面
     */
    @GetMapping("/web/system/application/add")
    public String add() {
        return "web/system/application/system_application_add::#page-wrapper";
    }

    /**
     * 编辑
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/system/application/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Application application = applicationService.findById(id);
        if (Objects.nonNull(application)) {
            modelMap.addAttribute("systemApplication", application);
            page = "web/system/application/system_application_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到应用数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
