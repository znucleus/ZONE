package top.zbeboy.zone.feign.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zbase.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zone.hystrix.data.PoliticalLandscapeHystrixClientFallbackFactory;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.politics.PoliticsAddVo;
import top.zbeboy.zbase.vo.data.politics.PoliticsEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = PoliticalLandscapeHystrixClientFallbackFactory.class)
public interface PoliticalLandscapeFeignService {

    /**
     * 获取全部政治面貌
     *
     * @return 政治面貌数据
     */
    @GetMapping("/base/data/politicses")
    List<PoliticalLandscape> findAll();

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/data/politicses/paging")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存时检验是否重复
     *
     * @param politicalLandscapeName 政治面貌
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/politics/check/add/name")
    AjaxUtil<Map<String, Object>> checkAddName(@RequestParam("politicalLandscapeName") String politicalLandscapeName);

    /**
     * 保存
     *
     * @param politicsAddVo 政治面貌
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/data/politics/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody PoliticsAddVo politicsAddVo);

    /**
     * 检验编辑时政治面貌名重复
     *
     * @param politicalLandscapeId   政治面貌id
     * @param politicalLandscapeName 政治面貌名
     * @return true 合格 false 不合格
     */
    @PostMapping("/base/data/politics/check/edit/name")
    AjaxUtil<Map<String, Object>> checkEditName(@RequestParam("politicalLandscapeId") int politicalLandscapeId, @RequestParam("politicalLandscapeName") String politicalLandscapeName);

    /**
     * 保存更改
     *
     * @param politicsEditVo 政治面貌
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/data/politics/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody PoliticsEditVo politicsEditVo);
}
