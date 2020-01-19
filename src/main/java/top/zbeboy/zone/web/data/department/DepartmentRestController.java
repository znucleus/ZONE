package top.zbeboy.zone.web.data.department;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.DepartmentRecord;
import top.zbeboy.zone.service.data.DepartmentService;
import top.zbeboy.zone.web.bean.data.college.CollegeBean;
import top.zbeboy.zone.web.bean.data.department.DepartmentBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.department.DepartmentSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/department/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("departmentId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("departmentIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = departmentService.findAllByPage(dataTablesUtil);
        List<DepartmentBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(DepartmentBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(departmentService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(departmentService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
