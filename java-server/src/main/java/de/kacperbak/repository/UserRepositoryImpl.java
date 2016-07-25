package de.kacperbak.repository;

import de.kacperbak.domain.StompUser;
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

    private Map<String,StompUser> userMap;

    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();
        createUser(100, "kacper", userMap);
    }

    @Override
    public StompUser findUserByName(String username) {
        return this.userMap.get(username);
    }

    private static void createUser(int count, String userNamePrefix, Map<String, StompUser> userMap){
        Set<GrantedAuthority> set = new HashSet<>();
        set.add(new SimpleGrantedAuthority("ROLE_USER"));
        for(int i = 0; i < count; i++){
            userMap.put(userNamePrefix + i, new StompUser("password", userNamePrefix + i, set));
        }
    }
}
