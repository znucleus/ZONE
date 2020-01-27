package top.zbeboy.zone.web.data.building;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;
import top.zbeboy.zone.service.data.BuildingService;
import top.zbeboy.zone.web.bean.data.building.BuildingBean;
import top.zbeboy.zone.web.bean.data.department.DepartmentBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.building.BuildingSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class BuildingRestController {

    @Resource
    private BuildingService buildingService;

    /**
     * 根据院获取全部有效楼
     *
     * @param buildingSearchVo 院id
     * @return 楼数据
     */
    @GetMapping("/users/data/building")
    public ResponseEntity<Map<String, Object>> usersData(BuildingSearchVo buildingSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<BuildingRecord> buildings = buildingService.findByCollegeIdAndBuildingIsDel(buildingSearchVo.getCollegeId(), BooleanUtil.toByte(false));
        buildings.forEach(building -> select2Data.add(building.getBuildingId().toString(), building.getBuildingName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/building/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("buildingId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("buildingName");
        headers.add("buildingIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = buildingService.findAllByPage(dataTablesUtil);
        List<BuildingBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(BuildingBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(buildingService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(buildingService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }
}
