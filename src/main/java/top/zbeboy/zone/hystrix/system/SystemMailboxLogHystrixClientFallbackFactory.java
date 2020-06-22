package top.zbeboy.zone.hystrix.system;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.SystemMailboxLog;
import top.zbeboy.zone.feign.system.SystemMailboxLogService;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

import java.util.ArrayList;

@Component
public class SystemMailboxLogHystrixClientFallbackFactory implements SystemMailboxLogService {
    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public void save(SystemMailboxLog systemMailboxLog) {

    }
}
