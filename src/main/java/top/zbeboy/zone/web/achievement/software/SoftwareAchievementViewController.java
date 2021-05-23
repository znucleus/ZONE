package top.zbeboy.zone.web.achievement.software;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zone.web.util.SessionUtil;

@Controller
public class SoftwareAchievementViewController {

    /**
     * 软考成绩
     *
     * @return 软考成绩页面
     */
    @GetMapping("/web/menu/achievement/software/query")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.put("idCard", users.getIdCard());
        return "web/achievement/software/software_achievement_query::#page-wrapper";
    }

    /**
     * 软考成绩统计
     *
     * @return 软考成绩统计页面
     */
    @GetMapping("/web/menu/achievement/software/statistics")
    public String statistics() {
        return "web/achievement/software/software_achievement_statistics::#page-wrapper";
    }
}
