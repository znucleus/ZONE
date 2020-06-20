package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendReleaseSubService;
import top.zbeboy.zone.web.bean.attend.AttendReleaseSubBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Map;

@Component
public class AttendReleaseSubHystrixClientFallbackFactory implements AttendReleaseSubService {
    @Override
    public AjaxUtil<AttendReleaseSubBean> subData(SimplePaginationUtil simplePaginationUtil) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> subQuery(int attendReleaseSubId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> subDelete(int attendReleaseSubId, String username) {
        return AjaxUtil.of();
    }
}
