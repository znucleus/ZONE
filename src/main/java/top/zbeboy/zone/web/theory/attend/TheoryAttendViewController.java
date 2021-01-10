package top.zbeboy.zone.web.theory.attend;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.theory.attend.TheoryAttendBean;
import top.zbeboy.zbase.bean.theory.release.TheoryReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.TheoryRelease;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.service.theory.TheoryAttendService;
import top.zbeboy.zone.service.theory.TheoryReleaseService;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.theory.common.TheoryConditionCommon;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TheoryAttendViewController {
    @Resource
    private TheoryReleaseService theoryReleaseService;

    @Resource
    private TheoryConditionCommon theoryConditionCommon;

    @Resource
    private TheoryAttendService theoryAttendService;

    @Resource
    private UsersTypeService usersTypeService;

    /**
     * 主页
     *
     * @return 页面
     */
    @GetMapping("/web/menu/theory/attend")
    public String index() {
        return "web/theory/attend/theory_attend::#page-wrapper";
    }

    /**
     * 列表
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/theory/attend/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = theoryReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            boolean isStudent = false;
            Users users = SessionUtil.getUserFromSession();
            UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
            if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
                if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                    isStudent = true;
                }
            }
            modelMap.addAttribute("theoryReleaseId", id);
            modelMap.addAttribute("canOperator", BooleanUtil.toByte(theoryConditionCommon.usersCondition(id)));
            modelMap.addAttribute("isStudent", BooleanUtil.toByte(isStudent));
            page = "web/theory/attend/theory_attend_list::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/attend/release/{id}")
    public String release(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.usersCondition(id)) {
            Optional<Record> theoryReleaseRecord = theoryReleaseService.findByIdRelation(id);
            if (theoryReleaseRecord.isPresent()) {
                TheoryReleaseBean bean = theoryReleaseRecord.get().into(TheoryReleaseBean.class);
                modelMap.addAttribute("theoryReleaseId", bean.getTheoryReleaseId());
                modelMap.addAttribute("collegeId", bean.getCollegeId());
                page = "web/theory/attend/theory_attend_release::#page-wrapper";
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
     * 编辑
     *
     * @param id       id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/theory/attend/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = theoryAttendService.findByIdRelation(id);
        if (record.isPresent()) {
            TheoryAttendBean bean = record.get().into(TheoryAttendBean.class);
            if (theoryConditionCommon.usersCondition(bean.getTheoryReleaseId())) {
                modelMap.addAttribute("theoryAttend", bean);
                page = "web/theory/attend/theory_attend_edit::#page-wrapper";
            } else {
                config.buildWarningTip("操作警告", "您无权限操作");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到理论考勤数据");
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
    @GetMapping("/web/theory/attend/users/list/{id}")
    public String usersList(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Optional<Record> record = theoryAttendService.findByIdRelation(id);
        if (record.isPresent()) {
            TheoryAttendBean bean = record.get().into(TheoryAttendBean.class);
            modelMap.addAttribute("theoryReleaseId", bean.getTheoryReleaseId());
            modelMap.addAttribute("theoryAttendId", id);
            modelMap.addAttribute("canOperator", BooleanUtil.toByte(theoryConditionCommon.usersCondition(bean.getTheoryReleaseId())));
            page = "web/theory/attend/theory_attend_users::#page-wrapper";
        } else {
            config.buildDangerTip("查询错误", "未查询到理论考勤数据");
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
    @GetMapping("/web/theory/attend/my/{id}")
    public String my(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = SessionUtil.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                TheoryRelease theoryRelease = theoryReleaseService.findById(id);
                if (Objects.nonNull(theoryRelease)) {
                    modelMap.addAttribute("theoryReleaseId", id);
                    page = "web/theory/attend/theory_attend_my::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到理论发布数据");
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
    @GetMapping("/web/theory/attend/situation/{id}")
    public String situation(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (theoryConditionCommon.usersCondition(id)) {
            Optional<Record> theoryReleaseRecord = theoryReleaseService.findByIdRelation(id);
            if (theoryReleaseRecord.isPresent()) {
                TheoryReleaseBean bean = theoryReleaseRecord.get().into(TheoryReleaseBean.class);
                modelMap.addAttribute("theoryReleaseId", bean.getTheoryReleaseId());
                page = "web/theory/attend/theory_attend_situation::#page-wrapper";
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
}
