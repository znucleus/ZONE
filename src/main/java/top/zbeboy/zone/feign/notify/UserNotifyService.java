package top.zbeboy.zone.feign.notify;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.UserNotify;
import top.zbeboy.zone.hystrix.notify.UserNotifyHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.notify.UserNotifyBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;

import java.util.Map;

@FeignClient(value = "base-server", fallback = UserNotifyHystrixClientFallbackFactory.class)
public interface UserNotifyService {

    /**
     * 获取用户通知数据
     *
     * @param simplePaginationUtil 分页工具
     * @return 数据
     */
    @PostMapping("/base/users/data/notify")
    AjaxUtil<UserNotifyBean> userDataNotify(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 获取通知详情
     *
     * @param username     当前用户
     * @param userNotifyId 通知id
     * @return 数据
     */
    @GetMapping("/base/users/notify/detail/{username}/{userNotifyId}")
    AjaxUtil<Map<String, Object>> userNotifyDetail(@PathVariable("username") String username, @PathVariable("userNotifyId") String userNotifyId);

    /**
     * 批量更新状态为已读
     *
     * @param username      当前用户
     * @param userNotifyIds ids
     * @return 是否成功
     */
    @PostMapping("/base/users/notify/reads")
    AjaxUtil<Map<String, Object>> userNotifyReads(@RequestParam("username") String username, @RequestParam("userNotifyId") String userNotifyIds);

    /**
     * 保存
     *
     * @param userNotify 数据
     */
    @PostMapping("/base/users/notify/save")
    void save(@RequestBody UserNotify userNotify);
}
