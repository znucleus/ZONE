package top.zbeboy.zone.api.student;

import org.jooq.Record;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.domain.tables.records.StudentRecord;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.web.bean.data.student.StudentBean;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
public class StudentApiController {

    @Resource
    private UsersService usersService;

    @Resource
    private StudentService studentService;

    /**
     * API:获取学生信息
     *
     * @param principal 用户
     * @return 数据
     */
    @GetMapping("/api/student")
    public ResponseEntity<Map<String, Object>> users(Principal principal) {
        AjaxUtil<Object> ajaxUtil = AjaxUtil.of();
        Users users = usersService.getUserFromOauth(principal);
        if (Objects.nonNull(users)) {
            Optional<Record> record = studentService.findByUsernameRelation(users.getUsername());
            if (record.isPresent()) {
                Map<String, Object> outPut = new HashMap<>();
                StudentBean student = record.get().into(StudentBean.class);
                outPut.put("studentId", student.getStudentId());
                outPut.put("studentNumber", student.getStudentNumber());
                outPut.put("organizeId", student.getOrganizeId());
                outPut.put("schoolId", student.getSchoolId());
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
        if (Objects.nonNull(student)) {
            ajaxUtil.success().msg("查询正常");
        } else {
            ajaxUtil.fail().msg("根据学号未查询到学生信息");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
