package de.kacperbak.repository;

import de.kacperbak.domain.UserDetailsImpl;

/**
 * Created by bakka on 16.05.16.
 */
public interface UserRepository {

    UserDetailsImpl findUserByName(String username);
}
