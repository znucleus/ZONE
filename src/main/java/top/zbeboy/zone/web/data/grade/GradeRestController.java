package top.zbeboy.zone.web.data.grade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Grade;
import top.zbeboy.zbase.feign.data.GradeService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.vo.data.grade.GradeSearchVo;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GradeRestController {

    @Resource
    private GradeService gradeService;

    /**
     * 获取专业下全部有效年级
     *
     * @param gradeSearchVo 查询参数
     * @return 年级数据
     */
    @ApiLoggingRecord(remark = "年级数据", channel = Workbook.channel.WEB)
    @GetMapping("/anyone/data/grade")
    public ResponseEntity<Map<String, Object>> anyoneData(GradeSearchVo gradeSearchVo, HttpServletRequest request) {
        Select2Data select2Data = Select2Data.of();
        Optional<List<Grade>> grades = gradeService.findByScienceIdAndGradeIsDel(gradeSearchVo);
        grades.ifPresent(gradeList -> gradeList.forEach(grade -> select2Data.add(grade.getGradeId().toString(), grade.getGrade().toString())));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
