package top.zbeboy.zone.api.student;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.domain.tables.pojos.Student;
import top.zbeboy.zone.service.data.StudentService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

public class StudentApiController {

    @Resource
    private StudentService studentService;

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
