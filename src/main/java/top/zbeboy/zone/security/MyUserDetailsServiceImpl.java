package top.zbeboy.zone.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Authorities;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.platform.AuthorizeService;
import top.zbeboy.zbase.feign.platform.UsersService;

import javax.annotation.Resource;
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

    @Resource
    private UsersService usersService;

    @Resource
    private AuthorizeService authorizeService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Username is {}", s);
        String username = StringUtils.deleteWhitespace(s);
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
        }
        assert result.isPresent();
        Users users = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        Optional<List<Authorities>> optionalAuthorities = authorizeService.findByUsername(users.getUsername());
        if(optionalAuthorities.isPresent()){
            authorities = buildUserAuthority(optionalAuthorities.get());
        }
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
        if (Objects.nonNull(authorities) && !authorities.isEmpty()) {
            authorities.forEach(auth -> setAuths.add(new SimpleGrantedAuthority(auth.getAuthority())));
        }
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
        if (Objects.nonNull(users) && StringUtils.isNotBlank(users.getUsername())) {
            enable = Objects.nonNull(users.getEnabled()) && users.getEnabled() == 1;
            accountNonExpired = Objects.nonNull(users.getEnabled()) && users.getAccountNonExpired() == 1;
            credentialsNonExpired = Objects.nonNull(users.getEnabled()) && users.getCredentialsNonExpired() == 1;
            accountNonLocked = Objects.nonNull(users.getEnabled()) && users.getAccountNonLocked() == 1;
            username = users.getUsername();
            password = users.getPassword();
        }
        return new MyUserImpl(users, username, password, enable, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
