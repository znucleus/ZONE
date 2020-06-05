package top.zbeboy.zone.hystrix.system;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zone.feign.system.SystemLogService;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

import java.util.ArrayList;

@Component
public class SystemLogHystrixClientFallbackFactory implements SystemLogService {
    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public void save(SystemOperatorLog systemLog) {

    }
}
