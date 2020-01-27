package top.zbeboy.zone.web.data.building;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.domain.tables.pojos.Building;
import top.zbeboy.zone.domain.tables.records.BuildingRecord;
import top.zbeboy.zone.service.data.BuildingService;
import top.zbeboy.zone.web.bean.data.building.BuildingBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.SmallPropsUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.building.BuildingAddVo;
import top.zbeboy.zone.web.vo.data.building.BuildingEditVo;
import top.zbeboy.zone.web.vo.data.building.BuildingSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    /**
     * 保存时检验楼名是否重复
     *
     * @param buildingName 楼名
     * @param collegeId    院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/building/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("buildingName") String buildingName,
                                                            @RequestParam(value = "collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(buildingName);
        Result<BuildingRecord> records = buildingService.findByBuildingNameAndCollegeId(param, collegeId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("楼名不重复");
        } else {
            ajaxUtil.fail().msg("楼名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存楼信息
     *
     * @param buildingAddVo 楼
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/building/save")
    public ResponseEntity<Map<String, Object>> save(@Valid BuildingAddVo buildingAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Building building = new Building();
            building.setBuildingIsDel(ByteUtil.toByte(1).equals(buildingAddVo.getBuildingIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            building.setBuildingName(buildingAddVo.getBuildingName());
            building.setCollegeId(buildingAddVo.getCollegeId());
            buildingService.save(building);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时楼名重复
     *
     * @param buildingId   楼id
     * @param buildingName 楼名
     * @param collegeId    院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/building/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("buildingId") int buildingId,
                                                             @RequestParam("buildingName") String buildingName,
                                                             @RequestParam("collegeId") int collegeId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(buildingName);
        Result<BuildingRecord> buildingRecords = buildingService.findByBuildingNameAndCollegeIdNeBuildingId(param, collegeId, buildingId);
        if (buildingRecords.isEmpty()) {
            ajaxUtil.success().msg("楼名不重复");
        } else {
            ajaxUtil.fail().msg("楼名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存楼更改
     *
     * @param buildingEditVo 楼
     * @param bindingResult  检验
     * @return true 更改成功 false 更改失败
     */

    @PostMapping("/web/data/building/update")
    public ResponseEntity<Map<String, Object>> update(@Valid BuildingEditVo buildingEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Building building = buildingService.findById(buildingEditVo.getBuildingId());
            if (Objects.nonNull(building)) {
                building.setBuildingIsDel(ByteUtil.toByte(1).equals(buildingEditVo.getBuildingIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
                building.setBuildingName(buildingEditVo.getBuildingName());
                building.setCollegeId(buildingEditVo.getCollegeId());
                buildingService.update(building);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据楼ID未查询到楼数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 批量更改楼状态
     *
     * @param buildingIds 楼ids
     * @param isDel       is_del
     * @return true注销成功
     */
    @PostMapping("/web/data/building/status")
    public ResponseEntity<Map<String, Object>> status(String buildingIds, Byte isDel) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (StringUtils.isNotBlank(buildingIds)) {
            buildingService.updateIsDel(SmallPropsUtil.StringIdsToNumberList(buildingIds), isDel);
            ajaxUtil.success().msg("更新状态成功");
        } else {
            ajaxUtil.fail().msg("请选择楼");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
