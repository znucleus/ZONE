package top.zbeboy.zone.web.register.epidemic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zone.web.register.common.RegisterConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;

@Controller
public class RegisterEpidemicViewController {

    @Resource
    private RegisterConditionCommon registerConditionCommon;

    /**
     * 疫情登记
     *
     * @return 疫情登记页面
     */
    @GetMapping("/web/menu/register/epidemic")
    public String index() {
        return "web/register/epidemic/epidemic_release::#page-wrapper";
    }

    /**
     * 疫情登记发布添加页面
     *
     * @param modelMap 页面对象
     * @return 疫情登记发布添加页面
     */
    @GetMapping("/web/register/epidemic/release/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (registerConditionCommon.epidemicOperator()) {
            page = "web/register/epidemic/epidemic_release_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
