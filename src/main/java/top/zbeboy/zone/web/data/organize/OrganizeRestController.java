package top.zbeboy.zone.web.data.organize;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.OrganizeRecord;
import top.zbeboy.zone.service.data.OrganizeService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.organize.OrganizeVo;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class OrganizeRestController {

    @Resource
    private OrganizeService organizeService;

    /**
     * 获取年级下全部有效班级
     *
     * @param organizeVo 查询参数
     * @return 班级数据
     */
    @GetMapping("/anyone/data/organize")
    public ResponseEntity<Map<String, Object>> anyoneData(OrganizeVo organizeVo) {
        Select2Data select2Data = Select2Data.of();
        Result<OrganizeRecord> organizes = organizeService.findByGradeIdAndOrganizeIsDel(organizeVo.getGradeId(), BooleanUtil.toByte(false));
        organizes.forEach(organize -> select2Data.add(organize.getOrganizeId().toString(), organize.getOrganizeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
