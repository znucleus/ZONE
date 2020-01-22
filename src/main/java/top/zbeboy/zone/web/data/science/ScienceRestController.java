package top.zbeboy.zone.web.data.science;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;
import top.zbeboy.zone.service.data.ScienceService;
import top.zbeboy.zone.web.bean.data.department.DepartmentBean;
import top.zbeboy.zone.web.bean.data.science.ScienceBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.science.ScienceSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/science/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("scienceId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("scienceName");
        headers.add("scienceCode");
        headers.add("scienceIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = scienceService.findAllByPage(dataTablesUtil);
        List<ScienceBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(ScienceBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(scienceService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(scienceService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
