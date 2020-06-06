package top.zbeboy.zone.hystrix.data;

import org.springframework.stereotype.Component;
import top.zbeboy.zone.domain.tables.pojos.WeiXin;
import top.zbeboy.zone.feign.data.WeiXinService;
import top.zbeboy.zone.web.bean.data.weixin.WeiXinBean;

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
    public void save(WeiXin weiXin) {

    }

    @Override
    public void update(WeiXin weiXin) {

    }
}
