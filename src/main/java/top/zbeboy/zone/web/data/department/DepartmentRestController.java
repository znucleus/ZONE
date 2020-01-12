package top.zbeboy.zone.web.data.department;

import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;
import top.zbeboy.zone.service.data.DepartmentService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.vo.data.department.DepartmentSearchVo;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class DepartmentRestController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 获取院下全部有效系
     *
     * @param departmentSearchVo 查询参数
     * @return 系数据
     */
    @GetMapping("/anyone/data/department")
    public ResponseEntity<Map<String, Object>> anyoneData(DepartmentSearchVo departmentSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<DepartmentRecord> departments = departmentService.findByCollegeIdAndDepartmentIsDel(departmentSearchVo.getCollegeId(), BooleanUtil.toByte(false));
        departments.forEach(department -> select2Data.add(department.getDepartmentId().toString(), department.getDepartmentName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }
}
