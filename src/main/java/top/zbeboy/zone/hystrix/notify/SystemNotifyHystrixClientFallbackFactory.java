package top.zbeboy.zone.hystrix.notify;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.SystemNotify;
import top.zbeboy.zone.feign.notify.SystemNotifyService;
import top.zbeboy.zone.web.bean.notify.SystemNotifyBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.notify.SystemNotifyAddVo;
import top.zbeboy.zone.web.vo.system.notify.SystemNotifyEditVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SystemNotifyHystrixClientFallbackFactory implements SystemNotifyService {
    @Override
    public SystemNotifyBean findByIdRelation(String id) {
        return new SystemNotifyBean();
    }

    @Override
    public List<SystemNotify> findByEffective() {
        return new ArrayList<>();
    }

    @Override
    public List<SystemNotify> userSystemNotify() {
        return new ArrayList<>();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(SystemNotifyAddVo systemNotifyAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(SystemNotifyEditVo systemNotifyEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String systemNotifyIds) {
        return AjaxUtil.of();
    }
}
