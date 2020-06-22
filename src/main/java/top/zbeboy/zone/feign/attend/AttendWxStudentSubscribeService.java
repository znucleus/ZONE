package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.hystrix.attend.AttendWxStudentSubscribeHystrixClientFallbackFactory;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.attend.weixin.AttendWxStudentSubscribeAddVo;

import java.io.IOException;
import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendWxStudentSubscribeHystrixClientFallbackFactory.class)
public interface AttendWxStudentSubscribeService {
    /**
     * 保存
     *
     * @param resCode code
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/save")
    AjaxUtil<Map<String, Object>> save(@RequestParam("resCode") String resCode, @RequestParam("appId") String appId, @RequestParam("username") String username) throws IOException;

    /**
     * 订阅
     *
     * @param attendWxStudentSubscribeAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe")
    AjaxUtil<Map<String, Object>> subscribe(@RequestBody AttendWxStudentSubscribeAddVo attendWxStudentSubscribeAddVo);

    /**
     * 订阅缓存
     *
     * @return true or false
     */
    @GetMapping("/api/attend/weixin/subscribe_cache/save")
    AjaxUtil<Map<String, Object>> subscribeCache();

    /**
     * 订阅发送
     *
     * @param cacheKey 缓存 key
     */
    @GetMapping("/api/attend/weixin/subscribe/send/{key}")
    void subscribeSend(@PathVariable("key") String cacheKey) throws IOException;

    /**
     * 取消订阅
     *
     * @param username 当前用户
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/delete")
    AjaxUtil<Map<String, Object>> subscribeDelete(@RequestParam("attendReleaseId") String attendReleaseId, @RequestParam("username") String username);

    /**
     * 订阅查询
     *
     * @param username 当前用户
     * @return true or false
     */
    @PostMapping("/api/attend/weixin/subscribe/query")
    AjaxUtil<Map<String, Object>> subscribeQuery(@RequestParam("attendReleaseId") String attendReleaseId, @RequestParam("username") String username);
}
