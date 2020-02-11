package top.zbeboy.zone.web.internship.apply;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.internship.InternshipApplyService;
import top.zbeboy.zone.service.internship.InternshipInfoService;
import top.zbeboy.zone.service.internship.InternshipTeacherDistributionService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.platform.UsersTypeService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class InternshipApplyViewController {

    @Resource
    private InternshipTeacherDistributionService internshipTeacherDistributionService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipInfoService internshipInfoService;

    @Resource
    private InternshipApplyService internshipApplyService;

    @Resource
    private UsersService usersService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private UsersTypeService usersTypeService;

    /**
     * 实习申请
     *
     * @return 实习申请页面
     */
    @GetMapping("/web/menu/internship/apply")
    public String index(ModelMap modelMap) {
        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            modelMap.put("usersType", usersType.getUsersTypeName());
        }
        return "web/internship/apply/internship_apply::#page-wrapper";
    }

    /**
     * 添加
     *
     * @param id       实习id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/apply/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.applyCondition(id)) {
            Users users = usersService.getUserFromSession();
            Optional<Record> studentRecord = studentService.findByUsernameRelation(users.getUsername());
            if (studentRecord.isPresent()) {
                StudentBean studentBean = studentRecord.get().into(StudentBean.class);
                String qqMail = "";
                if (studentBean.getEmail().toLowerCase().contains("@qq.com")) {
                    qqMail = studentBean.getEmail();
                }
                modelMap.addAttribute("qqMail", qqMail);
                modelMap.addAttribute("student", studentBean);

                Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(id, studentBean.getStudentId());
                if (internshipTeacherDistributionRecord.isPresent()) {
                    InternshipTeacherDistribution internshipTeacherDistribution = internshipTeacherDistributionRecord.get().into(InternshipTeacherDistribution.class);
                    Optional<Record> staffRecord = staffService.findByIdRelation(internshipTeacherDistribution.getStaffId());
                    if (staffRecord.isPresent()) {
                        StaffBean staffBean = staffRecord.get().into(StaffBean.class);
                        modelMap.put("internshipTeacherName", staffBean.getRealName());
                        modelMap.put("internshipTeacherMobile", staffBean.getMobile());
                        modelMap.put("internshipTeacher", staffBean.getRealName() + " " + staffBean.getMobile());
                    }
                }
            }
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/apply/internship_apply_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 我的申请
     *
     * @return 页面
     */
    @GetMapping("/web/internship/apply/my")
    public String my(ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = usersService.getUserFromSession();
        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType)) {
            if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                page = "web/internship/apply/internship_apply_my::#page-wrapper";
            } else {
                config.buildDangerTip("操作错误", "该页面仅允许学生用户使用");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到用户类型");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 实习数据编辑
     *
     * @param id       实习发布id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/apply/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.applyEditCondition(id)) {
            Users users = usersService.getUserFromSession();
            Optional<Record> studentRecord = studentService.findByUsernameRelation(users.getUsername());
            if (studentRecord.isPresent()) {
                Student student = studentRecord.get().into(Student.class);
                Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(id, student.getStudentId());
                if (internshipInfoRecord.isPresent()) {
                    InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                    Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(id, student.getStudentId());
                    if (internshipApplyRecord.isPresent()) {
                        InternshipApply internshipApply = internshipApplyRecord.get().into(InternshipApply.class);
                        modelMap.put("internshipInfo", internshipInfo);
                        modelMap.put("internshipApply", internshipApply);
                        page = "web/internship/apply/internship_apply_edit::#page-wrapper";
                    } else {
                        config.buildDangerTip("查询错误", "未查询到实习申请信息");
                        config.dataMerging(modelMap);
                        page = "inline_tip::#page-wrapper";
                    }
                } else {
                    config.buildDangerTip("查询错误", "未查询到实习数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到学生信息");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 实习申请查看
     *
     * @param id       实习发布id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/apply/look/{id}")
    public String look(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        Users users = usersService.getUserFromSession();
        Optional<Record> studentRecord = studentService.findByUsernameRelation(users.getUsername());
        if (studentRecord.isPresent()) {
            Student student = studentRecord.get().into(Student.class);
            Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(id, student.getStudentId());
            if (internshipInfoRecord.isPresent()) {
                InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                modelMap.put("internshipInfo", internshipInfo);
                page = "web/internship/apply/internship_apply_look::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到实习数据");
                config.dataMerging(modelMap);
                page = "inline_tip::#page-wrapper";
            }
        } else {
            config.buildDangerTip("查询错误", "未查询到学生信息");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }
}
