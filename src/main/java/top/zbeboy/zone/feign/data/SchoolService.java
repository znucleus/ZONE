package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.School;
import top.zbeboy.zone.hystrix.data.SchoolHystrixClientFallbackFactory;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.school.SchoolAddVo;
import top.zbeboy.zbase.vo.data.school.SchoolEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = SchoolHystrixClientFallbackFactory.class)
public interface SchoolService {

    /**
     * 获取学校
     *
     * @param id 学校主键
     * @return 学校数据
     */
    @GetMapping("/base/data/school/{id}")
    School findById(@PathVariable("id") int id);

    /**
     * 获取全部有效学校
     *
     * @return 学校数据
     */
    @GetMapping("/base/data/schools_normal")
    List<School> findNormal();

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/schools/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验学校名是否重复
     *
     * @param schoolName 学校名
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/school/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("schoolName") String schoolName);

    /**
     * 保存学校信息
     *
     * @param schoolAddVo 学校
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/school/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody SchoolAddVo schoolAddVo);

    /**
     * 检验编辑时学校名重复
     *
     * @param id         学校id
     * @param schoolName 学校名
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/school/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("schoolId") int id, @RequestParam("schoolName") String schoolName);

    /**
     * 保存学校更改
     *
     * @param schoolEditVo 学校
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/school/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody SchoolEditVo schoolEditVo);

    /**
     * 批量更改学校状态
     *
     * @param schoolIds 学校ids
     * @param isDel     is_del
     * @return true注销成功
     */
    @PostMapping("/base/data/schools/status")
    AjaxUtil<Map<String, Object>> status(@RequestParam(value = "schoolIds", required = false) String schoolIds, @RequestParam("isDel") Byte isDel);
}
