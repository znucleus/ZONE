package top.zbeboy.zone.web.data.schoolroom;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.SchoolroomRecord;
import top.zbeboy.zone.service.data.SchoolroomService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomSearchVo;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class SchoolroomRestController {

    @Resource
    private SchoolroomService schoolroomService;

    /**
     * 获取楼下全部有效教室
     *
     * @param schoolroomSearchVo 查询参数
     * @return 教室数据
     */
    @GetMapping("/user/data/schoolroom")
    public ResponseEntity<Map<String, Object>> anyoneData(SchoolroomSearchVo schoolroomSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<SchoolroomRecord> schoolrooms = schoolroomService.findByBuildingIdAndSchoolroomIsDel(schoolroomSearchVo.getBuildingId(), BooleanUtil.toByte(false));
        schoolrooms.forEach(schoolroom -> select2Data.add(schoolroom.getSchoolroomId().toString(), schoolroom.getBuildingCode()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
