package top.zbeboy.zone.web.data.academic;

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
import top.zbeboy.zone.domain.tables.pojos.AcademicTitle;
import top.zbeboy.zone.domain.tables.records.AcademicTitleRecord;
import top.zbeboy.zone.service.data.AcademicTitleService;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.academic.AcademicAddVo;
import top.zbeboy.zone.web.vo.data.academic.AcademicEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AcademicRestController {

    @Resource
    private AcademicTitleService academicTitleService;

    /**
     * 获取全部职称
     *
     * @return 职称数据
     */
    @GetMapping("/anyone/data/academic")
    public ResponseEntity<Map<String, Object>> anyoneData() {
        Select2Data select2Data = Select2Data.of();
        List<AcademicTitle> academicTitles = academicTitleService.findAll();
        academicTitles.forEach(academicTitle -> select2Data.add(academicTitle.getAcademicTitleId().toString(), academicTitle.getAcademicTitleName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/academic/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("academicTitleId");
        headers.add("academicTitleName");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = academicTitleService.findAllByPage(dataTablesUtil);
        List<AcademicTitle> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(AcademicTitle.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(academicTitleService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(academicTitleService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验职称是否重复
     *
     * @param academicTitleName 职称
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/academic/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("academicTitleName") String academicTitleName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(academicTitleName);
        List<AcademicTitle> academicTitles = academicTitleService.findByAcademicTitleName(param);
        if (Objects.isNull(academicTitles) || academicTitles.isEmpty()) {
            ajaxUtil.success().msg("职称不重复");
        } else {
            ajaxUtil.fail().msg("职称重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存职称信息
     *
     * @param academicAddVo 职称
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/academic/save")
    public ResponseEntity<Map<String, Object>> save(@Valid AcademicAddVo academicAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            AcademicTitle academic = new AcademicTitle();
            academic.setAcademicTitleName(academicAddVo.getAcademicTitleName());
            academicTitleService.save(academic);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 检验编辑时职称名重复
     *
     * @param academicTitleId   职称id
     * @param academicTitleName 职称名
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/academic/check/edit/name")
    public ResponseEntity<Map<String, Object>> checkEditName(@RequestParam("academicTitleId") int academicTitleId,
                                                             @RequestParam("academicTitleName") String academicTitleName) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(academicTitleName);
        Result<AcademicTitleRecord> records = academicTitleService.findByAcademicTitleNameNeAcademicTitleId(param, academicTitleId);
        if (records.isEmpty()) {
            ajaxUtil.success().msg("职称不重复");
        } else {
            ajaxUtil.fail().msg("职称重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }


    /**
     * 保存更改
     *
     * @param academicEditVo 职称
     * @param bindingResult  检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/data/academic/update")
    public ResponseEntity<Map<String, Object>> save(@Valid AcademicEditVo academicEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            AcademicTitle academic = academicTitleService.findById(academicEditVo.getAcademicTitleId());
            if (Objects.nonNull(academic)) {
                academic.setAcademicTitleName(academicEditVo.getAcademicTitleName());
                academicTitleService.update(academic);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据职称ID未查询到职称数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
