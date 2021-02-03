package top.zbeboy.zone.web.training.special;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.training.special.TrainingSpecialBean;
import top.zbeboy.zbase.bean.training.special.TrainingSpecialDocumentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zbase.feign.system.FilesService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zone.service.training.TrainingSpecialDocumentService;
import top.zbeboy.zone.service.training.TrainingSpecialService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TrainingSpecialViewController {

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingSpecialService trainingSpecialService;

    @Resource
    private TrainingSpecialDocumentService trainingSpecialDocumentService;

    @Resource
    private FilesService filesService;

    /**
     * 专题主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/special")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("canOperator", trainingConditionCommon.specialCondition());
        return "web/training/special/training_special::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @return 添加页面
     */
    @GetMapping("/web/training/special/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.specialCondition()) {
            Optional<Files> optionalFiles = filesService.findById(Workbook.SYSTEM_COVER);
            optionalFiles.ifPresent(files -> modelMap.addAttribute("cover", Workbook.DIRECTORY_SPLIT + files.getRelativePath()));
            page = "web/training/special/training_special_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 编辑页面
     *
     * @return 编辑页面
     */
    @GetMapping("/web/training/special/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingSpecialService.findByIdRelation(id);
        if (record.isPresent()) {
            if (trainingConditionCommon.specialCondition()) {
                TrainingSpecialBean bean = record.get().into(TrainingSpecialBean.class);
                bean.setRealCover(Workbook.DIRECTORY_SPLIT + bean.getRelativePath());
                modelMap.addAttribute("trainingSpecial", bean);
                page = "web/training/special/training_special_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实训专题数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }

    /**
     * 文档页面
     *
     * @return 文档页面
     */
    @GetMapping("/web/training/special/document/{id}")
    public String document(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("trainingSpecialId", id);
        modelMap.addAttribute("canOperator", trainingConditionCommon.specialCondition());
        return "web/training/special/training_special_document::#page-wrapper";
    }

    /**
     * 添加
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/special/document/add/{id}")
    public String documentAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.specialCondition()) {
            modelMap.addAttribute("trainingSpecialId", id);
            page = "web/training/special/training_special_document_add::#page-wrapper";
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
    @GetMapping("/web/training/special/document/edit/{id}")
    public String documentEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingSpecialDocumentService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingSpecialDocumentBean bean = record.get().into(TrainingSpecialDocumentBean.class);
            if (trainingConditionCommon.specialCondition()) {
                modelMap.addAttribute("trainingSpecialDocument", bean);
                page = "web/training/special/training_special_document_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实训专题文章数据");
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
    @GetMapping("/web/training/special/document/look/{id}")
    public String look(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingSpecialDocumentService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingSpecialDocumentBean bean = record.get().into(TrainingSpecialDocumentBean.class);
            bean.setCreateDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getCreateDate()));
            trainingSpecialDocumentService.updateReading(bean.getTrainingSpecialDocumentId());
            modelMap.addAttribute("trainingSpecialDocument", bean);
            page = "web/training/special/training_special_document_look::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训文章数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 文件页面
     *
     * @return 文件页面
     */
    @GetMapping("/web/training/special/file/{id}")
    public String file(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("trainingSpecialId", id);
        modelMap.addAttribute("canOperator", trainingConditionCommon.specialCondition());
        return "web/training/special/training_special_file::#page-wrapper";
    }
}
