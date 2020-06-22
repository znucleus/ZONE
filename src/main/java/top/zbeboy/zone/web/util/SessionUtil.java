package top.zbeboy.zone.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.ObjectUtils;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zone.security.MyUserImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SessionUtil {

    /**
     * 从session中获取用户完整信息
     *
     * @return session中的用户信息
     */
    public static Users getUserFromSession() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = null;
        if (Objects.nonNull(principal) && principal instanceof MyUserImpl) {
            users = ((MyUserImpl) principal).getUsers();
        }
        return users;
    }

    /**
     * 从oauth中获取用户完整信息
     *
     * @return oauth中的用户信息
     */
    public static Users getUserFromOauth(Principal principal) {
        Users users = null;
        if (Objects.nonNull(principal) && principal instanceof OAuth2Authentication) {
            users = ((MyUserImpl) ((OAuth2Authentication) principal).getUserAuthentication().getPrincipal()).getUsers();
        }
        return users;
    }

    /**
     * 根据渠道获取用户信息
     *
     * @param channel   渠道
     * @param principal 用户
     * @return 数据
     */
    public static Users getUserByChannel(String channel, Principal principal) {
        Users users;
        if (StringUtils.equals(Workbook.channel.API.name(), channel)) {
            users = getUserFromOauth(principal);
        } else {
            users = getUserFromSession();
        }
        return users;
    }

    /**
     * 从session中获取用户完整权限
     *
     * @return session中的完整权限
     */
    public static List<String> getAuthoritiesFromSession() {
        List<String> authorities = new ArrayList<>();
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().forEachRemaining(i -> authorities.add(i.getAuthority()));
        return authorities;
    }

    /**
     * 检查当前用户是否有此权限
     *
     * @param role 权限
     * @return true 有 false 无
     */
    public static Boolean isCurrentUserInRole(String role) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (Objects.nonNull(authentication)) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(role));
            }
        }
        return false;
    }

    /**
     * 检查当前用户是否有此权限
     *
     * @param role      权限
     * @param principal oauth用户
     * @return true 有 false 无
     */
    public static Boolean isOauthUserInRole(String role, Principal principal) {
        if (Objects.nonNull(principal)) {
            return ((OAuth2Authentication) principal).getUserAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(role));
        }
        return false;
    }

    /**
     * Check if users is login by remember me cookie, refer
     * org.springframework.security.authentication.AuthenticationTrustResolverImpl
     *
     * @return true or false
     */
    public static Boolean isAnonymousAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !ObjectUtils.isEmpty(authentication) && new AuthenticationTrustResolverImpl().isAnonymous(authentication);
    }
}
