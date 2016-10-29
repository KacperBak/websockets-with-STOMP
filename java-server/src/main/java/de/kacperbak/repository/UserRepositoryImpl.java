package de.kacperbak.repository;

import de.kacperbak.domain.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by bakka on 16.05.16.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<String,UserDetailsImpl> userMap;

    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();
        Set<GrantedAuthority> set = new HashSet<>();
        set.add(new SimpleGrantedAuthority("ROLE_USER"));
        userMap.put("kacper", new UserDetailsImpl("password", "kacper", set, true));
    }

    @Override
    public UserDetailsImpl findUserByName(String username) {
        return this.userMap.get(username);
    }
}
