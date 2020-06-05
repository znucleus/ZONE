package top.zbeboy.zone.listener.oauth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zone.feign.system.SystemLogService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Objects;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Resource
    private SystemLogService systemLogService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        //这里的事件源除了登录事件（UsernamePasswordAuthenticationToken）还有可能是token验证事件源（OAuth2Authentication）
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            if (token.getDetails() instanceof LinkedHashMap) {
                LinkedHashMap map = (LinkedHashMap) token.getDetails();
                String grantType = (String) map.get("grant_type");
                String scope = (String) map.get("scope");
                String username = (String) map.get("username");

                if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(scope)) {
                    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                    HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getRequest();

                    SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(), "OAUTH登录[" + grantType + "][" + scope + "]", DateTimeUtil.getNowSqlTimestamp(), username, RequestUtil.getIpAddress(request));
                    systemLogService.save(systemLog);
                }
            }
        }

    }
}
