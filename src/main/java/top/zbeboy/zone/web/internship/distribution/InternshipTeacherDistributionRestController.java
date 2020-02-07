package top.zbeboy.zone.web.internship.distribution;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Department;
import top.zbeboy.zone.domain.tables.pojos.InternshipTeacherDistribution;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.data.StaffService;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.internship.InternshipReleaseService;
import top.zbeboy.zone.service.internship.InternshipTeacherDistributionService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.web.bean.data.staff.StaffBean;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.bean.internship.distribution.InternshipTeacherDistributionBean;
import top.zbeboy.zone.web.bean.internship.release.InternshipReleaseBean;
import top.zbeboy.zone.web.internship.common.InternshipConditionCommon;
import top.zbeboy.zone.web.internship.common.InternshipControllerCommon;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * 数据
     *
     * @param simplePaginationUtil 请求
     * @return 数据
     */
    @GetMapping("/web/internship/teacher_distribution/internship/data")
    public ResponseEntity<Map<String, Object>> data(SimplePaginationUtil simplePaginationUtil) {
        AjaxUtil<InternshipReleaseBean> ajaxUtil = AjaxUtil.of();
        internshipControllerCommon.InternshipReleaseData(ajaxUtil, simplePaginationUtil);
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
        List<StaffBean> beans = new ArrayList<>();
        Optional<Record> record = internshipReleaseService.findByIdRelation(id);
        if (record.isPresent()) {
            Department department = record.get().into(Department.class);
            Result<Record> staffRecord = staffService.findNormalByDepartmentIdRelation(department.getDepartmentId());
            if (staffRecord.isNotEmpty()) {
                beans = staffRecord.into(StaffBean.class);
            }
        }
        beans.forEach(bean -> select2Data.add(bean.getStaffId().toString(), bean.getRealName() + " " + bean.getStaffNumber()));
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
}
