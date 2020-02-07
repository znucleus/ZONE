package top.zbeboy.zone.web.internship.distribution;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class InternshipTeacherDistributionViewController {

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    /**
     * 实习教师分配
     *
     * @return 实习教师分配页面
     */
    @GetMapping("/web/menu/internship/teacher_distribution")
    public String index() {
        return "web/internship/distribution/internship_teacher_distribution::#page-wrapper";
    }

    /**
     * 教师分配页面
     *
     * @param id 实习发布id
     * @return 页面
     */
    @GetMapping("/web/internship/teacher_distribution/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = internshipReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            if (internshipConditionCommon.teacherDistributionCondition(id)) {
                modelMap.addAttribute("internshipReleaseId", id);
                page = "web/internship/distribution/internship_distribution_list::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实习发布数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
