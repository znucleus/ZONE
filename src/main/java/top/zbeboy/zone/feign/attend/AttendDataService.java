package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.hystrix.attend.AttendDataHystrixClientFallbackFactory;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.data.AttendDataAddVo;

import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendDataHystrixClientFallbackFactory.class)
public interface AttendDataService {

    /**
     * 保存
     *
     * @param attendDataAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/attend/data/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody AttendDataAddVo attendDataAddVo);

    /**
     * 删除签到记录
     *
     * @param attendReleaseSubId 子表ID
     * @param attendUsersId      名单ID
     * @param username           当前用户
     * @return true or false
     */
    @PostMapping("/api/attend/data/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("attendReleaseSubId") int attendReleaseSubId, @RequestParam("attendUsersId") String attendUsersId, @RequestParam("username") String username);
}
