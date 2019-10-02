package top.zbeboy.zone.web.data.politicalLandscape;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zone.service.data.PoliticalLandscapeService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class PoliticalLandscapeRestController {

    @Resource
    private PoliticalLandscapeService politicalLandscapeService;

    /**
     * 获取全部政治面貌
     *
     * @return 政治面貌数据
     */
    @GetMapping("/anyone/data/political_landscape")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<PoliticalLandscape> politicalLandscapes = politicalLandscapeService.findAll();
        politicalLandscapes.forEach(politicalLandscape -> {
            select2Data.add(politicalLandscape.getPoliticalLandscapeId().toString(), politicalLandscape.getPoliticalLandscapeName());
        });
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
