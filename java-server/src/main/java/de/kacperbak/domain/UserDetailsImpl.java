package de.kacperbak.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Created by bakka on 16.05.16.
 */
public class UserDetailsImpl implements UserDetails {

    private String password;
    private String username;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Set<GrantedAuthority> authorities;

    public UserDetailsImpl(String password, String username, Set<GrantedAuthority> authorities, boolean enabled) {
        this.password = password;
        this.username = username;
        this.authorities = authorities;
        this.isAccountNonLocked = enabled;
        this.isAccountNonExpired = enabled;
        this.isCredentialsNonExpired = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountNonExpired && this.isAccountNonLocked && this.isCredentialsNonExpired;
    }
}
