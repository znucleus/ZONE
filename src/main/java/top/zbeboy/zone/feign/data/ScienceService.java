package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.domain.tables.pojos.Science;
import top.zbeboy.zone.hystrix.data.ScienceHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.data.science.ScienceBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.science.ScienceAddVo;
import top.zbeboy.zone.web.vo.data.science.ScienceEditVo;
import top.zbeboy.zone.web.vo.data.science.ScienceSearchVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = ScienceHystrixClientFallbackFactory.class)
public interface ScienceService {

    /**
     * 获取专业
     *
     * @param id 专业主键
     * @return 专业数据
     */
    @GetMapping("/base/data/science/{id}")
    Science findById(@PathVariable("id") int id);

    /**
     * 获取专业
     *
     * @param id 专业主键
     * @return 专业数据
     */
    @GetMapping("/base/data/science_relation/{id}")
    ScienceBean findByIdRelation(@PathVariable("id") int id);

    /**
     * 获取系下全部有效专业
     *
     * @param scienceSearchVo 查询参数
     * @return 专业数据
     */
    @PostMapping("/base/data/sciences/search")
    List<Science> findByDepartmentIdAndScienceIsDel(@RequestBody ScienceSearchVo scienceSearchVo);

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/sciences/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验专业名是否重复
     *
     * @param scienceName  专业名
     * @param departmentId 系id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/science/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("scienceName") String scienceName, @RequestParam("departmentId") int departmentId);

    /**
     * 保存时检验专业代码是否重复
     *
     * @param scienceCode 专业代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/science/check/add/code")
    AjaxUtil<Map<String, Object>> checkAddCode(@RequestParam("scienceCode") String scienceCode);

    /**
     * 保存专业信息
     *
     * @param scienceAddVo 专业
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/science/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody ScienceAddVo scienceAddVo);

    /**
     * 检验编辑时专业名重复
     *
     * @param scienceId    专业id
     * @param scienceName  专业名
     * @param departmentId 系id
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/science/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("scienceId") int scienceId, @RequestParam("scienceName") String scienceName, @RequestParam("departmentId") int departmentId);

    /**
     * 检验编辑时专业代码重复
     *
     * @param scienceId   专业id
     * @param scienceCode 专业代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/science/check/edit/code")
    AjaxUtil<Map<String, Object>> checkEditCode(@RequestParam("scienceId") int scienceId, @RequestParam("scienceCode") String scienceCode);

    /**
     * 保存专业更改
     *
     * @param scienceEditVo 专业
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/science/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody ScienceEditVo scienceEditVo);

    /**
     * 批量更改专业状态
     *
     * @param scienceIds 专业ids
     * @param isDel      is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/sciences/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "scienceIds", required = false) String scienceIds, @RequestParam("isDel") Byte isDel);
}
