package top.zbeboy.zone.web.data.science;

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
import top.zbeboy.zone.domain.tables.pojos.Science;
import top.zbeboy.zone.domain.tables.records.ScienceRecord;
import top.zbeboy.zone.service.data.ScienceService;
import top.zbeboy.zone.web.bean.data.science.ScienceBean;
import top.zbeboy.zone.web.plugin.select2.Select2Data;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.ByteUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.data.science.ScienceAddVo;
import top.zbeboy.zone.web.vo.data.science.ScienceSearchVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ScienceRestController {

    @Resource
    private ScienceService scienceService;

    /**
     * 获取系下全部有效专业
     *
     * @param scienceSearchVo 查询参数
     * @return 专业数据
     */
    @GetMapping("/anyone/data/science")
    public ResponseEntity<Map<String, Object>> anyoneData(ScienceSearchVo scienceSearchVo) {
        Select2Data select2Data = Select2Data.of();
        Result<ScienceRecord> sciences = scienceService.findByDepartmentIdAndScienceIsDel(scienceSearchVo.getDepartmentId(), BooleanUtil.toByte(false));
        sciences.forEach(science -> select2Data.add(science.getScienceId().toString(), science.getScienceName()));
        return new ResponseEntity<>(select2Data.send(false), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/data/science/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("select");
        headers.add("scienceId");
        headers.add("schoolName");
        headers.add("collegeName");
        headers.add("departmentName");
        headers.add("scienceName");
        headers.add("scienceCode");
        headers.add("scienceIsDel");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = scienceService.findAllByPage(dataTablesUtil);
        List<ScienceBean> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(ScienceBean.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(scienceService.countAll(dataTablesUtil));
        dataTablesUtil.setiTotalDisplayRecords(scienceService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存时检验专业名是否重复
     *
     * @param scienceName  专业名
     * @param departmentId 系id
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/science/check/add/name")
    public ResponseEntity<Map<String, Object>> checkAddName(@RequestParam("scienceName") String scienceName,
                                                            @RequestParam("departmentId") int departmentId) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(scienceName);
        Result<ScienceRecord> scienceRecords = scienceService.findByScienceNameAndDepartmentId(param, departmentId);
        if (scienceRecords.isEmpty()) {
            ajaxUtil.success().msg("专业名不重复");
        } else {
            ajaxUtil.fail().msg("专业名重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存时检验专业代码是否重复
     *
     * @param scienceCode 专业代码
     * @return true 合格 false 不合格
     */
    @PostMapping("/web/data/science/check/add/code")
    public ResponseEntity<Map<String, Object>> checkAddCode(@RequestParam("scienceCode") String scienceCode) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        String param = StringUtils.deleteWhitespace(scienceCode);
        List<Science> sciences = scienceService.findByScienceCode(param);
        if (Objects.isNull(sciences) || sciences.isEmpty()) {
            ajaxUtil.success().msg("专业代码不重复");
        } else {
            ajaxUtil.fail().msg("专业代码重复");
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 保存专业信息
     *
     * @param scienceAddVo  专业
     * @param bindingResult 检验
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/web/data/science/save")
    public ResponseEntity<Map<String, Object>> save(@Valid ScienceAddVo scienceAddVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            Science science = new Science();
            science.setScienceIsDel(ByteUtil.toByte(1).equals(scienceAddVo.getScienceIsDel()) ? ByteUtil.toByte(1) : ByteUtil.toByte(0));
            science.setScienceName(scienceAddVo.getScienceName());
            science.setScienceCode(scienceAddVo.getScienceCode());
            science.setDepartmentId(scienceAddVo.getDepartmentId());
            scienceService.save(science);
            ajaxUtil.success().msg("保存成功");
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
