package top.zbeboy.zone.web.training.document;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.service.training.TrainingDocumentService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.training.document.TrainingDocumentBean;
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

    @Resource
    private TrainingDocumentService trainingDocumentService;

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

    /**
     * 编辑文章
     *
     * @param id       实训文章id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/document/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingDocumentService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingDocumentBean bean = record.get().into(TrainingDocumentBean.class);
            if (trainingConditionCommon.canOperator(bean.getTrainingReleaseId())) {
                modelMap.addAttribute("trainingDocument", bean);
                page = "web/training/document/training_document_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实训文章数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 查看文章
     *
     * @param id       实训文章id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/document/look/{id}")
    public String look(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingDocumentService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingDocumentBean bean = record.get().into(TrainingDocumentBean.class);
            bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
            trainingDocumentService.updateReading(bean.getTrainingDocumentId());
            modelMap.addAttribute("trainingDocument", bean);
            page = "web/training/document/training_document_look::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训文章数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

}
