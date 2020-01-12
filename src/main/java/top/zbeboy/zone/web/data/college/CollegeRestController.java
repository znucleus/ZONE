package top.zbeboy.zone.web.data.college;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.CollegeRecord;
import top.zbeboy.zone.service.data.CollegeService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.college.CollegeSearchVo;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class CollegeRestController {

    @Resource
    private CollegeService collegeService;

    /**
     * 获取学校下全部有效院
     *
     * @param collegeSearchVo 查询参数
     * @return 院数据
     */
    @GetMapping("/anyone/data/college")
    public ResponseEntity<Map<String, Object>> anyoneData(CollegeSearchVo collegeSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<CollegeRecord> colleges = collegeService.findBySchoolIdAndCollegeIsDel(collegeSearchVo.getSchoolId(), BooleanUtil.toByte(false));
        colleges.forEach(college -> select2Data.add(college.getCollegeId().toString(), college.getCollegeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
