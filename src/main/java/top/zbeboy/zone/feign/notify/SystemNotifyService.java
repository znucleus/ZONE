package top.zbeboy.zone.feign.notify;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.SystemNotify;
import top.zbeboy.zone.hystrix.notify.SystemNotifyHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.notify.SystemNotifyBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.system.notify.SystemNotifyAddVo;
import top.zbeboy.zbase.vo.system.notify.SystemNotifyEditVo;

import java.util.List;
import java.util.Map;

@FeignClient(value = "base-server", fallback = SystemNotifyHystrixClientFallbackFactory.class)
public interface SystemNotifyService {

    /**
     * 获取系统通知
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/base/system/notify_relation/{id}")
    SystemNotifyBean findByIdRelation(@PathVariable("id") String id);

    /**
     * 获取有效系统通知
     *
     * @return 数据
     */
    @GetMapping("/base/system/notify/effective")
    List<SystemNotify> findByEffective();

    /**
     * 数据
     *
     * @param dataTablesUtil 请求
     * @return 数据
     */
    @PostMapping("/base/system/notify/data")
    DataTablesUtil data(@RequestBody DataTablesUtil dataTablesUtil);

    /**
     * 保存
     *
     * @param systemNotifyAddVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/system/notify/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody SystemNotifyAddVo systemNotifyAddVo);

    /**
     * 更新
     *
     * @param systemNotifyEditVo 数据
     * @return true 保存成功 false 保存失败
     */
    @PostMapping("/base/system/notify/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody SystemNotifyEditVo systemNotifyEditVo);

    /**
     * 批量删除
     *
     * @param systemNotifyIds ids
     * @return true
     */
    @PostMapping("/base/system/notify/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam(value = "systemNotifyIds", required = false) String systemNotifyIds);
}
