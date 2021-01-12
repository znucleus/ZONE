package top.zbeboy.zone.web.theory.users;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.service.theory.TheoryReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.theory.common.TheoryConditionCommon;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class TheoryUsersViewController {

    @Resource
    private TheoryReleaseService theoryReleaseService;

    @Resource
    private TheoryConditionCommon theoryConditionCommon;

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/theory/users")
    public String index() {
        return "web/theory/users/theory_users::#page-wrapper";
    }

    /**
     * 列表
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/theory/users/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = theoryReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            modelMap.addAttribute("theoryReleaseId", id);
            modelMap.addAttribute("canOperator", BooleanUtil.toByte(theoryConditionCommon.usersCondition(id)));
            page = "web/theory/users/theory_users_list::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到理论发布数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
