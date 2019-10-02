package top.zbeboy.zone.web.data.school;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.School;
import top.zbeboy.zone.service.data.SchoolService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class SchoolRestController {

    @Resource
    private SchoolService schoolService;

    /**
     * 获取全部有效学校
     *
     * @return 学校数据
     */
    @GetMapping("/anyone/data/school")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<School> schools = schoolService.findBySchoolIsDel(BooleanUtil.toByte(false));
        schools.forEach(school -> select2Data.add(school.getSchoolId().toString(), school.getSchoolName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
