package de.kacperbak.repository;

import de.kacperbak.domain.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by bakka on 16.05.16.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private Map<String,UserDetailsImpl> userMap = new HashMap<>();

    @PostConstruct
    private void bootstrapUsers(){

        // Authorities
        Set<GrantedAuthority> authorities = Stream.of(new SimpleGrantedAuthority("ROLE_USER")).collect(Collectors.toSet());

        // User credentials
        String userName = "kacper";
        String userPasswordClearText = "password";
        String userPasswordBCryptEncoded = staticHash();

        LOGGER.debug("password in clear text: '{}'", userPasswordClearText);
        LOGGER.debug("password encoded: '{}'", userPasswordBCryptEncoded);

        // Add 'User' to repository
        userMap.put(userName, new UserDetailsImpl(userPasswordBCryptEncoded, userName, authorities, true));
    }

    @Override
    public UserDetailsImpl findUserByName(String username) {
        return this.userMap.get(username);
    }

    private String staticHash(){
        return "$2a$10$VieQ5Itliv2MBFLE.8orGuazoEr5gpgRiQNmJEswG9Ze/pcVa8TFS";
    }

    private String dynamicHash(String password){
        return passwordEncoder.encode(password);
    }
}