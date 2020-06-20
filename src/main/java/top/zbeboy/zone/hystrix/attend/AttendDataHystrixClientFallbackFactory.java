package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendDataService;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.data.AttendDataAddVo;

import java.util.Map;

@Component
public class AttendDataHystrixClientFallbackFactory implements AttendDataService {
    @Override
    public AjaxUtil<Map<String, Object>> save(AttendDataAddVo attendDataAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(int attendReleaseSubId, String attendUsersId, String username) {
        return AjaxUtil.of();
    }
}
