package top.zbeboy.zone.web.internship.journal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zbeboy.zbase.bean.data.staff.StaffBean;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.bean.internship.journal.InternshipJournalContentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.InternshipJournal;
import top.zbeboy.zbase.domain.tables.pojos.InternshipJournalContent;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.domain.tables.pojos.UsersType;
import top.zbeboy.zbase.domain.tables.records.InternshipJournalContentRecord;
import top.zbeboy.zbase.feign.data.StaffService;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.feign.platform.RoleService;
import top.zbeboy.zbase.feign.platform.UsersTypeService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zone.service.internship.InternshipJournalContentService;
import top.zbeboy.zone.service.internship.InternshipJournalService;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.system.tip.SystemInlineTipConfig;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

@Controller
public class InternshipJournalViewController {

    @Resource
    private UsersTypeService usersTypeService;

    @Resource
    private StudentService studentService;

    @Resource
    private StaffService staffService;

    @Resource
    private RoleService roleService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private InternshipJournalService internshipJournalService;

    @Resource
    private InternshipJournalContentService internshipJournalContentService;

    /**
     * 实习日志
     *
     * @return 实习日志页面
     */
    @GetMapping("/web/menu/internship/journal")
    public String index() {
        return "web/internship/journal/internship_journal::#page-wrapper";
    }

    /**
     * 日志列表
     *
     * @return 实习日志页面
     */
    @GetMapping("/web/internship/journal/list/{id}")
    public String list(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("internshipReleaseId", id);
        Users users = SessionUtil.getUserFromSession();
        if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_SYSTEM.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_SYSTEM.name());
        } else if (roleService.isCurrentUserInRole(users.getUsername(), Workbook.authorities.ROLE_ADMIN.name())) {
            modelMap.addAttribute("authorities", Workbook.authorities.ROLE_ADMIN.name());
        }

        UsersType usersType = usersTypeService.findById(users.getUsersTypeId());
        if (Objects.nonNull(usersType.getUsersTypeId()) && usersType.getUsersTypeId() > 0) {
            modelMap.addAttribute("usersTypeName", usersType.getUsersTypeName());

            if (StringUtils.equals(Workbook.STAFF_USERS_TYPE, usersType.getUsersTypeName())) {
                StaffBean bean = staffService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
                    modelMap.addAttribute("staffId", bean.getStaffId());
                }
            } else if (StringUtils.equals(Workbook.STUDENT_USERS_TYPE, usersType.getUsersTypeName())) {
                StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
                if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                    modelMap.addAttribute("studentId", studentBean.getStudentId());
                }
            }
        }
        if (internshipConditionCommon.journalCondition(id)) {
            modelMap.addAttribute("canAdd", 1);
        }
        return "web/internship/journal/internship_journal_list::#page-wrapper";
    }

    /**
     * 添加
     *
     * @param id       实习发布id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/journal/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.journalCondition(id)) {
            modelMap.addAttribute("internshipReleaseId", id);
            page = "web/internship/journal/internship_journal_add::#page-wrapper";
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
     * @param id       实习日志id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/journal/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.journalEditCondition(id)) {
            InternshipJournal internshipJournal = internshipJournalService.findById(id);
            if (Objects.nonNull(internshipJournal)) {
                modelMap.addAttribute("internshipJournal", internshipJournal);
                Optional<InternshipJournalContentRecord> record = internshipJournalContentService.findByInternshipJournalId(internshipJournal.getInternshipJournalId());
                if (record.isPresent()) {
                    InternshipJournalContent internshipJournalContent = record.get().into(InternshipJournalContent.class);
                    modelMap.addAttribute("internshipJournalContent", internshipJournalContent);
                    page = "web/internship/journal/internship_journal_edit::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到实习日志数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到实习日志信息");
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
     * 查看
     *
     * @param id       实习日志id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/journal/look/{id}")
    public String look(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.journalLookCondition(id)) {
            InternshipJournal internshipJournal = internshipJournalService.findById(id);
            if (Objects.nonNull(internshipJournal)) {
                modelMap.addAttribute("internshipJournal", internshipJournal);
                Optional<InternshipJournalContentRecord> record = internshipJournalContentService.findByInternshipJournalId(internshipJournal.getInternshipJournalId());
                if (record.isPresent()) {
                    InternshipJournalContentBean internshipJournalContent = record.get().into(InternshipJournalContentBean.class);
                    internshipJournalContent.setInternshipJournalDateStr(DateTimeUtil.formatSqlDate(internshipJournalContent.getInternshipJournalDate(), DateTimeUtil.YEAR_MONTH_DAY_CN_FORMAT));
                    modelMap.addAttribute("internshipJournalContent", internshipJournalContent);
                    page = "web/internship/journal/internship_journal_look::#page-wrapper";
                } else {
                    config.buildDangerTip("查询错误", "未查询到实习日志数据");
                    config.dataMerging(modelMap);
                    page = "inline_tip::#page-wrapper";
                }
            } else {
                config.buildDangerTip("查询错误", "未查询到实习日志信息");
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
     * 小组统计数据
     *
     * @param id       实习发布id
     * @param staffId  教职工id
     * @param modelMap 页面对象
     * @return 页面
     */
    @GetMapping("/web/internship/journal/statistical/{id}/{staffId}")
    public String statistical(@PathVariable("id") String id, @PathVariable("staffId") int staffId, ModelMap modelMap) {
        modelMap.addAttribute("internshipReleaseId", id);
        modelMap.addAttribute("staffId", staffId);
        StaffBean bean = staffService.findByIdRelation(staffId);
        if (Objects.nonNull(bean.getStaffId()) && bean.getStaffId() > 0) {
            modelMap.addAttribute("realName", bean.getRealName());
        }
        return "web/internship/journal/internship_journal_statistical::#page-wrapper";
    }

    /**
     * 日志列表
     *
     * @return 实习日志页面
     */
    @GetMapping("/web/internship/journal/my/list/{id}")
    public String myList(@PathVariable("id") String id, ModelMap modelMap) {
        SystemInlineTipConfig config = new SystemInlineTipConfig();
        String page;
        if (internshipConditionCommon.journalLookMyCondition(id)) {
            Users users = SessionUtil.getUserFromSession();
            StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
            if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                modelMap.addAttribute("internshipReleaseId", id);
                modelMap.addAttribute("studentId", studentBean.getStudentId());
                page = "web/internship/journal/internship_journal_my::#page-wrapper";
            } else {
                config.buildDangerTip("查询错误", "未查询到学生信息");
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
