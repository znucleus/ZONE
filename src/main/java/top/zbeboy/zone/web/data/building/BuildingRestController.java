package top.zbeboy.zone.web.data.building;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;
import top.zbeboy.zone.service.data.BuildingService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.building.BuildingVo;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class BuildingRestController {

    @Resource
    private BuildingService buildingService;

    /**
     * 根据院获取全部有效楼
     *
     * @param buildingVo 院id
     * @return 楼数据
     */
    @GetMapping("/user/data/building")
    public ResponseEntity<Map<String, Object>> anyoneData(BuildingVo buildingVo) {
        Select2Data select2Data = Select2Data.of();
        Result<BuildingRecord> buildings = buildingService.findByCollegeIdAndBuildingIsDel(buildingVo.getCollegeId(),BooleanUtil.toByte(false));
        buildings.forEach(building -> select2Data.add(building.getBuildingId().toString(), building.getBuildingName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
