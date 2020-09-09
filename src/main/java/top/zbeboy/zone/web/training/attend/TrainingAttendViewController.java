package top.zbeboy.zone.web.training.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.training.attend.TrainingAttendBean;
import top.zbeboy.zbase.bean.training.release.TrainingReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.TrainingRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.service.training.TrainingAttendService;
import top.zbeboy.zone.service.training.TrainingReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.training.common.TrainingConditionCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TrainingAttendViewController {

    @Resource
    private TrainingReleaseService trainingReleaseService;

    @Resource
    private TrainingConditionCommon trainingConditionCommon;

    @Resource
    private TrainingAttendService trainingAttendService;

    @Resource
    private UsersTypeService usersTypeService;

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/training/attend")
    public String index() {
        return "web/training/attend/training_attend::#page-wrapper";
    }

    /**
     * 列表
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/attend/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            boolean isStudent = false;
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    isStudent = true;
                }
            }
            modelMap.addAttribute("trainingReleaseId", id);
            modelMap.addAttribute("canOperator", BooleanUtil.toByte(trainingConditionCommon.usersCondition(id)));
            modelMap.addAttribute("isStudent", BooleanUtil.toByte(isStudent));
            page = "web/training/attend/training_attend_list::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训发布数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 自定义发布
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/attend/release/{id}")
    public String release(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.usersCondition(id)) {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(id);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);
                modelMap.addAttribute("trainingReleaseId", bean.getTrainingReleaseId());
                modelMap.addAttribute("collegeId", bean.getCollegeId());
                page = "web/training/attend/training_attend_release::#page-wrapper";
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
     * 编辑
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/attend/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingAttendService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingAttendBean bean = record.get().into(TrainingAttendBean.class);
            if (trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())) {
                modelMap.addAttribute("trainingAttend", bean);
                page = "web/training/attend/training_attend_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到实训考勤数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 列表
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/attend/users/list/{id}")
    public String usersList(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = trainingAttendService.findByIdRelation(id);
        if (record.isPresent()) {
            TrainingAttendBean bean = record.get().into(TrainingAttendBean.class);
            modelMap.addAttribute("trainingReleaseId", bean.getTrainingReleaseId());
            modelMap.addAttribute("trainingAttendId", id);
            modelMap.addAttribute("canOperator", BooleanUtil.toByte(trainingConditionCommon.usersCondition(bean.getTrainingReleaseId())));
            page = "web/training/attend/training_attend_users::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到实训考勤数据");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 我的考勤
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/attend/my/{id}")
    public String my(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                TrainingRelease trainingRelease = trainingReleaseService.findById(id);
                if (Objects.nonNull(trainingRelease)) {
                    modelMap.addAttribute("trainingReleaseId", id);
                    page = "web/training/attend/training_attend_my::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到实训发布数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到学生信息");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到注册类型信息");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 考勤情况
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/training/attend/situation/{id}")
    public String situation(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (trainingConditionCommon.usersCondition(id)) {
            Optional<Record> trainingReleaseRecord = trainingReleaseService.findByIdRelation(id);
            if (trainingReleaseRecord.isPresent()) {
                TrainingReleaseBean bean = trainingReleaseRecord.get().into(TrainingReleaseBean.class);
                modelMap.addAttribute("trainingReleaseId", bean.getTrainingReleaseId());
                page = "web/training/attend/training_attend_situation::#page-wrapper";
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
}
