package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zone.hystrix.system.SystemLogHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;

@FeignClient(value = "base-server", fallback = SystemLogHystrixClientFallbackFactory.class)
public interface SystemLogService {

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/log/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存日志
     *
     * @param systemLog 数据
     */
    @PostMapping("/base/system/log/save")
    void save(@RequestBody SystemOperatorLog systemLog);
}
