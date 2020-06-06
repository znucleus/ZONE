package top.zbeboy.zone.hystrix.notify;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.UserNotify;
import top.zbeboy.zone.feign.notify.UserNotifyService;
import top.zbeboy.zone.web.bean.notify.UserNotifyBean;
import top.zbeboy.zone.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.pagination.SimplePaginationUtil;

import java.util.Map;

@Component
public class UserNotifyHystrixClientFallbackFactory implements UserNotifyService {
    @Override
    public AjaxUtil<UserNotifyBean> userDataNotify(SimplePaginationUtil simplePaginationUtil) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userNotifyDetail(String username, String userNotifyId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> userNotifyReads(String username, String userNotifyIds) {
        return AjaxUtil.of();
    }

    @Override
    public void save(UserNotify userNotify) {

    }
}
