package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Building;
import top.zbeboy.zone.hystrix.data.BuildingHystrixClientFallbackFactory;
import top.zbeboy.zone.hystrix.data.CourseHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.building.BuildingBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.building.BuildingAddVo;
import top.zbeboy.zone.web.vo.data.building.BuildingEditVo;
import top.zbeboy.zone.web.vo.data.building.BuildingSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = BuildingHystrixClientFallbackFactory.class)
public interface BuildingService {

    /**
     * 获取楼
     *
     * @param id 楼主键
     * @return 楼数据
     */
    @GetMapping("/base/data/building/relation/{id}")
    BuildingBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 根据院获取全部有效楼
     *
     * @param buildingSearchVo 院id
     * @return 楼数据
     */
    @PostMapping("/base/data/building/all")
    List<Building> usersData(@RequestBody BuildingSearchVo buildingSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/building/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验楼名是否重复
     *
     * @param buildingName 楼名
     * @param collegeId    院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/building/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("buildingName") String buildingName, @RequestParam("collegeId") int collegeId);

    /**
     * 保存楼信息
     *
     * @param buildingAddVo 楼
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/building/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody BuildingAddVo buildingAddVo);

    /**
     * 检验编辑时楼名重复
     *
     * @param buildingId   楼id
     * @param buildingName 楼名
     * @param collegeId    院id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/building/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("buildingId") int buildingId, @RequestParam("buildingName") String buildingName, @RequestParam("collegeId") int collegeId);

    /**
     * 保存楼更改
     *
     * @param buildingEditVo 楼
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/building/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody BuildingEditVo buildingEditVo);

    /**
     * 批量更改楼状态
     *
     * @param buildingIds 楼ids
     * @param isDel       is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/building/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "buildingIds", required = false) String buildingIds, @RequestParam("isDel") Byte isDel);
}
