package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendReleaseService;
import top.zbeboy.zone.web.bean.attend.AttendReleaseBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;
import top.zbeboy.zone.web.vo.attend.release.AttendReleaseAddVo;
import top.zbeboy.zone.web.vo.attend.release.AttendReleaseEditVo;

import java.util.Map;

@Component
public class AttendReleaseHystrixClientFallbackFactory implements AttendReleaseService {
    @Override
    public void auto() {

    }

    @Override
    public AjaxUtil<Map<String, Object>> save(AttendReleaseAddVo attendReleaseAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<AttendReleaseBean> data(SimplePaginationUtil simplePaginationUtil) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(AttendReleaseEditVo attendReleaseEditVo) {
        return AjaxUtil.of();
    }
}
