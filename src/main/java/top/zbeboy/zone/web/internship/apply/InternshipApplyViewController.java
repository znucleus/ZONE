package top.zbeboy.zone.web.internship.apply;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.*;
import top.zbeboy.zbase.feign.data.OrganizeService;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zone.service.internship.InternshipApplyService;
import top.zbeboy.zone.service.internship.InternshipInfoService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipTeacherDistributionService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

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
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private OrganizeService organizeService;

    /**
     * 实习申请
     *
     * @return 实习申请页面
     */
    @GetMapping("/web/menu/internship/apply")
    public String index(ModelMap modelMap) {
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
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
            Users users = SessionUtil.getUserFromSession();
            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                String qqMail = "";
                if (studentBean.getEmail().toLowerCase().contains("@qq.com")) {
                    qqMail = studentBean.getEmail();
                }
                modelMap.addAttribute("qqMail", qqMail);
                modelMap.addAttribute("student", studentBean);

                Optional<Organize> optionalOrganize = organizeService.findById(studentBean.getOrganizeId());
                if (optionalOrganize.isPresent() && Objects.nonNull(optionalOrganize.get().getStaffId())) {
                    Optional<StaffBean> optionalStaffBean = staffService.findByIdRelation(optionalOrganize.get().getStaffId());
                    if (optionalStaffBean.isPresent()) {
                        StaffBean bean = optionalStaffBean.get();
                        modelMap.addAttribute("headmaster", bean.getRealName());
                        modelMap.addAttribute("headmasterTel", bean.getMobile());
                    }

                }

                Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(id, studentBean.getStudentId());
                if (internshipTeacherDistributionRecord.isPresent()) {
                    InternshipTeacherDistribution internshipTeacherDistribution = internshipTeacherDistributionRecord.get().into(InternshipTeacherDistribution.class);
                    Optional<StaffBean> optionalStaffBean = staffService.findByIdRelation(internshipTeacherDistribution.getStaffId());
                    if (optionalStaffBean.isPresent()) {
                        StaffBean bean = optionalStaffBean.get();
                        modelMap.put("internshipTeacher", bean.getRealName() + " " + bean.getMobile());

                        Optional<Record> internshipReleaseRecord = internshipReleaseService.findByIdRelation(id);
                        if (internshipReleaseRecord.isPresent()) {
                            InternshipReleaseBean internshipRelease = internshipReleaseRecord.get().into(InternshipReleaseBean.class);
                            if (StringUtils.equals(Workbook.POST_PRACTICE_INTERNSHIP_TYPE, internshipRelease.getInternshipTypeName()) ||
                                    StringUtils.equals(Workbook.GRADUATION_PRACTICE_IN_SCHOOL_INTERNSHIP_TYPE, internshipRelease.getInternshipTypeName())) {
                                modelMap.put("companyName", studentBean.getSchoolName() + studentBean.getCollegeName());
                                modelMap.put("companyAddress", studentBean.getCollegeAddress());
                                modelMap.put("companyContact", bean.getRealName());
                                modelMap.put("companyMobile", bean.getMobile());
                            }
                        }
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
        Users users = SessionUtil.getUserFromSession();
        Optional<UsersType> optionalUsersType = usersTypeService.findById(users.getUsersTypeId());
        if (optionalUsersType.isPresent()) {
            UsersType usersType = optionalUsersType.get();
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
            Users users = SessionUtil.getUserFromSession();
            Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
            if (optionalStudentBean.isPresent()) {
                StudentBean studentBean = optionalStudentBean.get();
                Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(id, studentBean.getStudentId());
                if (internshipInfoRecord.isPresent()) {
                    InternshipInfo internshipInfo = internshipInfoRecord.get().into(InternshipInfo.class);
                    Optional<Record> internshipApplyRecord = internshipApplyService.findByInternshipReleaseIdAndStudentId(id, studentBean.getStudentId());
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
        Users users = SessionUtil.getUserFromSession();
        Optional<StudentBean> optionalStudentBean = studentService.findByUsernameRelation(users.getUsername());
        if (optionalStudentBean.isPresent()) {
            StudentBean studentBean = optionalStudentBean.get();
            Optional<Record> internshipInfoRecord = internshipInfoService.findByInternshipReleaseIdAndStudentId(id, studentBean.getStudentId());
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
