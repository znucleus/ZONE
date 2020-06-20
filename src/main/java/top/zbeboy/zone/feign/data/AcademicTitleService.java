package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.hystrix.data.AcademicTitleHystrixClientFallbackFactory;
import top.zbeboy.zone.hystrix.data.BuildingHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.academic.AcademicAddVo;
import top.zbeboy.zone.web.vo.data.academic.AcademicEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = AcademicTitleHystrixClientFallbackFactory.class)
public interface AcademicTitleService {

    /**
     * 获取全部职称
     *
     * @return 职称数据
     */
    @GetMapping("/base/data/academics")
    List<AcademicTitle> findAll();

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/academics/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验职称是否重复
     *
     * @param academicTitleName 职称
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/academic/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("academicTitleName") String academicTitleName);

    /**
     * 保存职称信息
     *
     * @param academicAddVo 职称
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/academic/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody AcademicAddVo academicAddVo);

    /**
     * 检验编辑时职称名重复
     *
     * @param academicTitleId   职称id
     * @param academicTitleName 职称名
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/academic/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("academicTitleId") int academicTitleId, @RequestParam("academicTitleName") String academicTitleName);

    /**
     * 保存更改
     *
     * @param academicEditVo 职称
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/academic/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody AcademicEditVo academicEditVo);
}
