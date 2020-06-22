package top.zbeboy.zone.web.oauth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import top.zbeboy.zbase.domain.tables.pojos.OauthClientUsers;
import top.zbeboy.zone.feign.platform.AppService;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@SessionAttributes("authorizationRequest")
public class OauthViewController {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Resource
    private AppService appService;

    @Autowired
    private ApprovalStore approvalStore;

    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(ModelMap model, Principal principal) {
        SystemTipConfig config = new SystemTipConfig();
        String page;
        AuthorizationRequest clientAuth = (AuthorizationRequest) model.remove("authorizationRequest");
        if (Objects.nonNull(clientAuth)) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            model.put("auth_request", clientAuth);
            model.put("client", client);

            OauthClientUsers oauthClientUsers = appService.findOauthClientUsersById(clientAuth.getClientId());
            if(Objects.nonNull(oauthClientUsers) && StringUtils.isNotBlank(oauthClientUsers.getClientId())){
                model.put("appName", oauthClientUsers.getAppName());
            }
            Map<String, String> scopes = new LinkedHashMap<>();
            for (String scope : clientAuth.getScope()) {
                scopes.put(OAuth2Utils.SCOPE_PREFIX + scope, "false");
            }
            for (Approval approval : approvalStore.getApprovals(principal.getName(), client.getClientId())) {
                if (clientAuth.getScope().contains(approval.getScope())) {
                    scopes.put(OAuth2Utils.SCOPE_PREFIX + approval.getScope(),
                            approval.getStatus() == Approval.ApprovalStatus.APPROVED ? "true" : "false");
                }
            }
            model.put("scopes", scopes);

            page = "oauth_confirm";
        } else {
            config.buildDangerTip(
                    "查询失败",
                    "无授权信息！");
            config.addLoginButton();
            config.addHomeButton();
            config.dataMerging(model);
            page = "tip";
        }
        return page;
    }

    @RequestMapping("/oauth/error")
    public String handleError(Map<String, Object> model) {
        // We can add more stuff to the model here for JSP rendering. If the client was a machine then
        // the JSON will already have been rendered.
        model.put("message", "授权错误");
        return "oauth_error";
    }
}
