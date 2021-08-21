package top.zbeboy.zone.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SystemLoginLog;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.SpringBootUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        boolean needSkip = false;
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (Objects.nonNull(savedRequest)) {
            String requestURI = savedRequest.getRedirectUrl();
            if (requestURI.contains(Workbook.OAUTH_AUTHORIZE)) {
                needSkip = true;
            }
        }

        if (!needSkip) {
            String username = request.getParameter("username");
            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), "登录系统成功", DateTimeUtil.getNowLocalDateTime(), username, RequestUtil.getIpAddress(request));
            SystemLogService systemLogService = SpringBootUtil.getBean(SystemLogService.class);
            systemLogService.save(systemLog);
            String key = username + "_login_error_count";
            HttpSession session = request.getSession();
            if (Objects.nonNull(session.getAttribute(key))) {
                session.removeAttribute(key);
            }
            response.getWriter().print(AjaxAuthenticationCode.OK_CODE);
        } else {
            String username = request.getParameter("username");
            Map<String, String[]> requestMap = savedRequest.getParameterMap();
            String logMsg = "授权登录成功";
            if (Objects.nonNull(requestMap)) {
                String clientId = Arrays.toString(requestMap.get("client_id"));
                String grantType = Arrays.toString(requestMap.get("grant_type"));
                logMsg = "授权登录成功" + grantType + clientId;
            }
            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), logMsg, DateTimeUtil.getNowLocalDateTime(), username, RequestUtil.getIpAddress(request));
            SystemLogService systemLogService = SpringBootUtil.getBean(SystemLogService.class);
            systemLogService.save(systemLog);
            // 会帮我们跳转到上一次请求的页面上
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
