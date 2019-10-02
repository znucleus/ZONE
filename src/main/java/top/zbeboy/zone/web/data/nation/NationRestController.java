package top.zbeboy.zone.web.data.nation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Nation;
import top.zbeboy.zone.service.data.NationService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class NationRestController {

    @Resource
    private NationService nationService;

    /**
     * 获取全部民族
     *
     * @return 民族数据
     */
    @GetMapping("/anyone/data/nation")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<Nation> nations = nationService.findAll();
        nations.forEach(nation -> {
            select2Data.add(nation.getNationId().toString(), nation.getNationName());
        });
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
