package top.zbeboy.zone.web.system.configure;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.config.SystemConfigureEditVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SystemConfigureRestController {

    @Resource
    private SystemConfigureService systemConfigureService;

    /**
     * 获取对外静态参数
     *
     * @return 对外静态参数
     */
    @GetMapping("/anyone/data/configure")
    public ResponseEntity<Map<String, Object>> anyoneConfigure() {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        SystemConfigure configure1 = systemConfigureService
                .findByDataKey(Workbook.SystemConfigure.FORBIDDEN_REGISTER.name());
        SystemConfigure configure2 = systemConfigureService
                .findByDataKey(Workbook.SystemConfigure.WEI_XIN_SMALL_REVIEW_SWITCH.name());
        ajaxUtil.success().put(configure1.getDataKey(), configure1.getDataValue())
                .put(configure2.getDataKey(), configure2.getDataValue());
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }

    /**
     * 数据
     *
     * @param request 请求
     * @return 数据
     */
    @GetMapping("/web/system/config/data")
    public ResponseEntity<DataTablesUtil> data(HttpServletRequest request) {
        // 前台数据标题 注：要和前台标题顺序一致，获取order用
        List<String> headers = new ArrayList<>();
        headers.add("dataKey");
        headers.add("dataValue");
        headers.add("operator");
        DataTablesUtil dataTablesUtil = new DataTablesUtil(request, headers);
        Result<Record> records = systemConfigureService.findAllByPage(dataTablesUtil);
        List<SystemConfigure> beans = new ArrayList<>();
        if (Objects.nonNull(records) && records.isNotEmpty()) {
            beans = records.into(SystemConfigure.class);
        }
        dataTablesUtil.setData(beans);
        dataTablesUtil.setiTotalRecords(systemConfigureService.countAll());
        dataTablesUtil.setiTotalDisplayRecords(systemConfigureService.countByCondition(dataTablesUtil));
        return new ResponseEntity<>(dataTablesUtil, HttpStatus.OK);
    }

    /**
     * 保存更改
     *
     * @param systemConfigureEditVo 数据
     * @param bindingResult         检验
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/web/system/config/update")
    public ResponseEntity<Map<String, Object>> save(@Valid SystemConfigureEditVo systemConfigureEditVo, BindingResult bindingResult) {
        AjaxUtil<Map<String, Object>> ajaxUtil = AjaxUtil.of();
        if (!bindingResult.hasErrors()) {
            SystemConfigure systemConfigure = systemConfigureService.findByDataKey(systemConfigureEditVo.getDataKey());
            if (Objects.nonNull(systemConfigure)) {
                systemConfigure.setDataValue(systemConfigureEditVo.getDataValue());
                systemConfigureService.update(systemConfigure);
                ajaxUtil.success().msg("更新成功");
            } else {
                ajaxUtil.fail().msg("根据KEY未查询到数据");
            }
        } else {
            ajaxUtil.fail().msg(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
