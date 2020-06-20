package top.zbeboy.zone.hystrix.system;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.feign.system.SystemConfigureService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.config.SystemConfigureEditVo;

import java.util.ArrayList;
import java.util.Map;

@Component
public class SystemConfigureHystrixClientFallbackFactory implements SystemConfigureService {
    @Override
    public SystemConfigure findByDataKey(String key) {
        return new SystemConfigure();
    }

    @Override
    public AjaxUtil<Map<String, Object>> anyoneConfigure() {
        return AjaxUtil.of();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(SystemConfigureEditVo systemConfigureEditVo) {
        return AjaxUtil.of();
    }
}
