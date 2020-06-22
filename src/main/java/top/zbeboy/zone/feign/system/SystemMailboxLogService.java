package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zbase.domain.tables.pojos.SystemMailboxLog;
import top.zbeboy.zone.hystrix.system.SystemMailboxLogHystrixClientFallbackFactory;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;

@FeignClient(value = "base-server", fallback = SystemMailboxLogHystrixClientFallbackFactory.class)
public interface SystemMailboxLogService {

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/mailbox/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存日志
     *
     * @param systemMailboxLog 数据
     */
    @PostMapping("/base/system/mailbox/log/save")
    void save(@RequestBody SystemMailboxLog systemMailboxLog);
}
