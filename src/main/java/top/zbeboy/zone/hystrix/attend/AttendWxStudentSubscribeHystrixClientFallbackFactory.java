package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendWxStudentSubscribeService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.vo.attend.weixin.AttendWxStudentSubscribeAddVo;

import java.io.IOException;
import java.util.Map;

@Component
public class AttendWxStudentSubscribeHystrixClientFallbackFactory implements AttendWxStudentSubscribeService {
    @Override
    public AjaxUtil<Map<String, Object>> save(String resCode, String appId, String username) throws IOException {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> subscribe(AttendWxStudentSubscribeAddVo attendWxStudentSubscribeAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> subscribeCache() {
        return AjaxUtil.of();
    }

    @Override
    public void subscribeSend(String cacheKey) throws IOException {

    }

    @Override
    public AjaxUtil<Map<String, Object>> subscribeDelete(String attendReleaseId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> subscribeQuery(String attendReleaseId, String username) {
        return AjaxUtil.of();
    }
}
