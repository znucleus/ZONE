package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zone.domain.tables.pojos.SystemSmsLog;
import top.zbeboy.zone.hystrix.system.SystemSmsLogHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

@FeignClient(value = "base-server", fallback = SystemSmsLogHystrixClientFallbackFactory.class)
public interface SystemSmsLogService {

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/sms/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存日志
     *
     * @param systemSmsLog 数据
     */
    @PostMapping("/base/system/sms/log/save")
    void save(@RequestBody SystemSmsLog systemSmsLog);
}
