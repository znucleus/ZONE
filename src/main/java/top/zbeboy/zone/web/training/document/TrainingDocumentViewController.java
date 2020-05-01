package top.zbeboy.zone.web.training.document;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class TrainingDocumentViewController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/document")
    public String index() {
        return "web/training/document/training_document::#page-wrapper";
    }

    /**
     * 列表
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/document/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            modelMap.addAttribute("canOperator", trainingConditionCommon.canOperator(id));
            modelMap.addAttribute("trainingReleaseId", id);
            page = "web/training/document/training_document_list::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训发布数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 添加
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/document/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.canOperator(id)) {
            modelMap.addAttribute("trainingReleaseId", id);
            page = "web/training/document/training_document_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
