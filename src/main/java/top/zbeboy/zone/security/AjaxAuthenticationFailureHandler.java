package top.zbeboy.zone.security;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.SystemOperatorLogService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.BooleanUtil;

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
        if(Objects.nonNull(savedRequest)){
            String requestURI = savedRequest.getRedirectUrl();
            if(requestURI.contains(Workbook.OAUTH_AUTHORIZE)){
                needSkip = true;
            }
        }

        if(!needSkip){
            String username = request.getParameter("username");
            ServletContext context = request.getSession().getServletContext();
            ApplicationContext ctx = WebApplicationContextUtils
                    .getWebApplicationContext(context);
            SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(), "登录系统失败", DateTimeUtil.getNowSqlTimestamp(), username, RequestUtil.getIpAddress(request));
            SystemOperatorLogService systemOperatorLogService = (SystemOperatorLogService) Objects.requireNonNull(ctx)
                    .getBean("systemOperatorLogService");
            systemOperatorLogService.save(systemLog);
            int code = AjaxAuthenticationCode.AU_ERROR_CODE;
            String key = username + "_login_error_count";
            HttpSession session = request.getSession();
            if (Objects.nonNull(session.getAttribute(key))) {
                int loginErrorCount = (int) session.getAttribute(key);
                if (loginErrorCount > 7) {
                    code = AjaxAuthenticationCode.USERNAME_ACCOUNT_NON_LOCKED;
                    UsersService usersService = (UsersService) Objects.requireNonNull(ctx)
                            .getBean("usersService");
                    Users users = usersService.findByUsername(username);
                    users.setAccountNonLocked(BooleanUtil.toByte(false));
                    usersService.update(users);

                    systemLog = new SystemOperatorLog(UUIDUtil.getUUID(), "账号锁定", DateTimeUtil.getNowSqlTimestamp(), username, RequestUtil.getIpAddress(request));
                    systemOperatorLogService.save(systemLog);

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
            // 会帮我们跳转到上一次请求的页面上
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
