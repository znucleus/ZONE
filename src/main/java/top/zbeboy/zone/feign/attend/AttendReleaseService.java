package top.zbeboy.zone.feign.attend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.zbeboy.zone.hystrix.attend.AttendReleaseHystrixClientFallbackFactory;
import top.zbeboy.zone.web.bean.attend.AttendReleaseBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zbase.vo.attend.release.AttendReleaseAddVo;
import top.zbeboy.zbase.vo.attend.release.AttendReleaseEditVo;

import java.util.Map;

@FeignClient(value = "api-server", fallback = AttendReleaseHystrixClientFallbackFactory.class)
public interface AttendReleaseService {

    /**
     * 自动发布数据
     */
    @GetMapping("/api/attend/auto")
    void auto();

    /**
     * 保存
     *
     * @param attendReleaseAddVo 数据
     * @return true or false
     */
    @PostMapping("/api/attend/save")
    AjaxUtil<Map<String, Object>> save(@RequestBody AttendReleaseAddVo attendReleaseAddVo);

    /**
     * 列表数据
     *
     * @param simplePaginationUtil 数据
     * @return true or false
     */
    @PostMapping("/api/attend/data")
    AjaxUtil<AttendReleaseBean> data(@RequestBody SimplePaginationUtil simplePaginationUtil);

    /**
     * 更新
     *
     * @param attendReleaseEditVo 数据
     * @return true or false
     */
    @PostMapping("/api/attend/update")
    AjaxUtil<Map<String, Object>> update(@RequestBody AttendReleaseEditVo attendReleaseEditVo);
}
