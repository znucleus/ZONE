package top.zbeboy.zone.web.training.users;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class TrainingUsersViewController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/users")
    public String index() {
        return "web/training/users/training_users::#page-wrapper";
    }

    /**
     * 列表
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/users/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            modelMap.addAttribute("trainingReleaseId", id);
            modelMap.addAttribute("canOperator", trainingConditionCommon.usersCondition(id) ? 1 : 0);
            page = "web/training/users/training_users_list::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训发布数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
