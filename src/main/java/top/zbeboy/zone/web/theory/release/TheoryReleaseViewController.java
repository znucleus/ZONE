package top.zbeboy.zone.web.theory.release;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.theory.release.TheoryAuthoritiesBean;
import top.zbeboy.zbase.bean.theory.release.TheoryConfigureBean;
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.domain.tables.pojos.TheoryRelease;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zone.service.theory.TheoryAuthoritiesService;
import top.zbeboy.zone.service.theory.TheoryConfigureService;
import top.zbeboy.zone.service.theory.TheoryReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.theory.common.TheoryConditionCommon;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TheoryReleaseViewController {

    @Resource
    private TheoryReleaseService theoryReleaseService;

    @Resource
    private TheoryConditionCommon theoryConditionCommon;

    @Resource
    private TheoryConfigureService theoryConfigureService;

    @Resource
    private TheoryAuthoritiesService theoryAuthoritiesService;


    /**
     * 发布主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/theory/release")
    public String index() {
        return "web/theory/release/theory_release::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @return 添加页面
     */
    @GetMapping("/web/theory/release/add")
    public String add() {
        return "web/theory/release/theory_release_add::#page-wrapper";
    }

    /**
     * 编辑
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/theory/release/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.canOperator(id)) {
            Optional<Record> record = theoryReleaseService.findByIdRelation(id);
            if (record.isPresent()) {
                TheoryReleaseBean bean = record.get().into(TheoryReleaseBean.class);
                modelMap.addAttribute("theoryRelease", bean);
                page = "web/theory/release/theory_release_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/release/configure/{id}")
    public String configure(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.canOperator(id)) {
            TheoryRelease theoryRelease = theoryReleaseService.findById(id);
            if (Objects.nonNull(theoryRelease)) {
                modelMap.addAttribute("theoryReleaseId", id);
                page = "web/theory/release/theory_configure::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/release/configure/add/{id}")
    public String configureAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.canOperator(id)) {
            Optional<Record> theoryReleaseRecord = theoryReleaseService.findByIdRelation(id);
            if (theoryReleaseRecord.isPresent()) {
                TheoryReleaseBean bean = theoryReleaseRecord.get().into(TheoryReleaseBean.class);
                modelMap.addAttribute("theoryReleaseId", bean.getTheoryReleaseId());
                modelMap.addAttribute("collegeId", bean.getCollegeId());
                page = "web/theory/release/theory_configure_add::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/release/configure/edit/{id}")
    public String configureEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = theoryConfigureService.findByIdRelation(id);
        if (record.isPresent()) {
            TheoryConfigureBean bean = record.get().into(TheoryConfigureBean.class);
            if (theoryConditionCommon.canOperator(bean.getTheoryReleaseId())) {
                modelMap.addAttribute("theoryConfigure", bean);
                page = "web/theory/release/theory_configure_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到理论配置数据");
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
    @GetMapping("/web/theory/release/authorities/{id}")
    public String authorities(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.canOperator(id)) {
            TheoryRelease theoryRelease = theoryReleaseService.findById(id);
            if (Objects.nonNull(theoryRelease)) {
                modelMap.addAttribute("theoryReleaseId", id);
                page = "web/theory/release/theory_authorities::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/release/authorities/add/{id}")
    public String authoritiesAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.canOperator(id)) {
            Optional<Record> theoryReleaseRecord = theoryReleaseService.findByIdRelation(id);
            if (theoryReleaseRecord.isPresent()) {
                TheoryReleaseBean bean = theoryReleaseRecord.get().into(TheoryReleaseBean.class);
                modelMap.addAttribute("theoryReleaseId", bean.getTheoryReleaseId());
                page = "web/theory/release/theory_authorities_add::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/release/authorities/edit/{id}")
    public String authoritiesEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = theoryAuthoritiesService.findByIdRelation(id);
        if (record.isPresent()) {
            TheoryAuthoritiesBean bean = record.get().into(TheoryAuthoritiesBean.class);
            bean.setValidDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getValidDate()));
            bean.setExpireDateStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getExpireDate()));
            if (theoryConditionCommon.canOperator(bean.getTheoryReleaseId())) {
                modelMap.addAttribute("theoryAuthorities", bean);
                page = "web/theory/release/theory_authorities_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到理论配置数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
