package de.kacperbak;

/**
 * Created by bakka on 16.05.16.
 */
public interface UserRepository {

    StompUser findUserByName(String username);
}
