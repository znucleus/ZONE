package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Schoolroom;
import top.zbeboy.zone.hystrix.data.SchoolroomHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.schoolroom.SchoolroomBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomAddVo;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomEditVo;
import top.zbeboy.zone.web.vo.data.schoolroom.SchoolroomSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = SchoolroomHystrixClientFallbackFactory.class)
public interface SchoolroomService {

    /**
     * 获取教室
     *
     * @param id 教室主键
     * @return 教室数据
     */
    @GetMapping("/base/data/schoolroom_relation/{id}")
    SchoolroomBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取楼下全部有效教室
     *
     * @param schoolroomSearchVo 查询参数
     * @return 教室数据
     */
    @PostMapping("/base/data/schoolrooms/search")
    List<Schoolroom> findByBuildingIdAndSchoolroomIsDel(@RequestBody SchoolroomSearchVo schoolroomSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/schoolrooms/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验教室是否重复
     *
     * @param buildingCode 教室名
     * @param buildingId   楼id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/schoolroom/check/add/code")
    AjaxUtil<Map<String, Object>> checkAddCode(@RequestParam("buildingCode") String buildingCode, @RequestParam("buildingId") int buildingId);

    /**
     * 保存教室信息
     *
     * @param schoolroomAddVo 教室
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/schoolroom/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody SchoolroomAddVo schoolroomAddVo);

    /**
     * 检验编辑时教室重复
     *
     * @param schoolroomId 教室id
     * @param buildingCode 教室
     * @param buildingId   楼id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/schoolroom/check/edit/code")
    AjaxUtil<Map<String, Object>> checkEditCode(@RequestParam("schoolroomId") int schoolroomId, @RequestParam("buildingCode") String buildingCode, @RequestParam("buildingId") int buildingId);

    /**
     * 保存教室更改
     *
     * @param schoolroomEditVo 教室
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/schoolroom/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody SchoolroomEditVo schoolroomEditVo);

    /**
     * 批量更改教室状态
     *
     * @param schoolroomIds 教室ids
     * @param isDel         is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/schoolrooms/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "schoolroomIds", required = false) String schoolroomIds, @RequestParam("isDel") Byte isDel);
}
