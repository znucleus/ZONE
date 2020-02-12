package top.zbeboy.zone.web.internship.distribution;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.*;
import top.zbeboy.zone.service.data.OrganizeService;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.export.InternshipTeacherDistributionExport;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipTeacherDistributionService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.upload.UploadService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.web.bean.data.organize.OrganizeBean;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.bean.internship.distribution.InternshipTeacherDistributionBean;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.internship.common.InternshipControllerCommon;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class InternshipTeacherDistributionRestController {

    @Resource
    private InternshipTeacherDistributionService internshipTeacherDistributionService;

    @Resource
    private InternshipControllerCommon internshipControllerCommon;

    @Resource
    private InternshipReleaseService internshipReleaseService;

    @Resource
    private InternshipConditionCommon internshipConditionCommon;

    @Resource
    private StaffService staffService;

    @Resource
    private StudentService studentService;

    @Resource
    private UsersService usersService;

    @Resource
    private OrganizeService organizeService;

    @Resource
    private UploadService uploadService;

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/teacher_distribution/internship/data")
    public ResponseEntity<Map<String, Object>> internshipData(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReleaseBean> ajaxUtil = AjaxUtil.of();
        List<InternshipReleaseBean> beans = new ArrayList<>();
        Result<Record> records = internshipReleaseService.findAllByPage(simplePaginationUtil);
        if (records.isNotEmpty()) {
            beans = records.into(InternshipReleaseBean.class);
            beans.forEach(bean -> bean.setTeacherDistributionStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionStartTime())));
            beans.forEach(bean -> bean.setTeacherDistributionEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getTeacherDistributionEndTime())));
            beans.forEach(bean -> bean.setStartTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getStartTime())));
            beans.forEach(bean -> bean.setEndTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getEndTime())));
            beans.forEach(bean -> bean.setReleaseTimeStr(DateTimeUtil.defaultFormatSqlTimestamp(bean.getReleaseTime())));
            beans.forEach(bean -> bean.setCanOperator(BooleanUtil.toByte(internshipConditionCommon.teacherDistributionCondition(bean.getInternshipReleaseId()))));
        }
        simplePaginationUtil.setTotalSize(internshipReleaseService.countAll(simplePaginationUtil));
        ajaxUtil.success().list(beans).page(simplePaginationUtil).msg("获取数据成功");
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/internship/teacher_distribution/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("studentRealName");
        headers.add("studentUsername");
        headers.add("studentNumber");
        headers.add("staffRealName");
        headers.add("staffUsername");
        headers.add("staffNumber");
        headers.add("assigner");
        headers.add("username");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        List<InternshipTeacherDistributionBean> beans = internshipTeacherDistributionService.findAllByPage(dataTablesUtil);
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(internshipTeacherDistributionService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(internshipTeacherDistributionService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 教师数据
     *
     * @param id 实习id
     * @return 页面
     */
    @GetMapping("/web/internship/teacher_distribution/staff/{id}")
    public ResponseEntity<Map<String, Object>> staff(@PathVariable("id") String id) {
        Select2Data select2Data = Select2Data.of();
        List<StaffBean> beans = internshipControllerCommon.internshipReleaseStaffData(id);
        beans.forEach(bean -> select2Data.add(bean.getStaffId().toString(), bean.getRealName() + " " + bean.getStaffNumber()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 批量分配 所需班级数据
     *
     * @param id 实习id
     * @return 页面
     */
    @GetMapping("/web/internship/teacher_distribution/organizes/{id}")
    public ResponseEntity<Map<String, Object>> organizes(@PathVariable("id") String id) {
        Select2Data select2Data = Select2Data.of();
        List<OrganizeBean> beans = new ArrayList<>();
        Optional<Record> record = internshipReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            Science science = record.get().into(Science.class);
            Result<Record> organizeRecord = organizeService.findNormalByScienceId(science.getScienceId());
            if (organizeRecord.isNotEmpty()) {
                beans = organizeRecord.into(OrganizeBean.class);
            }
        }
        beans.forEach(bean -> select2Data.add(bean.getOrganizeId().toString(), bean.getOrganizeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 保存时检验学生账号信息
     *
     * @param internshipReleaseId 实习发布Id
     * @param info                信息
     * @param type                检验类型
     * @return true or false
     */
    @PostMapping("/web/internship/teacher_distribution/check/add/student")
    public ResponseEntity<Map<String, Object>> checkAddStudent(@RequestParam("id") String internshipReleaseId, @RequestParam("student") String info, int type) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(info);
        Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
        if (record.isPresent()) {
            Department department = record.get().into(Department.class);
            Optional<Record> studentRecord = Optional.empty();
            if (type == 0) {
                studentRecord = studentService.findNormalByUsernameAndDepartmentId(param, department.getDepartmentId());
            } else if (type == 1) {
                studentRecord = studentService.findNormalByStudentNumberAndDepartmentId(param, department.getDepartmentId());
            }
            if (studentRecord.isPresent()) {
                Student student = studentRecord.get().into(Student.class);
                Optional<Record> distribution = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, student.getStudentId());
                if (distribution.isPresent()) {
                    ajaxUtil.fail().msg("该学生账号已分配指导教师");
                } else {
                    ajaxUtil.success().msg("可分配");
                }
            } else {
                ajaxUtil.fail().msg("未查询到学生信息");
            }
        } else {
            ajaxUtil.fail().msg("根据实习发布ID未查询到实习发布数据");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 单个添加保存
     *
     * @param info                学生信息
     * @param staffId             教职工id
     * @param internshipReleaseId 实习发布id
     * @param type                检验类型
     * @return true or false
     */
    @PostMapping("/web/internship/teacher_distribution/save")
    public ResponseEntity<Map<String, Object>> save(@RequestParam("student") String info, @RequestParam("staffId") int staffId,
                                                    @RequestParam("id") String internshipReleaseId, int type) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(info);
        Optional<Record> record = internshipReleaseService.findByIdRelation(internshipReleaseId);
        if (record.isPresent()) {
            if (internshipConditionCommon.teacherDistributionCondition(internshipReleaseId)) {
                Department department = record.get().into(Department.class);
                Optional<Record> studentRecord = Optional.empty();
                if (type == 0) {
                    studentRecord = studentService.findNormalByUsernameAndDepartmentId(param, department.getDepartmentId());
                } else if (type == 1) {
                    studentRecord = studentService.findNormalByStudentNumberAndDepartmentId(param, department.getDepartmentId());
                }
                if (studentRecord.isPresent()) {
                    StudentBean student = studentRecord.get().into(StudentBean.class);
                    Users users = usersService.getUserFromSession();
                    InternshipTeacherDistribution internshipTeacherDistribution = new InternshipTeacherDistribution(staffId, student.getStudentId(), internshipReleaseId, users.getUsername(), student.getRealName(), users.getUsername());
                    internshipTeacherDistributionService.save(internshipTeacherDistribution);
                    ajaxUtil.success().msg("保存成功");
                } else {
                    ajaxUtil.fail().msg("未查询到学生信息");
                }
            } else {
                ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
            }
        } else {
            ajaxUtil.fail().msg("根据实习发布ID未查询到实习发布数据");
        }

        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存教师分配
     *
     * @param internshipReleaseId 实习id
     * @param organizeId          班级ids
     * @param staffId             教职工ids
     * @return true or false
     */
    @PostMapping("/web/internship/teacher_distribution/distribution/save")
    public ResponseEntity<Map<String, Object>> batchSave(@RequestParam("internshipReleaseId") String internshipReleaseId, String organizeId, String staffId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.teacherDistributionCondition(internshipReleaseId)) {
            List<Integer> organizeIds = SmallPropsUtil.StringIdsToNumberList(organizeId);
            List<Integer> staffIds = SmallPropsUtil.StringIdsToNumberList(staffId);

            // 删除以前的分配记录
            internshipTeacherDistributionService.deleteByInternshipReleaseId(internshipReleaseId);

            List<InternshipTeacherDistribution> internshipTeacherDistributions = new ArrayList<>();
            Result<Record> studentRecords = studentService.findNormalInOrganizeIds(organizeIds);
            if (studentRecords.isNotEmpty() && staffIds.size() > 0) {
                List<StudentBean> students = studentRecords.into(StudentBean.class);
                int i = 0;
                Users users = usersService.getUserFromSession();
                for (StudentBean student : students) {
                    if (i >= staffIds.size()) {
                        i = 0;
                    }
                    internshipTeacherDistributions.add(new InternshipTeacherDistribution(staffIds.get(i), student.getStudentId(), internshipReleaseId, users.getUsername(),
                            student.getRealName(), users.getRealName()));
                    i++;

                }
            }
            internshipTeacherDistributionService.batchSave(internshipTeacherDistributions);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 更新
     *
     * @param studentId           学生id
     * @param staffId             教职工id
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    @PostMapping("/web/internship/teacher_distribution/update")
    public ResponseEntity<Map<String, Object>> update(@RequestParam("studentId") int studentId, @RequestParam("staffId") int staffId,
                                                      @RequestParam("id") String internshipReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.teacherDistributionCondition(internshipReleaseId)) {
            Optional<Record> internshipTeacherDistributionRecord = internshipTeacherDistributionService.findByInternshipReleaseIdAndStudentId(internshipReleaseId, studentId);
            if (internshipTeacherDistributionRecord.isPresent()) {
                InternshipTeacherDistribution internshipTeacherDistribution = internshipTeacherDistributionRecord.get().into(InternshipTeacherDistribution.class);
                internshipTeacherDistribution.setStaffId(staffId);
                internshipTeacherDistributionService.updateStaff(internshipTeacherDistribution);
                ajaxUtil.success().msg("保存成功");
            } else {
                ajaxUtil.fail().msg("分配数据中未查询到该学生");
            }
        } else {
            ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量删除
     *
     * @param studentIds          学生ids
     * @param internshipReleaseId 实习id
     * @return true or false
     */
    @PostMapping("/web/internship/teacher_distribution/delete")
    public ResponseEntity<Map<String, Object>> delete(String studentIds, @RequestParam("id") String internshipReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.teacherDistributionCondition(internshipReleaseId)) {
            if (StringUtils.isNotBlank(studentIds)) {
                List<Integer> ids = SmallPropsUtil.StringIdsToNumberList(studentIds);
                ids.forEach(id -> internshipTeacherDistributionService.deleteByInternshipReleaseIdAndStudentId(internshipReleaseId, id));
                ajaxUtil.success().msg("删除成功");
            } else {
                ajaxUtil.fail().msg("请选择学生");
            }
        } else {
            ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 导出 分配列表 数据
     *
     * @param request 请求
     */
    @GetMapping("/web/internship/teacher_distribution/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, "studentNumber", "asc",
                "实习指导教师分配数据表", Workbook.internshipFilePath());
        List<InternshipTeacherDistributionBean> internshipTeacherDistributionBeans = internshipTeacherDistributionService.export(dataTablesUtil);
        InternshipTeacherDistributionExport export = new InternshipTeacherDistributionExport(internshipTeacherDistributionBeans);
        DataTablesUtil.ExportInfo exportInfo = dataTablesUtil.getExportInfo();
        if (export.exportExcel(exportInfo.getLastPath(), exportInfo.getFileName(), exportInfo.getExt())) {
            uploadService.download(exportInfo.getFileName(), exportInfo.getFilePath(), response, request);
        }
    }

    /**
     * 删除未申请学生的分配
     *
     * @param internshipReleaseId 实习发布id
     * @return true or false
     */
    @PostMapping("/web/internship/teacher_distribution/distribution/delete_not_apply")
    public ResponseEntity<Map<String, Object>> deleteNotApply(@RequestParam("id") String internshipReleaseId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (internshipConditionCommon.canOperator(internshipReleaseId)) {
            internshipTeacherDistributionService.deleteNotApply(internshipReleaseId);
            ajaxUtil.success().msg("删除成功");
        } else {
            ajaxUtil.fail().msg("您无权限或当前实习不允许操作");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
