package top.zbeboy.zone.web.training.release;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class TrainingReleaseViewController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    /**
     * 发布主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/release")
    public String index() {
        return "web/training/release/training_release::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @return 添加页面
     */
    @GetMapping("/web/training/release/add")
    public String add() {
        return "web/training/release/training_release_add::#page-wrapper";
    }

    /**
     * 编辑
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/training/release/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingReleaseBean bean = record.get().into(TrainingReleaseBean.class);
            modelMap.addAttribute("trainingRelease", bean);
            page = "web/training/release/training_release_edit::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训发布数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }
}
