package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbeboy.zone.hystrix.attend.AttendUsersHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.attend.AttendUsersBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.users.AttendUsersAddVo;

import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendUsersHystrixClientFallbackFactory.class)
public interface AttendUsersService {
    /**
     * 获取签到名单数据
     *
     * @param attendReleaseSubId 签到子表ID
     * @param type               数据类型 1:已签到 2:未签到 0:全部
     * @return 数据
     */
    @PostMapping("/api/attend/users/data")
    AjaxUtil<AttendUsersBean> data(@RequestParam("attendReleaseSubId") int attendReleaseSubId, @RequestParam(value = "type", required = false) int type);

    /**
     * 保存
     *
     * @param attendUsersAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/attend/users/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody AttendUsersAddVo attendUsersAddVo);

    /**
     * 删除人员
     *
     * @param attendUsersId 名单ID
     * @return true or false
     */
    @PostMapping("/api/attend/users/delete")
    AjaxUtil<Map<String, Object>> delete(@RequestParam("attendUsersId") String attendUsersId, @RequestParam("username") String username);

    /**
     * 名单重置
     *
     * @param attendReleaseId 发布表ID
     * @return true or false
     */
    @PostMapping("/api/attend/users/reset")
    AjaxUtil<Map<String, Object>> reset(@RequestParam("attendReleaseId") String attendReleaseId, @RequestParam("username") String username);

    /**
     * 备注
     *
     * @param attendUsersId 名单ID
     * @param remark        备注
     * @return true or false
     */
    @PostMapping("/api/attend/users/remark")
    AjaxUtil<Map<String, Object>> remark(@RequestParam("attendUsersId") String attendUsersId, @RequestParam(value = "remark", required = false) String remark, @RequestParam("username") String username);
}
