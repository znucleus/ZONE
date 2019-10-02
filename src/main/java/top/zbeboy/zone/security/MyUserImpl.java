package top.zbeboy.zone.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import top.zbeboy.zone.domain.tables.pojos.Users;

import java.util.Collection;

public class MyUserImpl extends User {

    private final Users users;

    MyUserImpl(Users users, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }
}
