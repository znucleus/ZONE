package top.zbeboy.zone.api.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.bean.data.student.StudentBean;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Student;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.StudentService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class StudentApiController {

    @Resource
    private StudentService studentService;

    /**
     * API:获取学生信息
     *
     * @param principal 用户
     * @return 数据
     */
    @ApiLoggingRecord(remark = "学生数据", channel = Workbook.channel.API, needLogin = true)
    @GetMapping("/api/student")
    public ResponseEntity<Map<String, Object>> users(Principal principal, HttpServletRequest request) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = SessionUtil.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            StudentBean studentBean = studentService.findByUsernameRelation(users.getUsername());
            if (Objects.nonNull(studentBean.getStudentId()) && studentBean.getStudentId() > 0) {
                Map<String, Object> outPut = new HashMap<>();
                outPut.put("studentId", studentBean.getStudentId());
                outPut.put("studentNumber", studentBean.getStudentNumber());
                outPut.put("organizeId", studentBean.getOrganizeId());
                outPut.put("schoolId", studentBean.getSchoolId());
                ajaxUtil.success().msg("获取用户信息成功").map(outPut);
            } else {
                ajaxUtil.fail().msg("未查询到学生信息");
            }
        } else {
            ajaxUtil.fail().msg("获取用户信息失败");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验学号是否正常
     *
     * @param studentNumber 学号
     * @return 数据
     */
    @PostMapping("/api/student/check/number")
    public ResponseEntity<Map<String, Object>> update(@RequestParam("studentNumber") String studentNumber) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        Student student = studentService.findByStudentNumber(studentNumber);
        if (Objects.nonNull(student.getStudentId()) && student.getStudentId() > 0) {
            ajaxUtil.success().msg("查询正常");
        } else {
            ajaxUtil.fail().msg("根据学号未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
