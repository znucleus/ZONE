package top.zbeboy.zone.web.educational.examination;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.domain.tables.pojos.ExaminationNoticeDetail;
import top.zbeboy.zbase.domain.tables.pojos.ExaminationNoticeRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.educational.examination.EducationalExaminationService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class ExaminationViewController {

    @Resource
    private EducationalExaminationService educationalExaminationService;

    /**
     * 考试通知
     *
     * @return 考试通知页面
     */
    @GetMapping("/web/menu/educational/examination")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.addAttribute("canRelease", educationalExaminationService.canRelease(users.getUsername()));
        return "web/educational/examination/examination_release::#page-wrapper";
    }

    /**
     * 查看页面
     *
     * @return 查看页面
     */
    @GetMapping("/web/educational/examination/look/{id}")
    public String look(@PathVariable("id") String id, ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        modelMap.put("examinationNoticeReleaseId", id);
        modelMap.addAttribute("canOperator", educationalExaminationService.canOperator(users.getUsername(), id));
        return "web/educational/examination/examination_detail::#page-wrapper";
    }

    /**
     * 添加页面
     *
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/educational/examination/add")
    public String add(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalExaminationService.canRelease(users.getUsername())) {
            page = "web/educational/examination/examination_release_add::#page-wrapper";
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
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/educational/examination/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalExaminationService.canOperator(users.getUsername(), id)) {
            Optional<ExaminationNoticeRelease> optionalExaminationNoticeRelease = educationalExaminationService.findReleaseById(id);
            if (optionalExaminationNoticeRelease.isPresent()) {
                modelMap.addAttribute("examinationNoticeRelease", optionalExaminationNoticeRelease.get());
                page = "web/educational/examination/examination_release_edit::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到数据");
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
     * 详情添加页面
     *
     * @param id       发布id
     * @param modelMap 页面对象
     * @return 添加页面
     */
    @GetMapping("/web/educational/examination/detail/add/{id}")
    public String detailAdd(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        if (educationalExaminationService.canOperator(users.getUsername(), id)) {
            modelMap.put("examinationNoticeReleaseId", id);
            page = "web/educational/examination/examination_detail_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 详情编辑页面
     *
     * @param modelMap 页面对象
     * @return 编辑页面
     */
    @GetMapping("/web/educational/examination/detail/edit/{id}")
    public String detailEdit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        Optional<ExaminationNoticeDetail> optionalExaminationNoticeDetail = educationalExaminationService.findDetailById(id);
        if (optionalExaminationNoticeDetail.isPresent()) {
            ExaminationNoticeDetail examinationNoticeDetail = optionalExaminationNoticeDetail.get();
            if (educationalExaminationService.canOperator(users.getUsername(), examinationNoticeDetail.getExaminationNoticeReleaseId())) {
                modelMap.addAttribute("examinationNoticeDetail", examinationNoticeDetail);
                page = "web/educational/examination/examination_detail_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }

        return page;
    }
}
