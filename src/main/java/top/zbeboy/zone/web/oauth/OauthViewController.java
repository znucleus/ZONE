package top.zbeboy.zone.web.oauth;

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
import top.zbeboy.zone.domain.tables.records.OauthClientUsersRecord;
import top.zbeboy.zone.service.platform.OauthClientUsersService;
import top.zbeboy.zone.web.system.tip.SystemTipConfig;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
@SessionAttributes("authorizationRequest")
public class OauthViewController {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Resource
    private OauthClientUsersService oauthClientUsersService;

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

            Optional<OauthClientUsersRecord> oauthClientUsersRecord = oauthClientUsersService.findById(clientAuth.getClientId());
            oauthClientUsersRecord.ifPresent(oauthClientUsersRecord1 -> model.put("appName", oauthClientUsersRecord1.getAppName()));
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
