package top.zbeboy.zone.web.campus.roster;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.campus.roster.RosterReleaseService;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;

@Controller
public class CampusRosterViewController {

    @Resource
    private RosterReleaseService rosterReleaseService;

    /**
     * 校园花名册
     *
     * @return 校园花名册页面
     */
    @GetMapping("/web/menu/campus/roster")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("canRelease", rosterReleaseService.canRelease(users.getUsername()));
        return "web/campus/roster/roster_data::#page-wrapper";
    }
}
