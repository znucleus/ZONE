package top.zbeboy.zone.hystrix.attend;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.feign.attend.AttendUsersService;
import top.zbeboy.zone.web.bean.attend.AttendUsersBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.vo.attend.users.AttendUsersAddVo;

import java.util.Map;

@Component
public class AttendUsersHystrixClientFallbackFactory implements AttendUsersService {
    @Override
    public AjaxUtil<AttendUsersBean> data(int attendReleaseSubId, int type) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(AttendUsersAddVo attendUsersAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String attendUsersId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> reset(String attendReleaseId, String username) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> remark(String attendUsersId, String remark, String username) {
        return AjaxUtil.of();
    }
}
