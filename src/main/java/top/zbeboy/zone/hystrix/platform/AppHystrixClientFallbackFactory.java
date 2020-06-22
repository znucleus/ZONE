package top.zbeboy.zone.hystrix.platform;

import org.springframework.stereotype.Component;
import top.zbeboy.zbase.domain.tables.pojos.OauthClientUsers;
import top.zbeboy.zone.feign.platform.AppService;
import top.zbeboy.zone.web.bean.platform.app.OauthClientUsersBean;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zbase.tools.web.util.pagination.DataTablesUtil;
import top.zbeboy.zbase.vo.platform.app.AppAddVo;
import top.zbeboy.zbase.vo.platform.app.AppEditVo;

import java.util.ArrayList;
import java.util.Map;

@Component
public class AppHystrixClientFallbackFactory implements AppService {
    @Override
    public OauthClientUsers findOauthClientUsersById(String id) {
        return new OauthClientUsers();
    }

    @Override
    public OauthClientUsersBean findOauthClientUsersByIdAndUsernameRelation(String id, String username) {
        return new OauthClientUsersBean();
    }

    @Override
    public DataTablesUtil data(DataTablesUtil dataTablesUtil) {
        dataTablesUtil.setData(new ArrayList<>());
        return dataTablesUtil;
    }

    @Override
    public AjaxUtil<Map<String, Object>> save(AppAddVo appAddVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> update(AppEditVo appEditVo) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> delete(String username, String clientId) {
        return AjaxUtil.of();
    }

    @Override
    public AjaxUtil<Map<String, Object>> remark(String username, String clientId, String remark) {
        return AjaxUtil.of();
    }
}
