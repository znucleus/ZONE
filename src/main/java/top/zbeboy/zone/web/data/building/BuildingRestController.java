package top.zbeboy.zone.web.data.building;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.Building;
import top.zbeboy.zbase.feign.data.BuildingService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.building.BuildingAddVo;
import top.zbeboy.zbase.vo.data.building.BuildingEditVo;
import top.zbeboy.zbase.vo.data.building.BuildingSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Building> buildings = buildingService.findByCollegeIdAndBuildingIsDel(buildingSearchVo);
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
        return new ResponseEntity<>(buildingService.data(dataTablesUtil), HttpStatus.OK);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = buildingService.checkAddName(buildingName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存楼信息
     *
     * @param buildingAddVo 楼
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/building/save")
    public ResponseEntity<Map<String, Object>> save(BuildingAddVo buildingAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = buildingService.save(buildingAddVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = buildingService.checkEditName(buildingId, buildingName, collegeId);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存楼更改
     *
     * @param buildingEditVo 楼
     * @return true 更改成功 false 更改失败
     */

    @PostMapping("/web/data/building/update")
    public ResponseEntity<Map<String, Object>> update(BuildingEditVo buildingEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = buildingService.update(buildingEditVo);
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
        AjaxUtil<Map<String, Object>> ajaxUtil = buildingService.status(buildingIds, isDel);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
