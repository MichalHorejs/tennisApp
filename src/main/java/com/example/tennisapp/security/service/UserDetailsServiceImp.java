package com.example.tennisapp.security.service;

import com.example.tennisapp.daos.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class represents the service for user details retrieval.
 * It implements the UserDetailsService interface from Spring Security.
 * It is used by Spring Security to load user-specific data during authentication.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserDao userDao;

    /**
     * This method loads the user details by the username (in this case, the phone number).
     * It retrieves the User entity from the database using the UserDao.
     * If the user is not found, it throws a UsernameNotFoundException.
     * @param username The username (phone number) of the user.
     * @return The UserDetails object.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByPhoneNumber(username).orElseThrow();
    }
}
