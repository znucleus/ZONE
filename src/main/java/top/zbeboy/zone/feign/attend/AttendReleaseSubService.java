package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zone.hystrix.attend.AttendReleaseSubHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendReleaseSubHystrixClientFallbackFactory.class)
public interface AttendReleaseSubService {

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @return true or false
     */
    @PostMapping("/api/attend/sub/data")
    AjaxUtil<AttendReleaseSubBean> subData(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 通过id查询子表数据
     *
     * @param attendReleaseSubId 子表id
     * @return 数据
     */
    @GetMapping("/api/attend/sub/query/{id}")
    AjaxUtil<Map<String, Object>> subQuery(@PathVariable("id") int attendReleaseSubId);

    /**
     * 根据id删除子表数据
     *
     * @param attendReleaseSubId 子表数据
     * @return 数据
     */
    @PostMapping("/api/attend/sub/delete")
    AjaxUtil<Map<String, Object>> subDelete(@RequestParam("id") int attendReleaseSubId, @RequestParam("username") String username);
}
