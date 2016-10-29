package de.kacperbak.service;

import de.kacperbak.domain.UserDetailsImpl;
import de.kacperbak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by bakka on 16.05.16.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetailsImpl userDetailsImpl = userRepository.findUserByName(s);
        if (userDetailsImpl == null){
            throw new UsernameNotFoundException(String.format("User with username: %s not found.", s));
        }
        return userDetailsImpl;
    }
}
