package top.zbeboy.zone.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.zbeboy.zone.domain.tables.pojos.Authorities;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.platform.UsersService;
import top.zbeboy.zone.service.system.AuthoritiesService;
import top.zbeboy.zone.web.system.mail.SystemMailConfig;
import top.zbeboy.zone.web.system.mobile.SystemMobileConfig;

import java.util.*;
import java.util.regex.Pattern;

/**
 * spring security userdetails实现类.
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
@Service("myUserDetailsService")
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(MyUserDetailsServiceImpl.class);

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Username is {}", s);
        String username = StringUtils.trimWhitespace(s);
        Users users = null;
        boolean hasUser = false;
        if (Pattern.matches(SystemMailConfig.MAIL_REGEX, username)) {
            users = usersService.findByEmail(username);
            hasUser = Objects.nonNull(users);
        }

        if (!hasUser && Pattern.matches(SystemMobileConfig.MOBILE_REGEX, username)) {
            users = usersService.findByMobile(username);
            hasUser = Objects.nonNull(users);
        }

        if (!hasUser) {
            users = usersService.findByUsername(username);
        }
        assert users != null;
        List<GrantedAuthority> authorities = buildUserAuthority(authoritiesService.findByUsername(users.getUsername()));
        return buildUserForAuthentication(users, authorities);
    }

    /**
     * 返回验证角色
     *
     * @param authorities 权限
     * @return 组装
     */
    private List<GrantedAuthority> buildUserAuthority(List<Authorities> authorities) {
        Set<GrantedAuthority> setAuths = new HashSet<>();
        authorities.forEach(auth -> setAuths.add(new SimpleGrantedAuthority(auth.getAuthority())));
        return new ArrayList<>(setAuths);
    }

    /**
     * 返回验证用户
     *
     * @param users       用户
     * @param authorities 权限
     * @return 组装
     */
    private MyUserImpl buildUserForAuthentication(Users users, List<GrantedAuthority> authorities) {
        boolean enable = false;
        boolean accountNonExpired = false;
        boolean credentialsNonExpired = false;
        boolean accountNonLocked = false;
        String username = null;
        String password = null;
        if (!ObjectUtils.isEmpty(users)) {
            enable = !ObjectUtils.isEmpty(users.getEnabled()) && users.getEnabled() == 1;
            accountNonExpired = !ObjectUtils.isEmpty(users.getEnabled()) && users.getAccountNonExpired() == 1;
            credentialsNonExpired = !ObjectUtils.isEmpty(users.getEnabled()) && users.getCredentialsNonExpired() == 1;
            accountNonLocked = !ObjectUtils.isEmpty(users.getEnabled()) && users.getAccountNonLocked() == 1;
            username = users.getUsername();
            password = users.getPassword();
        }
        return new MyUserImpl(users, username, password, enable, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
