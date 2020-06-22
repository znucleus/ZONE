package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.feign.data.WeiXinService;
import top.zbeboy.zone.web.bean.data.weixin.WeiXinBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;

import java.util.Map;

@Component
public class WeiXinHystrixClientFallbackFactory implements WeiXinService {
    @Override
    public WeiXin findByUsernameAndAppId(String username, String appId) {
        return new WeiXin();
    }

    @Override
    public WeiXinBean findByStudentIdAndAppId(int studentId, String appId) {
        return new WeiXinBean();
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(WeiXin weiXin) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(WeiXin weiXin) {
        return AjaxUtil.of();
    }
}
