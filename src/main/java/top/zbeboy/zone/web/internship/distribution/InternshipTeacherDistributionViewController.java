package top.zbeboy.zone.web.internship.distribution;

import org.jooq.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.domain.tables.pojos.InternshipTeacherDistribution;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zone.service.internship.InternshipTeacherDistributionService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class InternshipTeacherDistributionViewController {

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipTeacherDistributionService internshipTeacherDistributionService;

    @Resource
    private StudentService studentService;

    /**
     * 实习教师分配
     *
     * @return 实习教师分配页面
     */
    @GetMapping("/web/menu/internship/teacher-distribution")
    public String index() {
        return "web/internship/distribution/internship_teacher_distribution::#page-wrapper";
    }

    /**
     * 教师分配页面
     *
     * @param id 实习发布id
     * @return 页面
     */
    @GetMapping("/web/internship/teacher-distribution/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.teacherDistributionCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/distribution/internship_distribution_list::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 查看分配页面
     *
     * @param id 实习发布id
     * @return 页面
     */
    @GetMapping("/web/internship/teacher-distribution/look/{id}")
    public String look(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.basicCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/distribution/internship_distribution_look::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 添加
     *
     * @param id       实习id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/teacher-distribution/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.teacherDistributionCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/distribution/internship_distribution_add::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 批量分配
     *
     * @param id       实习id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/teacher-distribution/distribution/{id}")
    public String distribution(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.teacherDistributionCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/distribution/internship_distribution::#page-wrapper";
        } else {
            config.buildWarningTip("操作警告", "您无权限或当前实习不允许操作");
            config.dataMerging(modelMap);
            page = "inline_tip::#page-wrapper";
        }
        return page;
    }

    /**
     * 编辑
     *
     * @param id        实习发布id
     * @param studentId 学生id
     * @param modelMap  页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/teacher-distribution/edit/{id}/{studentId}")
    public String edit(@PathVariable("id") String id, @PathVariable("studentId") int studentId, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.teacherDistributionCondition(id)) {
            Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(id, studentId);
            if (internshipTeacherDistributionRecord.isPresent()) {
                InternshipTeacherDistribution internshipTeacherDistribution = internshipTeacherDistributionRecord.get().into(InternshipTeacherDistribution.class);
                StudentBean studentBean = studentService.findByIdRelation(studentId);
                if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                    modelMap.addAttribute("internshipReleaseId", internshipTeacherDistribution.getInternshipReleaseId());
                    modelMap.addAttribute("staffId", internshipTeacherDistribution.getStaffId());
                    modelMap.addAttribute("studentRealName", studentBean.getRealName());
                    modelMap.addAttribute("studentNumber", studentBean.getStudentNumber());
                    modelMap.addAttribute("organizeName", studentBean.getOrganizeName());
                    page = "web/internship/distribution/internship_distribution_edit::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到学生信息");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "分配数据中未查询到该学生");
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
}
