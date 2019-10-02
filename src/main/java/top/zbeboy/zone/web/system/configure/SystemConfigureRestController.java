package top.zbeboy.zone.web.system.configure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.service.system.SystemConfigureService;
import top.zbeboy.zone.web.util.AjaxUtil;

import javax.annotation.Resource;
import java.util.Map;

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
        SystemConfigure systemConfigure = systemConfigureService
                .findByDataKey(Workbook.SystemConfigure.FORBIDDEN_REGISTER.name());
        return new ResponseEntity<>(ajaxUtil.success()
                .put(Workbook.SystemConfigure.FORBIDDEN_REGISTER.name(), systemConfigure.getDataValue()).send(), HttpStatus.OK);
    }
}
