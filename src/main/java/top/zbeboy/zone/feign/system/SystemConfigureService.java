package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zone.domain.tables.pojos.SystemConfigure;
import top.zbeboy.zone.hystrix.system.SystemConfigureHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.DataTablesUtil;
import top.zbeboy.zone.web.vo.system.config.SystemConfigureEditVo;

import java.util.Map;

@FeignClient(value = "base-server", fallback = SystemConfigureHystrixClientFallbackFactory.class)
public interface SystemConfigureService {

    /**
     * 获取配置
     *
     * @param key 键
     * @return 数据
     */
    @GetMapping("/base/system/config/{key}")
    SystemConfigure findByDataKey(@PathVariable("key") String key);

    /**
     * 获取对外静态参数
     *
     * @return 对外静态参数
     */
    @GetMapping("/base/anyone/data/configure")
    AjaxUtil<Map<String, Object>> anyoneConfigure();

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/config/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存更改
     *
     * @param systemConfigureEditVo 数据
     * @return true 更改成功 false 更改失败
     */
    @PostMapping("/base/system/config/update")
    AjaxUtil<Map<String, Object>> save(@RequestBody SystemConfigureEditVo systemConfigureEditVo);
}
