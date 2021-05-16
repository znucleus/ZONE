package top.zbeboy.zone.web.training.release;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.training.release.TrainingAuthoritiesBean;
import top.zbeboy.zbase.bean.training.release.TrainingConfigureBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zone.service.training.TrainingAuthoritiesService;
import top.zbeboy.zone.service.training.TrainingConfigureService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TrainingReleaseViewController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingConfigureService trainingConfigureService;

    @Resource
    private TrainingAuthoritiesService trainingAuthoritiesService;

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
        if (trainingConditionCommon.canOperator(id)) {
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
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 配置
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 配置页面
     */
    @GetMapping("/web/training/release/configure/{id}")
    public String configure(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.canOperator(id)) {
            TrainingRelease trainingRelease = trainingReleaseService.findById(id);
            if (Objects.nonNull(trainingRelease)) {
                modelMap.addAttribute("trainingReleaseId", id);
                page = "web/training/release/training_configure::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到实训发布数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 配置添加
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 配置页面
     */
    @GetMapping("/web/training/release/configure/add/{id}")
    public String configureAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.canOperator(id)) {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(id);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);
                modelMap.addAttribute("trainingReleaseId", bean.getTrainingReleaseId());
                modelMap.addAttribute("collegeId", bean.getCollegeId());
                page = "web/training/release/training_configure_add::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到实训发布数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 配置编辑
     *
     * @param id       配置id
     * @param modelMap 页面对象
     * @return 配置页面
     */
    @GetMapping("/web/training/release/configure/edit/{id}")
    public String configureEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingConfigureService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingConfigureBean bean = record.get().into(TrainingConfigureBean.class);
            if (trainingConditionCommon.canOperator(bean.getTrainingReleaseId())) {
                modelMap.addAttribute("trainingConfigure", bean);
                page = "web/training/release/training_configure_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实训配置数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 配置
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 配置页面
     */
    @GetMapping("/web/training/release/authorities/{id}")
    public String authorities(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.canOperator(id)) {
            TrainingRelease trainingRelease = trainingReleaseService.findById(id);
            if (Objects.nonNull(trainingRelease)) {
                modelMap.addAttribute("trainingReleaseId", id);
                page = "web/training/release/training_authorities::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到实训发布数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 权限添加
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 配置页面
     */
    @GetMapping("/web/training/release/authorities/add/{id}")
    public String authoritiesAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.canOperator(id)) {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(id);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);
                modelMap.addAttribute("trainingReleaseId", bean.getTrainingReleaseId());
                page = "web/training/release/training_authorities_add::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到实训发布数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 权限更新
     *
     * @param id       权限id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/release/authorities/edit/{id}")
    public String authoritiesEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingAuthoritiesService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingAuthoritiesBean bean = record.get().into(TrainingAuthoritiesBean.class);
            bean.setValidDateStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getValidDate()));
            bean.setExpireDateStr(DateTimeUtil.defaultFormatLocalDateTime(bean.getExpireDate()));
            if (trainingConditionCommon.canOperator(bean.getTrainingReleaseId())) {
                modelMap.addAttribute("trainingAuthorities", bean);
                page = "web/training/release/training_authorities_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实训配置数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
