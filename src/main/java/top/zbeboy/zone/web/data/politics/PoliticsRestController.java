package top.zbeboy.zone.web.data.politics;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zbase.feign.data.PoliticalLandscapeFeignService;
import top.zbeboy.zbase.tools.web.plugin.select2.Select2Data;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.data.politics.PoliticsAddVo;
import top.zbeboy.zbase.vo.data.politics.PoliticsEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class PoliticsRestController {

    @Resource
    private PoliticalLandscapeFeignService politicalLandscapeFeignService;

    /**
     * 获取全部政治面貌
     *
     * @return 政治面貌数据
     */
    @GetMapping("/anyone/data/politics")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<PoliticalLandscape> politicalLandscapes = politicalLandscapeFeignService.findAll();
        politicalLandscapes.forEach(politicalLandscape -> select2Data.add(politicalLandscape.getPoliticalLandscapeId().toString(), politicalLandscape.getPoliticalLandscapeName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/politics/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("politicalLandscapeId");
        headers.add("politicalLandscapeName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        return new ResponseEntity<>(politicalLandscapeFeignService.data(dataTablesUtil), HttpStatus.OK);
    }

    /**
     * 保存时检验是否重复
     *
     * @param politicalLandscapeName 政治面貌
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/politics/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("politicalLandscapeName") String politicalLandscapeName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = politicalLandscapeFeignService.checkAddName(politicalLandscapeName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param politicsAddVo 政治面貌
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/politics/save")
    public ResponseEntity<Map<String, Object>> save(PoliticsAddVo politicsAddVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = politicalLandscapeFeignService.save(politicsAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时政治面貌名重复
     *
     * @param politicalLandscapeId   政治面貌id
     * @param politicalLandscapeName 政治面貌名
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/politics/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("politicalLandscapeId") int politicalLandscapeId,
                                                             @RequestParam("politicalLandscapeName") String politicalLandscapeName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = politicalLandscapeFeignService.checkEditName(politicalLandscapeId, politicalLandscapeName);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }


    /**
     * 保存更改
     *
     * @param politicsEditVo 政治面貌
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/politics/update")
    public ResponseEntity<Map<String, Object>> update(PoliticsEditVo politicsEditVo) {
        AjaxUtil<Map<String, Object>> ajaxUtil = politicalLandscapeFeignService.update(politicsEditVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
