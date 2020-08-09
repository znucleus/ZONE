package top.zbeboy.zone.web.campus.opens;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.opens.SchoolOpensService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;

@Controller
public class CampusOpensViewController {

    @Resource
    private SchoolOpensService schoolOpensService;

    /**
     * 校园开学
     *
     * @return 校园开学页面
     */
    @GetMapping("/web/menu/campus/opens")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("canRelease", schoolOpensService.opensConditionRelease(users.getUsername()));
        return "web/campus/opens/opens_data::#page-wrapper";
    }
}
