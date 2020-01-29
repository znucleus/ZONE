package top.zbeboy.zone.web.data.nation;

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
import top.zbeboy.zone.domain.tables.pojos.Nation;
import top.zbeboy.zone.domain.tables.records.NationRecord;
import top.zbeboy.zone.service.data.NationService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.nation.NationAddVo;
import top.zbeboy.zone.web.vo.data.nation.NationEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class NationRestController {

    @Resource
    private NationService nationService;

    /**
     * 获取全部民族
     *
     * @return 民族数据
     */
    @GetMapping("/anyone/data/nation")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<Nation> nations = nationService.findAll();
        nations.forEach(nation -> select2Data.add(nation.getNationId().toString(), nation.getNationName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/nation/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("nationId");
        headers.add("nationName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = nationService.findAllByPage(dataTablesUtil);
        List<Nation> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(Nation.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(nationService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(nationService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验民族是否重复
     *
     * @param nationName 民族
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/nation/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("nationName") String nationName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(nationName);
        List<Nation> nations = nationService.findByNationName(param);
        if (Objects.isNull(nations) || nations.isEmpty()) {
            ajaxUtil.success().msg("民族不重复");
        } else {
            ajaxUtil.fail().msg("民族重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存民族信息
     *
     * @param nationAddVo   民族
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/nation/save")
    public ResponseEntity<Map<String, Object>> save(@Valid NationAddVo nationAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Nation nation = new Nation();
            nation.setNationName(nationAddVo.getNationName());
            nationService.save(nation);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时民族名重复
     *
     * @param nationId   民族id
     * @param nationName 民族名
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/nation/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("nationId") int nationId,
                                                             @RequestParam("nationName") String nationName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(nationName);
        Result<NationRecord> records = nationService.findByNationNameNeNationId(param, nationId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("民族不重复");
        } else {
            ajaxUtil.fail().msg("民族重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }


    /**
     * 保存更改
     *
     * @param nationEditVo  民族
     * @param bindingResult 检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/nation/update")
    public ResponseEntity<Map<String, Object>> save(@Valid NationEditVo nationEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Nation nation = nationService.findById(nationEditVo.getNationId());
            if (Objects.nonNull(nation)) {
                nation.setNationName(nationEditVo.getNationName());
                nationService.update(nation);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据民族ID未查询到民族数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
