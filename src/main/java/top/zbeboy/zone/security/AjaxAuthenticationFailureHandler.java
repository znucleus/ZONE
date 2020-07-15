package top.zbeboy.zone.security;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SystemLoginLog;
import top.zbeboy.zbase.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SpringBootUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * Returns a 401 error code (Unauthorized) to the client, when Ajax authentication fails.
 */
@Component
public class AjaxAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
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
            ServletContext context = request.getSession().getServletContext();
            ApplicationContext ctx = WebApplicationContextUtils
                    .getWebApplicationContext(context);
            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), "登录系统失败", DateTimeUtil.getNowSqlTimestamp(), username, RequestUtil.getIpAddress(request));
            SystemLogService systemLogService = SpringBootUtil.getBean(SystemLogService.class);
            systemLogService.save(systemLog);
            int code = AjaxAuthenticationCode.AU_ERROR_CODE;
            String key = username + "_login_error_count";
            HttpSession session = request.getSession();
            if (Objects.nonNull(session.getAttribute(key))) {
                int loginErrorCount = (int) session.getAttribute(key);
                if (loginErrorCount > 7) {
                    code = AjaxAuthenticationCode.USERNAME_ACCOUNT_NON_LOCKED;
                    UsersService usersService = SpringBootUtil.getBean(UsersService.class);
                    Users users = usersService.findByUsername(username);
                    users.setAccountNonLocked(BooleanUtil.toByte(false));
                    usersService.update(users);

                    systemLog = new SystemLoginLog(UUIDUtil.getUUID(), "账号锁定", DateTimeUtil.getNowSqlTimestamp(), username, RequestUtil.getIpAddress(request));
                    systemLogService.save(systemLog);

                    session.removeAttribute(key);
                } else {
                    loginErrorCount++;
                    session.setAttribute(key, loginErrorCount);
                }
            } else {
                request.getSession().setAttribute(key, 1);
            }
            response.getWriter().print(code);
        } else {
            String username = request.getParameter("username");
            ServletContext context = request.getSession().getServletContext();
            ApplicationContext ctx = WebApplicationContextUtils
                    .getWebApplicationContext(context);
            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), "授权登录失败", DateTimeUtil.getNowSqlTimestamp(), username, RequestUtil.getIpAddress(request));
            SystemLogService systemLogService = SpringBootUtil.getBean(SystemLogService.class);
            systemLogService.save(systemLog);
            // 会帮我们跳转到上一次请求的页面上
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
