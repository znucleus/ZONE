package top.zbeboy.zone.web.data.politics;

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
import top.zbeboy.zone.domain.tables.pojos.PoliticalLandscape;
import top.zbeboy.zone.domain.tables.records.PoliticalLandscapeRecord;
import top.zbeboy.zone.service.data.PoliticalLandscapeService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.politics.PoliticsAddVo;
import top.zbeboy.zone.web.vo.data.politics.PoliticsEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class PoliticsRestController {

    @Resource
    private PoliticalLandscapeService politicalLandscapeService;

    /**
     * 获取全部政治面貌
     *
     * @return 政治面貌数据
     */
    @GetMapping("/anyone/data/politics")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<PoliticalLandscape> politicalLandscapes = politicalLandscapeService.findAll();
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
        Result<Record> records = politicalLandscapeService.findAllByPage(dataTablesUtil);
        List<PoliticalLandscape> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(PoliticalLandscape.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(politicalLandscapeService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(politicalLandscapeService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验是否重复
     *
     * @param politicalLandscapeName 政治面貌
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/politics/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("politicalLandscapeName") String politicalLandscapeName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(politicalLandscapeName);
        List<PoliticalLandscape> politicalLandscapes = politicalLandscapeService.findByPoliticalLandscapeName(param);
        if (Objects.isNull(politicalLandscapes) || politicalLandscapes.isEmpty()) {
            ajaxUtil.success().msg("政治面貌不重复");
        } else {
            ajaxUtil.fail().msg("政治面貌重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存
     *
     * @param politicsAddVo 政治面貌
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/politics/save")
    public ResponseEntity<Map<String, Object>> save(@Valid PoliticsAddVo politicsAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            PoliticalLandscape politicalLandscape = new PoliticalLandscape();
            politicalLandscape.setPoliticalLandscapeName(politicsAddVo.getPoliticalLandscapeName());
            politicalLandscapeService.save(politicalLandscape);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
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
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(politicalLandscapeName);
        Result<PoliticalLandscapeRecord> records = politicalLandscapeService.findByPoliticalLandscapeNameNePoliticalLandscapeId(param, politicalLandscapeId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("政治面貌不重复");
        } else {
            ajaxUtil.fail().msg("政治面貌重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }


    /**
     * 保存更改
     *
     * @param politicsEditVo 政治面貌
     * @param bindingResult  检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/politics/update")
    public ResponseEntity<Map<String, Object>> save(@Valid PoliticsEditVo politicsEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            PoliticalLandscape politicalLandscape = politicalLandscapeService.findById(politicsEditVo.getPoliticalLandscapeId());
            if (Objects.nonNull(politicalLandscape)) {
                politicalLandscape.setPoliticalLandscapeName(politicsEditVo.getPoliticalLandscapeName());
                politicalLandscapeService.update(politicalLandscape);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据政治面貌ID未查询到政治面貌数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
