package top.zbeboy.zone.web.data.academicTitle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.service.data.AcademicTitleService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class AcademicTitleRestController {

    @Resource
    private AcademicTitleService academicTitleService;

    /**
     * 获取全部职称
     *
     * @return 职称数据
     */
    @GetMapping("/anyone/data/academic_title")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<AcademicTitle> academicTitles = academicTitleService.findAll();
        academicTitles.forEach(academicTitle -> {
            select2Data.add(academicTitle.getAcademicTitleId().toString(), academicTitle.getAcademicTitleName());
        });
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
