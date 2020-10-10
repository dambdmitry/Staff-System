package edu.prctice.staff.security;

import edu.prctice.staff.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private final List<SimpleGrantedAuthority> authorities;
    private final String username;
    private final String password;
    private final boolean is = true;


    public SecurityUser(List<SimpleGrantedAuthority> authorities, String username, String password) {
        this.authorities = authorities;
        this.username = username;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return is;
    }

    @Override
    public boolean isAccountNonLocked() {
        return is;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return is;
    }

    @Override
    public boolean isEnabled() {
        return is;
    }

    public static UserDetails fromUser(User user){
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getRole().getAuthorities());
    }
}
