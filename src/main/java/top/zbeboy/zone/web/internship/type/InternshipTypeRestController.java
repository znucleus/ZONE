package top.zbeboy.zone.web.internship.type;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.InternshipType;
import top.zbeboy.zone.service.internship.InternshipTypeService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class InternshipTypeRestController {

    @Resource
    private InternshipTypeService internshipTypeService;

    /**
     * 获取全部实习类型
     *
     * @return 实习类型
     */
    @GetMapping("/users/data/internship_type")
    public ResponseEntity<Map<String, Object>> usersData() {
        Select2Data select2Data = Select2Data.of();
        List<InternshipType> internshipTypes = internshipTypeService.findAll();
        internshipTypes.forEach(internshipType -> select2Data.add(internshipType.getInternshipTypeId().toString(), internshipType.getInternshipTypeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
