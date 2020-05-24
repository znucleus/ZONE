package top.zbeboy.zone.web.util;

import org.springframework.security.core.context.SecurityContextHolder;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.security.MyUserImpl;

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
}
