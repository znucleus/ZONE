package top.zbeboy.zone.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import top.zbeboy.zone.config.Workbook;
import top.zbeboy.zone.domain.tables.pojos.Users;
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
}
