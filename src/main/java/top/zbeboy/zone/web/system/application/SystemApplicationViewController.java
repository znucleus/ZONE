package top.zbeboy.zone.web.system.application;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.domain.tables.pojos.Application;
import top.zbeboy.zone.service.system.ApplicationService;

import javax.annotation.Resource;

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

    @GetMapping("/web/system/application/add")
    public String add() {
        return "web/system/application/system_application_add::#page-wrapper";
    }

    @GetMapping("/web/system/application/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        Application application = applicationService.findById(id);
        modelMap.addAttribute("application", application);
        return "web/system/application/system_application_edit::#page-wrapper";
    }
}
