package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.domain.tables.pojos.Nation;
import top.zbeboy.zone.hystrix.data.NationHystrixClientFallbackFactory;
import top.zbeboy.zone.hystrix.data.OrganizeHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.nation.NationAddVo;
import top.zbeboy.zone.web.vo.data.nation.NationEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = NationHystrixClientFallbackFactory.class)
public interface NationService {

    /**
     * 获取全部民族
     *
     * @return 民族数据
     */
    @GetMapping("/base/anyone/data/nation/all")
    List<Nation> anyoneData();

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/nation/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验民族是否重复
     *
     * @param nationName 民族
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/nation/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("nationName") String nationName);

    /**
     * 保存民族信息
     *
     * @param nationAddVo 民族
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/nation/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody NationAddVo nationAddVo);

    /**
     * 检验编辑时民族名重复
     *
     * @param nationId   民族id
     * @param nationName 民族名
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/nation/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("nationId") int nationId, @RequestParam("nationName") String nationName);

    /**
     * 保存更改
     *
     * @param nationEditVo 民族
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/nation/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody NationEditVo nationEditVo);
}
