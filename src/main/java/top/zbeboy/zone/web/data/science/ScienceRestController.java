package top.zbeboy.zone.web.data.science;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;
import top.zbeboy.zone.service.data.ScienceService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.science.ScienceSearchVo;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class ScienceRestController {

    @Resource
    private ScienceService scienceService;

    /**
     * 获取系下全部有效专业
     *
     * @param scienceSearchVo 查询参数
     * @return 专业数据
     */
    @GetMapping("/anyone/data/science")
    public ResponseEntity<Map<String, Object>> anyoneData(ScienceSearchVo scienceSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<ScienceRecord> sciences = scienceService.findByDepartmentIdAndScienceIsDel(scienceSearchVo.getDepartmentId(), BooleanUtil.toByte(false));
        sciences.forEach(science -> select2Data.add(science.getScienceId().toString(), science.getScienceName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
