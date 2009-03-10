package com.icesoft.icefaces.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        AppUser user = userDao.findUser(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found: " + username);
        else {
            return makeUser(user);
        }
    }

    private org.springframework.security.userdetails.User makeUser(AppUser user) {
        return new org.springframework.security.userdetails.User(user.getLogin(), user
                .getPassword(), true, true, true, true,
                makeGrantedAuthorities(user));
    }

    private GrantedAuthority[] makeGrantedAuthorities(AppUser user) {
        GrantedAuthority[] result = new GrantedAuthority[user.getRoles().size()];
        int i = 0;
        for (String role : user.getRoles()) {
            result[i++] = new GrantedAuthorityImpl(role);
        }
        return result;
    }

}
