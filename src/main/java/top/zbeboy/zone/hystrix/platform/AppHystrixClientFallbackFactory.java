package top.zbeboy.zone.hystrix.platform;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.platform.AppService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.platform.app.AppAddVo;
import top.zbeboy.zone.web.vo.platform.app.AppEditVo;

import java.util.ArrayList;
import java.util.Map;

@Component
public class AppHystrixClientFallbackFactory implements AppService {
    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(AppAddVo appAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(AppEditVo appEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String username, String clientId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> remark(String username, String clientId, String remark) {
        return AjaxUtil.of();
    }
}
