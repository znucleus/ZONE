package top.zbeboy.zone.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.SystemLoginLog;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.UsersService;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zbase.tools.web.util.BooleanUtil;
import top.zbeboy.zone.web.util.SpringBootUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

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
            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), "登录系统失败", DateTimeUtil.getNowLocalDateTime(), username, RequestUtil.getIpAddress(request));
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
                    Optional<Users> result = Optional.empty();
                    boolean hasUser = false;
                    if (Pattern.matches(Workbook.MAIL_REGEX, username)) {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("email", username);
                        result = usersService.findByCondition(paramMap);
                        hasUser = result.isPresent();
                    }

                    if (!hasUser && Pattern.matches(Workbook.MOBILE_REGEX, username)) {
                        HashMap<String, String> paramMap = new HashMap<>();
                        paramMap.put("mobile", username);
                        result = usersService.findByCondition(paramMap);
                        hasUser = result.isPresent();
                    }

                    if (!hasUser) {
                        result = usersService.findByUsername(username);
                        hasUser = result.isPresent();
                    }

                    if (hasUser) {
                        Users users = result.get();
                        users.setAccountNonLocked(BooleanUtil.toByte(false));
                        usersService.update(users);

                        systemLog = new SystemLoginLog(UUIDUtil.getUUID(), "账号锁定", DateTimeUtil.getNowLocalDateTime(), username, RequestUtil.getIpAddress(request));
                        systemLogService.save(systemLog);
                    }

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
            Map<String, String[]> requestMap = savedRequest.getParameterMap();
            String logMsg = "授权登录失败";
            if (Objects.nonNull(requestMap)) {
                String clientId = Arrays.toString(requestMap.get("client_id"));
                String grantType = Arrays.toString(requestMap.get("grant_type"));
                logMsg = "授权登录失败" + grantType + clientId;
            }
            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), logMsg, DateTimeUtil.getNowLocalDateTime(), username, RequestUtil.getIpAddress(request));
            SystemLogService systemLogService = SpringBootUtil.getBean(SystemLogService.class);
            systemLogService.save(systemLog);
            // 会帮我们跳转到上一次请求的页面上
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
